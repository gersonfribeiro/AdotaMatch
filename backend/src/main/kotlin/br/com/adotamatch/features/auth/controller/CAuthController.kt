package br.com.adotamatch.features.auth.controller

import br.com.adotamatch.features.auth.dto.CUserAuthResponse
import br.com.adotamatch.features.auth.dto.CUserLoginRequest
import br.com.adotamatch.features.auth.dto.CUserRegisterRequest
import br.com.adotamatch.features.auth.service.CAuthService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * @description Controller REST responsável pelos endpoints de autenticação e controle de perfis de acesso.
 */
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Autenticação e Perfis", description = "Endpoints de login, cadastro e recuperação de sessão para Adotantes, Protetores e Pet Shops")
class CAuthController(
    private val authService: CAuthService
) {

    /**
     * @description Cadastra uma nova conta de usuário com seleção do papel de acesso.
     * @param pRequest Dados do perfil a cadastrar.
     * @returns Resposta HTTP 201 Created com dados e token do usuário.
     */
    @PostMapping("/register")
    @Operation(summary = "Cadastrar Usuário", description = "Registra um novo Adotante, Protetor Independente ou Abrigo/Pet Shop")
    fun register(@Valid @RequestBody pRequest: CUserRegisterRequest): ResponseEntity<CUserAuthResponse> {
        val response = authService.register(pRequest)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    /**
     * @description Efetua a autenticação por e-mail e senha.
     * @param pRequest Credenciais de login.
     * @returns Resposta HTTP 200 OK com dados e token do usuário.
     */
    @PostMapping("/login")
    @Operation(summary = "Autenticar Usuário", description = "Efetua login por e-mail e senha")
    fun login(@Valid @RequestBody pRequest: CUserLoginRequest): ResponseEntity<CUserAuthResponse> {
        val response = authService.login(pRequest)
        return ResponseEntity.ok(response)
    }

    /**
     * @description Recupera os dados do usuário autenticado a partir do ID.
     * @param pId Identificador do usuário.
     * @returns Resposta HTTP 200 OK com o perfil atualizado.
     */
    @GetMapping("/me")
    @Operation(summary = "Obter Dados da Sessão", description = "Retorna os dados do usuário a partir do identificador")
    fun me(@RequestParam pId: Long): ResponseEntity<CUserAuthResponse> {
        val response = authService.findById(pId)
        return ResponseEntity.ok(response)
    }
}
