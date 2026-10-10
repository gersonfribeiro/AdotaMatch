package br.com.adotamatch.features.auth.dto

import br.com.adotamatch.features.auth.domain.EUserRole

/**
 * @description DTO de resposta após autenticação bem-sucedida.
 */
data class CUserAuthResponse(
    val id: Long,
    val name: String,
    val email: String,
    val role: EUserRole,
    val phone: String,
    val city: String,
    val document: String? = null,
    val organizationName: String? = null,
    val token: String
)
