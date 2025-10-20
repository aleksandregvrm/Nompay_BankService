package com.nompay.banking_universal.repositories.entities

import com.nompay.banking_universal.repositories.enums.other.Currencies
import com.nompay.banking_universal.repositories.enums.transactions.TransactionTypes
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToOne
import jakarta.persistence.PrePersist
import jakarta.persistence.Table
import org.hibernate.annotations.UuidGenerator
import org.springframework.data.jpa.repository.JpaRepository
import java.math.BigDecimal
import java.time.Instant

@Entity
@Table(
  name = "transactions", indexes = [
    Index(name = "transaction_id", columnList = "transaction_id"),
    Index(name = "from_user_id", columnList = "from_user_id"),
    Index(name = "to_user_id", columnList = "to_user_id")
  ]
)
class TransactionEntity(

  @ManyToOne
  @JoinColumn(name = "from_user_id")
  var fromUser: UserEntity? = null,

  @ManyToOne
  @JoinColumn(name = "to_user_id")
  var toUser: UserEntity? = null,

  @ManyToOne
  @JoinColumn(name = "from_merchant")
  var fromMerchant: MerchantEntity? = null,

  @ManyToOne
  @JoinColumn(name = "to_merchant")
  var toMerchant: MerchantEntity? = null,

  @ManyToOne
  @JoinColumn(name = "from_external")
  var fromExternal: ExternalAccountEntity? = null,

  @ManyToOne
  @JoinColumn(name = "to_external")
  var toExternal: ExternalAccountEntity? = null,

  @Column(name = "from_email", nullable = false)
  val fromEmail: String,

  @Column(name = "to_email", nullable = false)
  val toEmail: String,

  @ManyToOne
  @JoinColumn(name = "from_account_id", nullable = true)
  val fromAccount: AccountEntity? = null,

  @ManyToOne
  @JoinColumn(name = "to_account_id", nullable = true)
  val toAccount: AccountEntity? = null,

  @Column(name = "currency", nullable = false)
  @Enumerated(EnumType.STRING)
  val currency: Currencies,

  @Column(name = "amount", nullable = false)
  val amount: BigDecimal,

  @UuidGenerator
  @Column(name = "transaction_id", nullable = false)
  var transactionId: String,
) {
  @Column(name = "create_date", nullable = false)
  var createDate: Instant? = null

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long? = null

  @Column(name = "transaction_type")
  @Enumerated(EnumType.STRING)
  var transactionType: TransactionTypes? = null

  @Column(name = "description")
  var transactionDescription: String? = null

  @OneToOne()
  @JoinColumn(
    name = "external_transaction_ref_id",
    referencedColumnName = "transactionId",
    nullable = true
  )
  var externalReferencedTransaction: ExternalAccountTransactionsEntity? = null

  @PrePersist
  fun prePersist() {
    val now = Instant.now()
    createDate = now
  }

  override fun toString(): String {
    return "TransactionEntity(fromUser=$fromUser, toUser=$toUser, fromEmail='$fromEmail', toEmail='$toEmail', fromAccount=$fromAccount, toAccount=$toAccount, currency=$currency, amount=$amount, transactionId='$transactionId', createDate=$createDate, id=$id)"
  }
}

interface TransactionEntityRepository : JpaRepository<TransactionEntity, Long> {
  fun getTransactionByFromUser_Id(userId: Long): List<TransactionEntity>?
  fun getTransactionByToUser_Id(userId: Long): List<TransactionEntity>?
  fun getTransactionByFromAccount_Id(accountId: Long): List<TransactionEntity>?
  fun getTransactionByToAccount_Id(accountId: Long): List<TransactionEntity>?
}