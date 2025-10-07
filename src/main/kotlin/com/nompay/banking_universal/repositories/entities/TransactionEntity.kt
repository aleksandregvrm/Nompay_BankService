package com.nompay.banking_universal.repositories.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.Table
import org.hibernate.annotations.UuidGenerator
import org.springframework.data.jpa.repository.JpaRepository

@Entity
@Table(
  name = "transactions", indexes = [
    Index(name = "transaction_id", columnList = "transaction_id")
  ]
)
class TransactionEntity(
  var fromUserId: Long,

  var toUserId: Long,

  @UuidGenerator
  @Column(name = "transaction_id", nullable = false)
  var transactionId: String,
) {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long? = null
}

interface TransactionEntityRepository : JpaRepository<TransactionEntity, Long> {

}