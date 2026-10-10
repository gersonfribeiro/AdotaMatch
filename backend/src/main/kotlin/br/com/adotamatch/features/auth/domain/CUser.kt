package br.com.adotamatch.features.auth.domain

import jakarta.persistence.*
import java.time.LocalDateTime

/**
 * @description Entidade JPA representativa de usuários cadastrados no AdotaMatch.
 */
@Entity
@Table(name = "tb_users")
class CUser(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, length = 150)
    var name: String,

    @Column(nullable = false, unique = true, length = 150)
    var email: String,

    @Column(nullable = false, length = 255)
    var passwordHash: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var role: EUserRole,

    @Column(nullable = false, length = 30)
    var phone: String,

    @Column(nullable = false, length = 100)
    var city: String,

    @Column(length = 30)
    var document: String? = null,

    @Column(length = 150)
    var organizationName: String? = null,

    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
)
