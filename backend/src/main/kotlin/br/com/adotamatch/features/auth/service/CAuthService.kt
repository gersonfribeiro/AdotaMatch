package br.com.adotamatch.features.auth.service

import br.com.adotamatch.features.auth.domain.CUser
import br.com.adotamatch.features.auth.dto.CUserAuthResponse
import br.com.adotamatch.features.auth.dto.CUserLoginRequest
import br.com.adotamatch.features.auth.dto.CUserRegisterRequest
import br.com.adotamatch.features.auth.repository.IUserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.util.Base64
import java.util.UUID

/**
 * @description Serviço responsável pelas regras de autenticação, registro e geração de sessão de usuários.
 */
@Service
class CAuthService(
    private val userRepository: IUserRepository
) {

    /**
     * @description Cadastra um novo usuário vinculando seu papel de acesso.
     * @param pRequest Dados do cadastro.
     * @returns Resposta com dados do perfil e token gerado.
     */
    @Transactional
    fun register(pRequest: CUserRegisterRequest): CUserAuthResponse {
        val cleanEmail = pRequest.email.trim().lowercase()
        if (userRepository.existsByEmail(cleanEmail)) {
            throw IllegalArgumentException("Já existe um usuário cadastrado com o e-mail informado.")
        }

        val hashedPassword = hashPassword(pRequest.password)
        val user = CUser(
            name = pRequest.name.trim(),
            email = cleanEmail,
            passwordHash = hashedPassword,
            role = pRequest.role,
            phone = pRequest.phone.trim(),
            city = pRequest.city.trim(),
            document = pRequest.document?.trim(),
            organizationName = pRequest.organizationName?.trim()
        )

        val saved = userRepository.save(user)
        val token = generateToken(saved)

        return toAuthResponse(saved, token)
    }

    /**
     * @description Realiza a autenticação por e-mail e senha.
     * @param pRequest Credenciais de login.
     * @returns Resposta com dados do perfil e token gerado.
     */
    @Transactional(readOnly = true)
    fun login(pRequest: CUserLoginRequest): CUserAuthResponse {
        val cleanEmail = pRequest.email.trim().lowercase()
        val user = userRepository.findByEmail(cleanEmail)
            ?: throw IllegalArgumentException("E-mail ou senha incorretos.")

        val inputHashed = hashPassword(pRequest.password)
        if (user.passwordHash != inputHashed) {
            throw IllegalArgumentException("E-mail ou senha incorretos.")
        }

        val token = generateToken(user)
        return toAuthResponse(user, token)
    }

    /**
     * @description Recupera os dados do usuário autenticado.
     * @param pId Identificador único do usuário.
     * @returns Dados do perfil.
     */
    @Transactional(readOnly = true)
    fun findById(pId: Long): CUserAuthResponse {
        val user = userRepository.findById(pId)
            .orElseThrow { IllegalArgumentException("Usuário não encontrado.") }
        return toAuthResponse(user, "valid-session-${user.id}")
    }

    private fun hashPassword(pRaw: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(pRaw.toByteArray(StandardCharsets.UTF_8))
        return Base64.getEncoder().encodeToString(digest)
    }

    private fun generateToken(pUser: CUser): String {
        val raw = "${pUser.id}:${pUser.email}:${pUser.role}:${UUID.randomUUID()}"
        return Base64.getUrlEncoder().withoutPadding().encodeToString(raw.toByteArray(StandardCharsets.UTF_8))
    }

    private fun toAuthResponse(pUser: CUser, pToken: String): CUserAuthResponse {
        return CUserAuthResponse(
            id = pUser.id ?: 0L,
            name = pUser.name,
            email = pUser.email,
            role = pUser.role,
            phone = pUser.phone,
            city = pUser.city,
            document = pUser.document,
            organizationName = pUser.organizationName,
            token = pToken
        )
    }
}
