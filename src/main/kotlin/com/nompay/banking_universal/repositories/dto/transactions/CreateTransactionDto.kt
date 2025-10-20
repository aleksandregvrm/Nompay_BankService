package com.nompay.banking_universal.repositories.dto.transactions

import com.nompay.banking_universal.repositories.entities.AccountEntity
import com.nompay.banking_universal.repositories.entities.ExternalAccountEntity
import com.nompay.banking_universal.repositories.entities.MerchantEntity
import com.nompay.banking_universal.repositories.entities.UserEntity
import com.nompay.banking_universal.repositories.enums.other.Currencies
import com.nompay.banking_universal.repositories.enums.transactions.TransactionStatuses
import com.nompay.banking_universal.repositories.enums.transactions.TransactionTypes
import org.hibernate.Transaction
import java.math.BigDecimal

data class CreateTransactionDto(
  val fromUser: UserEntity?,
  val toUser: UserEntity?,
  val fromEmail: String,
  val toEmail: String,
  val fromMerchant: MerchantEntity?,
  val toMerchant: MerchantEntity?,
  val fromExternal: ExternalAccountEntity?,
  val toExternal: ExternalAccountEntity?,
  val fromAccount: AccountEntity?,
  val toAccount: AccountEntity?,
  val transactionType: TransactionTypes,
  val transactionId: String,
  val currency: Currencies,
  val amount: BigDecimal,
  val status: TransactionStatuses,
  val description: String?
) {
  class Builder {
    private var fromUser: UserEntity? = null
    private var toUser: UserEntity? = null
    private var fromEmail: String? = null
    private var toEmail: String? = null
    private var fromMerchant: MerchantEntity? = null
    private var toMerchant: MerchantEntity? = null
    private var fromExternal: ExternalAccountEntity? = null
    private var toExternal: ExternalAccountEntity? = null
    private var fromAccount: AccountEntity? = null
    private var toAccount: AccountEntity? = null
    private var transactionType: TransactionTypes? = null
    private var transactionId: String? = null
    private var currency: Currencies? = null
    private var amount: BigDecimal? = null
    private var status: TransactionStatuses? = TransactionStatuses.PENDING // Example default status
    private var description: String? = null

    // Setter methods (withers) for fluent chaining
    fun withFromUser(fromUser: UserEntity?) = apply { this.fromUser = fromUser }
    fun withToUser(toUser: UserEntity?) = apply { this.toUser = toUser }
    fun withFromEmail(fromEmail: String) = apply { this.fromEmail = fromEmail }
    fun withToEmail(toEmail: String) = apply { this.toEmail = toEmail }
    fun withFromMerchant(fromMerchant: MerchantEntity?) = apply { this.fromMerchant = fromMerchant }
    fun withToMerchant(toMerchant: MerchantEntity?) = apply { this.toMerchant = toMerchant }
    fun withFromExternal(fromExternal: ExternalAccountEntity?) = apply { this.fromExternal = fromExternal }
    fun withToExternal(toExternal: ExternalAccountEntity?) = apply { this.toExternal = toExternal }
    fun withFromAccount(fromAccount: AccountEntity?) = apply { this.fromAccount = fromAccount }
    fun withToAccount(toAccount: AccountEntity?) = apply { this.toAccount = toAccount }
    fun withTransactionType(transactionType: TransactionTypes) = apply { this.transactionType = transactionType }
    fun withTransactionId(transactionId: String) = apply { this.transactionId = transactionId }
    fun withCurrency(currency: Currencies) = apply { this.currency = currency }
    fun withAmount(amount: BigDecimal) = apply { this.amount = amount }
    fun withStatus(status: TransactionStatuses) = apply { this.status = status }
    fun withDescription(description: String?) = apply { this.description = description }

    fun build(): CreateTransactionDto {
      return CreateTransactionDto(
        fromUser = fromUser,
        toUser = toUser,
        fromEmail = fromEmail!!,
        toEmail = toEmail!!,
        fromAccount = fromAccount,
        toAccount = toAccount,
        transactionId = transactionId!!,
        transactionType = transactionType!!,
        currency = currency!!,
        amount = amount!!,
        status = status!!,
        fromMerchant = fromMerchant,
        toMerchant = toMerchant,
        fromExternal = fromExternal,
        toExternal = toExternal,
        description = description
      )
    }
  }
}
