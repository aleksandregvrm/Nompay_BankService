package com.nompay.banking_universal.repositories.entities

import jakarta.persistence.*
import org.springframework.data.jpa.repository.JpaRepository
import java.sql.Date
import java.time.Instant

@Entity
@Table(
  name = "user",
  uniqueConstraints = [
    UniqueConstraint(columnNames = ["email"]),
    UniqueConstraint(columnNames = ["username"])
  ]
)
class UserEntity(
  @Column(name = "email", nullable = false, unique = true)
  var email: String,

  @Column(name = "name", nullable = false)
  var name: String,

  @Column(name = "username", nullable = false, unique = true)
  var username: String,

  @Column(name = "birth_date", nullable = false)
  var birthDate: Date
) {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long? = null

  @Column(name = "create_date", nullable = false)
  var createDate: Instant? = null

  @Column(name = "password", nullable = false)
  lateinit var password: String

  @Column(name = "update_date")
  var updateDate: Instant? = null

  @OneToMany(mappedBy = "ownerUser", cascade = [CascadeType.ALL], orphanRemoval = true)
  var accounts: MutableSet<AccountEntity> = mutableSetOf()

  @PrePersist
  fun prePersist() {
    val now = Instant.now()
    createDate = now
    updateDate = now
  }

  @PreUpdate
  fun preUpdate() {
    updateDate = Instant.now()
  }
}

interface UserEntityRepository : JpaRepository<UserEntity, Long> {
  fun findUserByEmail(email: String): UserEntity?
}
