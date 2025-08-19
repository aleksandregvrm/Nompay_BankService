package com.nompay.banking_universal.repositories.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import com.nompay.banking_universal.repositories.enums.Currencies
import jakarta.persistence.*
import org.springframework.data.jpa.repository.JpaRepository
import java.math.BigDecimal


@Entity
@Table(name = "account", indexes = [
  Index(name = "idx_account_email", columnList = "email")
])
class AccountEntity(
  @Column(name = "email", nullable = false)
  var email: String,

  @Column(name = "name", nullable = false)
  var name: String,

  @Enumerated(EnumType.STRING)
  @Column(name = "currency", nullable = false)
  var currency: Currencies,

  @Column(name = "iban", nullable = false)
  val iban: String
  ) {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long? = null

  @Column(name = "balance", nullable = false, columnDefinition = "DECIMAL(19,2) DEFAULT 0")
  var balance: BigDecimal = BigDecimal.ZERO

  @ManyToOne
  @JoinColumn(name = "owner_user_id")
  @JsonIgnore
  lateinit var ownerUser: UserEntity
}

interface AccountEntityRepository : JpaRepository<AccountEntity, Long> {
  fun getAccountByEmail(email: String): AccountEntity
}
