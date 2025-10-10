package com.nompay.banking_universal.repositories.entities

import com.nompay.banking_universal.repositories.enums.Currencies
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
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
  @JoinColumn(name = "from_user_id", nullable = false)
  val fromUserId: UserEntity,

  @ManyToOne
  @JoinColumn(name = "to_user_id", nullable = false)
  val toUserId: UserEntity,

  @Column(name = "from_email", nullable = false)
  val fromEmail: String,

  @Column(name = "to_email", nullable = false)
  val toEmail: String,

  @ManyToOne
  @JoinColumn(name = "from_account_id", nullable = false)
  val fromAccountId: AccountEntity,

  @ManyToOne
  @JoinColumn(name = "to_account_id", nullable = false)
  val toAccountId: AccountEntity,

  @Column(name = "currency", nullable = false)
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

  @PrePersist
  fun prePersist() {
    val now = Instant.now()
    createDate = now
  }
}

interface TransactionEntityRepository : JpaRepository<TransactionEntity, Long> {
  fun getTransactionByFromUserId(fromUserId: Long): List<TransactionEntity>?
  fun getTransactionByToUserId(toUserId: Long): List<TransactionEntity>?
  fun getTransactionByFromAccountId(fromAccountId: Long): List<TransactionEntity>?
  fun getTransactionByToAccountId(toAccountId: Long): List<TransactionEntity>?
}