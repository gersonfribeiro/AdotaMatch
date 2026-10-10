package br.com.adotamatch.features.recommendation.domain

/**
 * @description Detalhamento discriminado dos 5 critérios convexos de similaridade e do alinhamento semântico da LLM.
 * Atende aos requisitos de Explicabilidade Transparente da Ciência da Computação (XAI) definidos no artigo de TCC.
 *
 * @property energyTimeScore Similaridade s1: Nível de energia do animal vs tempo disponível do adotante (0 a 100).
 * @property familyScore Similaridade s2: Convivência familiar com crianças e outros animais domésticos (0 a 100).
 * @property housingSizeScore Similaridade s3: Espaço de moradia do lar vs porte físico do animal (0 a 100).
 * @property solitudeScore Similaridade s4: Tolerância à solidão diária vs rotina de ausência do tutor (0 a 100).
 * @property experienceCareScore Similaridade s5: Experiência prévia com pets vs necessidades especiais de saúde (0 a 100).
 * @property llmSemanticScore Escore contextual s_LLM: Alinhamento semântico entre relato livre e diário da ONG (0 a 100).
 */
data class CCriteriaBreakdown(
    val energyTimeScore: Double,
    val familyScore: Double,
    val housingSizeScore: Double,
    val solitudeScore: Double,
    val experienceCareScore: Double,
    val llmSemanticScore: Double
)
