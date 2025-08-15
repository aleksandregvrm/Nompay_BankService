package com.nompay.banking_universal.repositories.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import com.nompay.banking_universal.repositories.enums.Currencies
import jakarta.persistence.*
import org.springframework.data.jpa.repository.JpaRepository


@Entity
@Table(name = "account")
class AccountEntity(
  @Column(name = "email", nullable = false)
  var email: String,

  @Column(name = "name", nullable = false)
  var name: String,

  @Enumerated(EnumType.STRING)
  @Column(name = "currency", nullable = false)
  var currency: Currencies,

  ) {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long? = null

  @ManyToOne
  @JoinColumn(name = "owner_user_id")
  @JsonIgnore
  lateinit var ownerUser: UserEntity
}

interface AccountEntityRepository : JpaRepository<AccountEntity, Long> {
  fun getAccountByEmail(email: String): AccountEntity
}
