package br.com.adotamatch.features.adopter.repository

import br.com.adotamatch.features.adopter.domain.CAdoptionIntent
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * @description Repositório Spring Data JPA para manifestações formais de interesse de adoção.
 */
@Repository
interface IAdoptionIntentRepository : JpaRepository<CAdoptionIntent, Long> {

    /**
     * @description Busca manifestações de interesse direcionadas a um determinado animal.
     * @param pPetId Identificador do animal.
     * @returns Lista de intenções de adoção registradas para o animal.
     */
    fun findByPetIdOrderByCompatibilityScoreDesc(pPetId: Long): List<CAdoptionIntent>

    /**
     * @description Busca histórico de intenções submetidas por um adotante.
     * @param pAdopterEmail E-mail do adotante.
     * @returns Lista de manifestações de interesse do adotante.
     */
    fun findByAdopterEmailOrderByCreatedAtDesc(pAdopterEmail: String): List<CAdoptionIntent>
}
