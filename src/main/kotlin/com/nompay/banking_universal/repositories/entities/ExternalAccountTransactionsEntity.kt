package com.nompay.banking_universal.repositories.entities

import com.nompay.banking_universal.repositories.dto.externalAccount.ExternalAccountBilling
import com.nompay.banking_universal.repositories.enums.other.Currencies
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToOne
import jakarta.persistence.PrePersist
import jakarta.persistence.Table
import org.springframework.data.jpa.repository.JpaRepository
import java.math.BigDecimal
import java.sql.Date
import java.time.Instant

@Entity
@Table(
  name = "external_account_transactions",
  indexes = [
    Index(name = "idx_external_account_transactions_email", columnList = "email"),
    Index(name = "idx_transaction_id", columnList = "transactionId")
  ]
)
class ExternalAccountTransactionsEntity(
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

  @Column(name = "date_of_birth")
  val dateOfBirth: Date? = null,

  @Column(name = "external_account_billing_transaction")
  val externalAccountBillingTransaction: ExternalAccountBilling? = null,

  @Column(name = "amount")
  val amount: BigDecimal,

  @Column(name = "currency")
  val currency: Currencies,

  @Column(name = "to_iban")
  val toIban: String? = null,

  @Column(name = "bank")
  val bank: String? = null,

  @Column(name = "to_email")
  val toEmail: String? = null,

  @Column(name = "merchant_transfer")
  val merchantTransfer: Boolean? = false,

  @Column(name = "external_transaction_id")
  val externalTransactionId: String? = null,

  @Column(name = "notification_url")
  val notificationUrl: String? = null,

  @Column(name = "transactionId", nullable = false, unique = true)
  val transactionId: String,

  @OneToOne(mappedBy = "externalReferencedTransaction")
  var referencedTransaction: TransactionEntity? = null
) {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long? = null

  @ManyToOne
  @JoinColumn(name = "owner_external_account")
  val ownerExternalAccount: ExternalAccountEntity? = null

  @Column(name = "create_date", nullable = false)
  var createDate: Instant? = null

  @PrePersist
  fun prePersist() {
    val now = Instant.now()
    createDate = now
  }
}

interface ExternalAccountTransactionEntityRepository : JpaRepository<ExternalAccountTransactionsEntity, Long> {

}