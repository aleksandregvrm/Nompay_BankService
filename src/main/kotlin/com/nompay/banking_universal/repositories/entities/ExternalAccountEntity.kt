package com.nompay.banking_universal.repositories.entities

import com.nompay.banking_universal.repositories.dto.externalAccount.ExternalAccountBilling
import com.nompay.banking_universal.repositories.enums.other.Currencies
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.OneToMany
import jakarta.persistence.PrePersist
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.annotations.UuidGenerator
import org.hibernate.type.SqlTypes
import org.springframework.data.jpa.repository.JpaRepository
import java.sql.Date
import java.time.Instant

@Entity
@Table(
  name = "external_accounts",
  uniqueConstraints = [
    UniqueConstraint(columnNames = ["email"]),
    UniqueConstraint(columnNames = ["iban"])
  ],
  indexes = [
    Index(name = "idx_external_account_email", columnList = "email"),
    Index(name = "idx_iban", columnList = "iban")
  ]
)
class ExternalAccountEntity(
  @Column(name = "email")
  val email: String? = null,

  @Column(name = "name")
  val name: String? = null,

  @Column(name = "surname")
  val surname: String? = null,

  @Column(name = "phone")
  val phone: String? = null,

  @Column(name = "iban")
  val iban: String? = null,

  @Column(name = "currency")
  @Enumerated(EnumType.STRING)
  val currency: Currencies? = null,

  @Column(name = "bank")
  val bank: String? = null,

  @Column(name = "date_of_birth")
  val dateOfBirth: Date? = null,

  @Column
  @JdbcTypeCode(SqlTypes.JSON)
  val externalAccountBilling: ExternalAccountBilling? = null

) {
  @Id
  @UuidGenerator
  val id: String? = null

  @Column(name = "create_date", nullable = false)
  var createDate: Instant? = null

  @OneToMany(mappedBy = "fromExternal", cascade = [CascadeType.ALL], orphanRemoval = true)
  var fromTransactions: MutableList<TransactionEntity> = mutableListOf()

  @OneToMany(mappedBy = "toExternal", cascade = [CascadeType.ALL], orphanRemoval = true)
  var toTransactions: MutableList<TransactionEntity> = mutableListOf()

  @OneToMany(mappedBy = "ownerExternalAccount", cascade = [CascadeType.ALL], orphanRemoval = true)
  var externalAccountTransactions: MutableList<ExternalAccountTransactionsEntity> = mutableListOf()

  @PrePersist
  fun prePersist() {
    val now = Instant.now()
    createDate = now
  }

  override fun toString(): String {
    return "ExternalAccountEntity(email=$email, name=$name, surname=$surname, phone=$phone, iban=$iban, currency=$currency, bank=$bank, dateOfBirth=$dateOfBirth, externalAccountBilling=$externalAccountBilling, id=$id, createDate=$createDate)"
  }
}

interface ExternalAccountEntityRepository : JpaRepository<ExternalAccountEntity, String>{
  fun getExternalAccountByIban(iban: String): ExternalAccountEntity?
}