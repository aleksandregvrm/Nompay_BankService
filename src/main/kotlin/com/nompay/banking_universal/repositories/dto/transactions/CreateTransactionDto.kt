package com.nompay.banking_universal.repositories.dto.transactions

import com.nompay.banking_universal.repositories.enums.Currencies
import com.nompay.banking_universal.repositories.enums.transactions.TransactionStatuses
import java.math.BigDecimal

data class CreateTransactionDto(
  val fromUserId: Long,
  val toUserId: Long,
  val fromEmail: Long,
  val toEmail: Long,
  val fromAccountId: Long,
  val toAccountId: Long,
  val transactionId: String,
  val currency: Currencies,
  val amount: BigDecimal,
  val status: TransactionStatuses
) {
  class Builder {
    private var fromUserId: Long? = null
    private var toUserId: Long? = null
    private var fromEmail: Long? = null
    private var toEmail: Long? = null
    private var fromAccountId: Long? = null
    private var toAccountId: Long? = null
    private var transactionId: String? = null
    private var currency: Currencies? = null
    private var amount: BigDecimal? = null
    private var status: TransactionStatuses? = TransactionStatuses.PENDING // Example default status

    // Setter methods (withers) for fluent chaining
    fun withFromUserId(fromUserId: Long) = apply { this.fromUserId = fromUserId }
    fun withToUserId(toUserId: Long) = apply { this.toUserId = toUserId }
    fun withFromEmail(fromEmail: Long) = apply { this.fromEmail = fromEmail }
    fun withToEmail(toEmail: Long) = apply { this.toEmail = toEmail }
    fun withFromAccountId(fromAccountId: Long) = apply { this.fromAccountId = fromAccountId }
    fun toAccountId(toAccountId: Long) = apply { this.toAccountId = toAccountId }
    fun withTransactionId(transactionId: String) = apply { this.transactionId = transactionId }
    fun withCurrency(currency: Currencies) = apply { this.currency = currency }
    fun withAmount(amount: BigDecimal) = apply { this.amount = amount }
    fun withStatus(status: TransactionStatuses) = apply { this.status = status }

    fun build(): CreateTransactionDto {
      return CreateTransactionDto(
        fromUserId = fromUserId!!,
        toUserId = toUserId!!,
        fromEmail = fromEmail!!,
        toEmail = toEmail!!,
        fromAccountId = fromAccountId!!,
        toAccountId = toAccountId!!,
        transactionId = transactionId!!,
        currency = currency!!,
        amount = amount!!,
        status = status!!
      )
    }
  }
}
