package br.com.adotamatch.features.auth.repository

import br.com.adotamatch.features.auth.domain.CUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * @description Repositório de dados JPA para usuários autenticados.
 */
@Repository
interface IUserRepository : JpaRepository<CUser, Long> {

    /**
     * @description Localiza um usuário pelo seu endereço de e-mail único.
     * @param pEmail Endereço de e-mail do usuário.
     * @returns Instância de CUser ou nulo se não encontrado.
     */
    fun findByEmail(pEmail: String): CUser?

    /**
     * @description Verifica se já existe registro com o e-mail informado.
     * @param pEmail Endereço de e-mail a consultar.
     * @returns Booleano indicando existência prévia.
     */
    fun existsByEmail(pEmail: String): Boolean
}
