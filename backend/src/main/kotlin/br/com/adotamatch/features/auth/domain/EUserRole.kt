package br.com.adotamatch.features.auth.domain

/**
 * @description Perfis de usuário do ecossistema AdotaMatch.
 */
enum class EUserRole {
    /**
     * @description Cidadão interessado em encontrar e adotar um animal de forma responsável.
     */
    ADOPTER,

    /**
     * @description Pessoa física (tutor ou protetor independente) que resgatou ou possui animal para doação.
     */
    GUARDIAN,

    /**
     * @description Instituição formal (ONG, abrigo municipal ou Pet Shop parceiro) com múltiplos animais sob custódia.
     */
    SHELTER
}
