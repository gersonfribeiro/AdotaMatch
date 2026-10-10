package br.com.adotamatch.e2e

import org.junit.jupiter.api.Test
import org.springframework.web.client.RestTemplate

/**
 * @description Teste utilitário para popular a instância local ativa (http://localhost:8080)
 * com os 50 novos animais (25 cães e 25 gatos) do CPopulationDataset.
 */
class CPopulateLiveRunnerTest {

    @Test
    fun `deve popular servidor local ativo caso esteja em execucao`() {
        val restTemplate = RestTemplate()
        val targetUrl = "http://localhost:8080/api/v1/pets"

        try {
            val currentPets = restTemplate.getForEntity(targetUrl, Array<Any>::class.java)
            val currentCount = currentPets.body?.size ?: 0
            println("Total de animais atual no servidor ativo: $currentCount")

            val dogs = CPopulationDataset.getDogs()
            var dogsCreated = 0
            for (dog in dogs) {
                try {
                    restTemplate.postForEntity(targetUrl, dog, String::class.java)
                    dogsCreated++
                } catch (e: Exception) {
                    System.err.println("Erro ao cadastrar cão ${dog.name}: ${e.message}")
                }
            }
            println("Cães cadastrados com sucesso no servidor ativo: $dogsCreated de ${dogs.size}")

            val cats = CPopulationDataset.getCats()
            var catsCreated = 0
            for (cat in cats) {
                try {
                    restTemplate.postForEntity(targetUrl, cat, String::class.java)
                    catsCreated++
                } catch (e: Exception) {
                    System.err.println("Erro ao cadastrar gato ${cat.name}: ${e.message}")
                }
            }
            println("Gatos cadastrados com sucesso no servidor ativo: $catsCreated de ${cats.size}")

            val updatedPets = restTemplate.getForEntity(targetUrl, Array<Any>::class.java)
            println("Total atualizado de animais no servidor ativo: ${updatedPets.body?.size ?: 0}")
        } catch (e: Exception) {
            println("Servidor ativo em http://localhost:8080 não disponível ou falhou: ${e.message}")
        }
    }
}
