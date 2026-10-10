package br.com.adotamatch.features.recommendation.client

import br.com.adotamatch.features.pet.domain.CPet
import br.com.adotamatch.features.recommendation.dto.CRecommendationRequest
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration

/**
 * @description Cliente de inferência em LLMs com suporte a múltiplos provedores e fallback heurístico automático.
 * Consulta APIs externas de ponta quando configuradas e comuta silenciosamente para o motor heurístico em caso de timeout ou indisponibilidade.
 */
@Component
@Primary
class CExternalLlmClient(
    private val heuristicFallbackClient: CHeuristicFallbackClient,
    private val objectMapper: ObjectMapper,
    @Value("\${adotamatch.recommendation.llm.provider:auto}") private val provider: String,
    @Value("\${adotamatch.recommendation.llm.timeout-ms:4000}") private val timeoutMs: Long
) : ILlmClient {

    private val logger = LoggerFactory.getLogger(CExternalLlmClient::class.java)
    private val httpClient = HttpClient.newBuilder()
        .connectTimeout(Duration.ofMillis(timeoutMs))
        .build()

    companion object {
        private val circuitOpen = java.util.concurrent.atomic.AtomicBoolean(false)
        private var lastFailureTimestamp = 0L
        private const val CIRCUIT_COOLDOWN_MS = 60_000L
    }

    override fun evaluate(pRequest: CRecommendationRequest, pPet: CPet): CLlmEvaluationResult {
        // Se configurado para modo semântico/local ou narrativa curta, executa imediatamente sem latência de rede
        if (provider.equals("semantic", ignoreCase = true) ||
            provider.equals("local", ignoreCase = true) ||
            provider.equals("mock-fallback", ignoreCase = true) ||
            pRequest.lifestyleNarrative.isBlank() ||
            pRequest.lifestyleNarrative.length < 15
        ) {
            return heuristicFallbackClient.evaluate(pRequest, pPet)
        }

        // Circuit breaker: se houve falha ou timeout recente de API externa, não bloqueia requisições
        val now = System.currentTimeMillis()
        if (circuitOpen.get()) {
            if (now - lastFailureTimestamp < CIRCUIT_COOLDOWN_MS) {
                return heuristicFallbackClient.evaluate(pRequest, pPet)
            } else {
                circuitOpen.set(false)
            }
        }

        // Tenta obter credenciais de ambiente
        val openRouterKey = System.getenv("API_KEY_OPENROUTER")
        val openAiKey = System.getenv("API_KEY_AGENTES_OPEN_AI") ?: System.getenv("OPENAI_API_KEY")

        if (openRouterKey.isNullOrBlank() && openAiKey.isNullOrBlank()) {
            return heuristicFallbackClient.evaluate(pRequest, pPet)
        }

        try {
            // Tenta OpenRouter com timeout seguro
            if (!openRouterKey.isNullOrBlank()) {
                val result = callOpenRouter(pRequest, pPet, openRouterKey)
                if (result != null) return result
            }

            // Tenta OpenAI se disponível
            if (!openAiKey.isNullOrBlank()) {
                val result = callOpenAi(pRequest, pPet, openAiKey)
                if (result != null) return result
            }
        } catch (pEx: Exception) {
            logger.warn("Falha ou timeout na chamada ao LLM externo: {}. Abrindo circuit breaker e ativando motor semântico local.", pEx.message)
            circuitOpen.set(true)
            lastFailureTimestamp = System.currentTimeMillis()
        }

        // Fallback garantido com o motor semântico refinado
        return heuristicFallbackClient.evaluate(pRequest, pPet)
    }

    private fun callOpenRouter(pRequest: CRecommendationRequest, pPet: CPet, pApiKey: String): CLlmEvaluationResult? {
        val prompt = buildPrompt(pRequest, pPet)
        val payload = mapOf(
            "model" to "nvidia/nemotron-3.5-lightning:free",
            "messages" to listOf(mapOf("role" to "user", "content" to prompt)),
            "temperature" to 0.2
        )

        val request = HttpRequest.newBuilder()
            .uri(URI.create("https://openrouter.ai/api/v1/chat/completions"))
            .timeout(Duration.ofMillis(timeoutMs))
            .header("Authorization", "Bearer $pApiKey")
            .header("Content-Type", "application/json")
            .header("HTTP-Referer", "https://adotamatch.com.br")
            .header("X-Title", "AdotaMatch")
            .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(payload)))
            .build()

        val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())
        if (response.statusCode() in 200..299) {
            val root = objectMapper.readTree(response.body())
            val content = root.path("choices").path(0).path("message").path("content").asText()
            return parseLlmJson(content)
        }
        return null
    }

    private fun callOpenAi(pRequest: CRecommendationRequest, pPet: CPet, pApiKey: String): CLlmEvaluationResult? {
        val prompt = buildPrompt(pRequest, pPet)
        val payload = mapOf(
            "model" to "gpt-4o-mini",
            "messages" to listOf(mapOf("role" to "user", "content" to prompt)),
            "response_format" to mapOf("type" to "json_object"),
            "temperature" to 0.2
        )

        val request = HttpRequest.newBuilder()
            .uri(URI.create("https://api.openai.com/v1/chat/completions"))
            .timeout(Duration.ofMillis(timeoutMs))
            .header("Authorization", "Bearer $pApiKey")
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(payload)))
            .build()

        val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())
        if (response.statusCode() in 200..299) {
            val root = objectMapper.readTree(response.body())
            val content = root.path("choices").path(0).path("message").path("content").asText()
            return parseLlmJson(content)
        }
        return null
    }

    private fun buildPrompt(pRequest: CRecommendationRequest, pPet: CPet): String {
        return """
            Você é o motor de inteligência e recomendação explicável do AdotaMatch (TCC UNIFAGOC).
            Avalie semanticamente a compatibilidade entre a rotina do adotante e o animal para adoção responsável e prevenção de devoluções.

            DADOS DO ANIMAL:
            - Nome: ${pPet.name}
            - Espécie: ${pPet.species}
            - Porte: ${pPet.size}
            - Idade: ${pPet.ageMonths} meses
            - Energia: ${pPet.energyLevel}/5
            - Tolerância à solidão: ${pPet.solitaryToleranceHours} horas
            - Sociável com crianças: ${pPet.kidsFriendly}
            - Sociável com pets: ${pPet.petsFriendly}
            - Cuidados especiais: ${pPet.requiresSpecialCare}
            - Histórico/Biografia: ${pPet.biography}
            - Notas do cuidador: ${pPet.behavioralNotes}

            DADOS DO ADOTANTE:
            - Moradia: ${pRequest.housingType}
            - Tempo diário disponível: ${pRequest.dailyHoursAvailable}h
            - Presença de crianças: ${pRequest.hasChildren}
            - Outros pets: ${pRequest.hasOtherPets}
            - Experiência prévia (1 a 3): ${pRequest.experienceLevel}
            - Relato de Estilo de Vida: "${pRequest.lifestyleNarrative}"

            DIRETRIZES SEMÂNTICAS OBRIGATÓRIAS:
            1. Analise se a rotina é presencial ou remota. Se for presencial, NUNCA afirme que trabalha em home office.
            2. Se houver menção a outros moradores (ex: irmã, cônjuge), reconheça sua disponibilidade de cuidado.
            3. Aponte fatores de afinidade reais (alignmentFactors) e alertas preventivos transparentes (preventiveWarnings).
            
            Responda EXCLUSIVAMENTE em formato JSON:
            {
              "semanticScore": 0.0 a 1.0,
              "alignmentFactors": ["..."],
              "preventiveWarnings": ["..."]
            }
        """.trimIndent()
    }

    private fun parseLlmJson(pContent: String): CLlmEvaluationResult? {
        return try {
            val cleaned = pContent.trim()
                .removePrefix("```json")
                .removePrefix("```")
                .removeSuffix("```")
                .trim()
            val node = objectMapper.readTree(cleaned)
            val score = node.path("semanticScore").asDouble(0.75)
            val factors = node.path("alignmentFactors").map { it.asText() }
            val warnings = node.path("preventiveWarnings").map { it.asText() }

            if (factors.isNotEmpty() || warnings.isNotEmpty()) {
                CLlmEvaluationResult(
                    semanticScore = score.coerceIn(0.10, 1.00),
                    alignmentFactors = factors,
                    preventiveWarnings = warnings
                )
            } else {
                null
            }
        } catch (pEx: Exception) {
            null
        }
    }
}
