package br.com.adotamatch.features.auth.dto

import br.com.adotamatch.features.auth.domain.EUserRole
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

/**
 * @description DTO de requisição para cadastro de novo usuário na plataforma.
 */
data class CUserRegisterRequest(
    @field:NotBlank(message = "O nome é obrigatório")
    val name: String,

    @field:NotBlank(message = "O e-mail é obrigatório")
    @field:Email(message = "Informe um e-mail válido")
    val email: String,

    @field:NotBlank(message = "A senha é obrigatória")
    @field:Size(min = 6, message = "A senha deve conter no mínimo 6 caracteres")
    val password: String,

    @field:NotNull(message = "O papel de usuário é obrigatório")
    val role: EUserRole,

    @field:NotBlank(message = "O telefone de contato é obrigatório")
    val phone: String,

    @field:NotBlank(message = "A cidade/UF é obrigatória")
    val city: String,

    val document: String? = null,

    val organizationName: String? = null
)
