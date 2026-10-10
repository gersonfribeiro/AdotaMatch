package br.com.adotamatch.features.auth.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

/**
 * @description DTO de requisição para autenticação de usuário.
 */
data class CUserLoginRequest(
    @field:NotBlank(message = "O e-mail é obrigatório")
    @field:Email(message = "Informe um e-mail válido")
    val email: String,

    @field:NotBlank(message = "A senha é obrigatória")
    val password: String
)
