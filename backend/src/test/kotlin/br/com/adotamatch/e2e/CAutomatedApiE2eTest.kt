package br.com.adotamatch.e2e

import br.com.adotamatch.features.adopter.dto.CAdoptionIntentRequest
import br.com.adotamatch.features.adopter.dto.CAdoptionIntentResponse
import br.com.adotamatch.features.auth.domain.EUserRole
import br.com.adotamatch.features.auth.dto.CUserAuthResponse
import br.com.adotamatch.features.auth.dto.CUserLoginRequest
import br.com.adotamatch.features.auth.dto.CUserRegisterRequest
import br.com.adotamatch.features.pet.domain.EPetStatus
import br.com.adotamatch.features.pet.domain.ESpecies
import br.com.adotamatch.features.pet.dto.CPetResponse
import br.com.adotamatch.features.pet.dto.CPetUpdateRequest
import br.com.adotamatch.features.recommendation.dto.CRecommendationResponse
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

/**
 * @description Suíte de testes automatizados ponta a ponta (E2E) consumindo todos os endpoints da API RESTful do AdotaMatch.
 * Executa o fluxo completo: autenticação de múltiplos atores, cadastro em massa de 50 novos animais (25 cães e 25 gatos),
 * simulação de recomendação com IA para diversas personas e tramitação de protocolos de adoção.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class CAutomatedApiE2eTest {

    @LocalServerPort
    private var port: Int = 0

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    private fun baseUrl(pPath: String): String = "http://localhost:$port/api/v1$pPath"

    companion object {
        var createdPetIds: MutableList<Long> = mutableListOf()
        var testAdopterToken: String = ""
        var testAdopterId: Long = 0L
    }

    /**
     * @description Teste 1: Fluxo de autenticação, cadastro e obtenção de sessão para Adotante, Protetor e Pet Shop.
     */
    @Test
    @Order(1)
    fun `deve cadastrar e autenticar usuarios para todos os perfis de acesso do sistema`() {
        // 1.1 Cadastro de Adotante
        val adopterReq = CUserRegisterRequest(
            name = "Carlos Eduardo Adotante",
            email = "carlos.adotante@teste.com.br",
            password = "senhaSegura123",
            role = EUserRole.ADOPTER,
            phone = "32988112233",
            city = "Ubá - MG"
        )
        val adopterResp = restTemplate.postForEntity(baseUrl("/auth/register"), adopterReq, CUserAuthResponse::class.java)
        assertEquals(HttpStatus.CREATED, adopterResp.statusCode)
        assertNotNull(adopterResp.body?.token)
        assertEquals("Carlos Eduardo Adotante", adopterResp.body?.name)
        assertEquals(EUserRole.ADOPTER, adopterResp.body?.role)
        testAdopterId = adopterResp.body?.id ?: 0L

        // 1.2 Cadastro de Protetor Independente
        val guardianReq = CUserRegisterRequest(
            name = "Fernanda Resgatista Protetora",
            email = "fernanda.protetora@teste.com.br",
            password = "senhaSegura123",
            role = EUserRole.GUARDIAN,
            phone = "32988445566",
            city = "Ubá - MG"
        )
        val guardianResp = restTemplate.postForEntity(baseUrl("/auth/register"), guardianReq, CUserAuthResponse::class.java)
        assertEquals(HttpStatus.CREATED, guardianResp.statusCode)
        assertEquals(EUserRole.GUARDIAN, guardianResp.body?.role)

        // 1.3 Cadastro de Pet Shop / Abrigo Parceiro com CNPJ
        val shelterReq = CUserRegisterRequest(
            name = "Pet Shop & Resgate Quatro Patas",
            email = "petshop.quatropatas@teste.com.br",
            password = "senhaSegura123",
            role = EUserRole.SHELTER,
            phone = "32988778899",
            city = "Ubá - MG",
            document = "98.765.432/0001-10",
            organizationName = "Quatro Patas Comércio e Resgate Ltda"
        )
        val shelterResp = restTemplate.postForEntity(baseUrl("/auth/register"), shelterReq, CUserAuthResponse::class.java)
        assertEquals(HttpStatus.CREATED, shelterResp.statusCode)
        assertEquals(EUserRole.SHELTER, shelterResp.body?.role)
        assertEquals("Quatro Patas Comércio e Resgate Ltda", shelterResp.body?.organizationName)

        // 1.4 Login do Adotante e obtenção de token
        val loginReq = CUserLoginRequest(email = "carlos.adotante@teste.com.br", password = "senhaSegura123")
        val loginResp = restTemplate.postForEntity(baseUrl("/auth/login"), loginReq, CUserAuthResponse::class.java)
        assertEquals(HttpStatus.OK, loginResp.statusCode)
        testAdopterToken = loginResp.body?.token ?: ""
        assertTrue(testAdopterToken.isNotBlank())

        // 1.5 Validação do endpoint /me
        val meResp = restTemplate.getForEntity(baseUrl("/auth/me?pId=$testAdopterId"), CUserAuthResponse::class.java)
        assertEquals(HttpStatus.OK, meResp.statusCode)
        assertEquals("carlos.adotante@teste.com.br", meResp.body?.email)
    }

    /**
     * @description Teste 2: Cadastro em massa de 50 novos animais (25 cães e 25 gatos) com métricas variáveis.
     * Consome POST /api/v1/pets, GET /api/v1/pets, GET /api/v1/pets/{id} e PUT /api/v1/pets/{id}.
     */
    @Test
    @Order(2)
    fun `deve cadastrar 50 novos animais com metricas variaveis sendo 25 caes e 25 gatos`() {
        val dogs = CPopulationDataset.getDogs()
        val cats = CPopulationDataset.getCats()

        assertEquals(25, dogs.size, "O dataset de cães deve conter exatamente 25 espécimes")
        assertEquals(25, cats.size, "O dataset de gatos deve conter exatamente 25 espécimes")

        // 2.1 Cadastra os 25 cães via POST /pets
        var registeredDogsCount = 0
        for (dogReq in dogs) {
            val response = restTemplate.postForEntity(baseUrl("/pets"), dogReq, CPetResponse::class.java)
            assertEquals(HttpStatus.CREATED, response.statusCode, "Falha ao cadastrar cão ${dogReq.name}")
            val pet = response.body
            assertNotNull(pet)
            assertTrue((pet?.id ?: 0L) > 0L)
            assertEquals(dogReq.name, pet?.name)
            assertEquals(ESpecies.DOG, pet?.species)
            assertEquals(EPetStatus.AVAILABLE, pet?.status)
            createdPetIds.add(pet!!.id)
            registeredDogsCount++
        }
        assertEquals(25, registeredDogsCount, "Todos os 25 cães devem ser cadastrados com sucesso")

        // 2.2 Cadastra os 25 gatos via POST /pets
        var registeredCatsCount = 0
        for (catReq in cats) {
            val response = restTemplate.postForEntity(baseUrl("/pets"), catReq, CPetResponse::class.java)
            assertEquals(HttpStatus.CREATED, response.statusCode, "Falha ao cadastrar gato ${catReq.name}")
            val pet = response.body
            assertNotNull(pet)
            assertTrue((pet?.id ?: 0L) > 0L)
            assertEquals(catReq.name, pet?.name)
            assertEquals(ESpecies.CAT, pet?.species)
            assertEquals(EPetStatus.AVAILABLE, pet?.status)
            createdPetIds.add(pet!!.id)
            registeredCatsCount++
        }
        assertEquals(25, registeredCatsCount, "Todos os 25 gatos devem ser cadastrados com sucesso")

        // 2.3 Consulta geral via GET /pets
        val allPetsResp = restTemplate.getForEntity(baseUrl("/pets"), Array<CPetResponse>::class.java)
        assertEquals(HttpStatus.OK, allPetsResp.statusCode)
        val allPets = allPetsResp.body ?: emptyArray()
        assertTrue(allPets.size >= 50, "O catálogo deve conter ao menos 50 animais cadastrados")

        // 2.4 Filtro por espécie DOG
        val dogsFilterResp = restTemplate.getForEntity(baseUrl("/pets?pSpecies=DOG"), Array<CPetResponse>::class.java)
        assertEquals(HttpStatus.OK, dogsFilterResp.statusCode)
        val dogsList = dogsFilterResp.body ?: emptyArray()
        assertTrue(dogsList.size >= 25, "O filtro por espécie deve retornar pelo menos 25 cães")
        assertTrue(dogsList.all { it.species == ESpecies.DOG })

        // 2.5 Filtro por espécie CAT
        val catsFilterResp = restTemplate.getForEntity(baseUrl("/pets?pSpecies=CAT"), Array<CPetResponse>::class.java)
        assertEquals(HttpStatus.OK, catsFilterResp.statusCode)
        val catsList = catsFilterResp.body ?: emptyArray()
        assertTrue(catsList.size >= 25, "O filtro por espécie deve retornar pelo menos 25 gatos")
        assertTrue(catsList.all { it.species == ESpecies.CAT })

        // 2.6 Consulta detalhada por ID GET /pets/{id}
        val firstPetId = createdPetIds.first()
        val singlePetResp = restTemplate.getForEntity(baseUrl("/pets/$firstPetId"), CPetResponse::class.java)
        assertEquals(HttpStatus.OK, singlePetResp.statusCode)
        assertEquals(firstPetId, singlePetResp.body?.id)

        // 2.7 Atualização via PUT /pets/{id}
        val updateReq = CPetUpdateRequest(
            name = singlePetResp.body!!.name,
            species = singlePetResp.body!!.species,
            gender = singlePetResp.body!!.gender,
            size = singlePetResp.body!!.size,
            ageMonths = singlePetResp.body!!.ageMonths,
            status = EPetStatus.AVAILABLE,
            energyLevel = singlePetResp.body!!.energyLevel,
            solitaryToleranceHours = singlePetResp.body!!.solitaryToleranceHours,
            kidsFriendly = singlePetResp.body!!.kidsFriendly,
            petsFriendly = singlePetResp.body!!.petsFriendly,
            requiresSpecialCare = singlePetResp.body!!.requiresSpecialCare,
            isVaccinated = true,
            isNeutered = true,
            photoUrl = singlePetResp.body!!.photoUrl,
            additionalPhotos = singlePetResp.body!!.additionalPhotos,
            videoUrl = singlePetResp.body!!.videoUrl,
            biography = singlePetResp.body!!.biography,
            behavioralNotes = "Atualizado pelo teste automatizado com excelente adaptação.",
            shelterName = singlePetResp.body!!.shelterName,
            shelterCity = singlePetResp.body!!.shelterCity,
            contactWhatsapp = singlePetResp.body!!.contactWhatsapp
        )
        val updateResp = restTemplate.exchange(
            baseUrl("/pets/$firstPetId"),
            HttpMethod.PUT,
            HttpEntity(updateReq),
            CPetResponse::class.java
        )
        assertEquals(HttpStatus.OK, updateResp.statusCode)
        assertEquals("Atualizado pelo teste automatizado com excelente adaptação.", updateResp.body?.behavioralNotes)
    }

    /**
     * @description Teste 3: Simulação de iterações com múltiplos adotantes (usuários finais) no motor de recomendação.
     * Consome POST /api/v1/recommendations/match para 5 personas socioambientais diferentes e valida os escores.
     */
    @Test
    @Order(3)
    fun `deve simular iteracoes de recomendacao com IA para multiplos perfis de adotantes`() {
        val personas = CPopulationDataset.getAdopterPersonas()
        assertTrue(personas.size >= 5, "Devem existir pelo menos 5 personas de teste")

        for ((index, persona) in personas.withIndex()) {
            val response = restTemplate.postForEntity(
                baseUrl("/recommendations/match"),
                persona,
                Array<CRecommendationResponse>::class.java
            )
            assertEquals(HttpStatus.OK, response.statusCode, "Falha na recomendação para Persona $index")
            val matches = response.body ?: emptyArray()
            assertTrue(matches.isNotEmpty(), "A recomendação para Persona $index deve retornar animais compatíveis")

            // Valida ordenação decrescente por score de compatibilidade
            val scores = matches.map { it.compatibilityScore }
            val sortedScores = scores.sortedDescending()
            assertEquals(sortedScores, scores, "Os animais recomendados devem estar ordenados decrescentemente por afinidade")

            // Valida explicabilidade: fatores de afinidade e alertas preventivos não vazios e sem termos genéricos
            val topMatch = matches.first()
            assertTrue(topMatch.compatibilityScore > 0.0)
            assertTrue(topMatch.alignmentFactors.isNotEmpty(), "Top match deve conter fatores de alinhamento positivos")
            assertTrue(topMatch.preventiveWarnings.isNotEmpty(), "Top match deve conter alertas preventivos transparentes")

            // Garante ausência de frases genéricas
            val hasGenericPhrases = topMatch.alignmentFactors.any { it.contains("correspondência satisfatória") }
            assertFalse(hasGenericPhrases, "Não devem existir frases vagas pré-fabricadas nos fatores de afinidade")
        }
    }

    /**
     * @description Teste 4: Tramitação de intenção de adoção entre Adotante e Pet Shop / Mantenedor.
     * Consome POST /api/v1/adoptions/intent, GET /api/v1/adoptions/intent/my e GET /api/v1/adoptions/intent/pet/{petId}.
     */
    @Test
    @Order(4)
    fun `deve submeter intencao de adocao e consultar protocolo pelo adotante e pela entidade mantenedora`() {
        val targetPetId = createdPetIds.first()
        val petResp = restTemplate.getForEntity(baseUrl("/pets/$targetPetId"), CPetResponse::class.java)
        val petName = petResp.body?.name ?: "Animal de Teste"

        // 4.1 Adotante manifesta interesse formal
        val intentReq = CAdoptionIntentRequest(
            petId = targetPetId,
            petName = petName,
            adopterName = "Carlos Eduardo Adotante",
            adopterEmail = "carlos.adotante@teste.com.br",
            adopterPhone = "32988112233",
            adopterCity = "Ubá - MG",
            compatibilityScore = 88.5,
            adopterNarrative = "Rotina estável com disponibilidade para dedicação integral aos cuidados do animal.",
            messageToShelter = "Olá equipe do abrigo! Gostaria muito de agendar uma visita presencial para conhecer o $petName."
        )

        val intentResp = restTemplate.postForEntity(baseUrl("/adoptions/intent"), intentReq, CAdoptionIntentResponse::class.java)
        assertEquals(HttpStatus.CREATED, intentResp.statusCode)
        val intent = intentResp.body
        assertNotNull(intent)
        assertTrue((intent?.id ?: 0L) > 0L)
        assertEquals(targetPetId, intent?.petId)
        assertEquals(petName, intent?.petName)
        assertEquals("Carlos Eduardo Adotante", intent?.adopterName)
        assertEquals("Olá equipe do abrigo! Gostaria muito de agendar uma visita presencial para conhecer o $petName.", intent?.messageToShelter)

        // 4.2 Adotante consulta suas manifestações na aba Minhas Adoções
        val myIntentsResp = restTemplate.getForEntity(
            baseUrl("/adoptions/intent/my?pEmail=carlos.adotante@teste.com.br"),
            Array<CAdoptionIntentResponse>::class.java
        )
        assertEquals(HttpStatus.OK, myIntentsResp.statusCode)
        val myIntents = myIntentsResp.body ?: emptyArray()
        assertTrue(myIntents.any { it.id == intent?.id }, "O protocolo criado deve constar na listagem do adotante")

        // 4.3 Pet Shop / Abrigo consulta candidatos interessados no animal
        val petIntentsResp = restTemplate.getForEntity(
            baseUrl("/adoptions/intent/pet/$targetPetId"),
            Array<CAdoptionIntentResponse>::class.java
        )
        assertEquals(HttpStatus.OK, petIntentsResp.statusCode)
        val petIntents = petIntentsResp.body ?: emptyArray()
        assertTrue(petIntents.any { it.id == intent?.id }, "O candidato deve constar na fila de triagem do animal sob custódia")
    }
}
