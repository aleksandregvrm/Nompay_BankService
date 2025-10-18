package com.nompay.banking_universal.services.impl

import com.nompay.banking_universal.repositories.dto.externalAccount.ExternalAccountDto
import com.nompay.banking_universal.repositories.dto.externalAccount.ExternalCreateTransactionDto
import com.nompay.banking_universal.repositories.entities.ExternalAccountEntity
import com.nompay.banking_universal.repositories.entities.ExternalAccountEntityRepository
import com.nompay.banking_universal.repositories.entities.ExternalAccountTransactionEntityRepository
import com.nompay.banking_universal.repositories.entities.ExternalAccountTransactionsEntity
import com.nompay.banking_universal.repositories.entities.TransactionEntity
import com.nompay.banking_universal.services.ExternalAccountService
import com.nompay.banking_universal.utils.impl.IBANServiceImpl
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ExternalAccountServiceImpl(
  private val externalAccountEntity: ExternalAccountEntityRepository,

  private val externalAccountTransactionEntity: ExternalAccountTransactionEntityRepository,

  private val ibanServiceImpl: IBANServiceImpl
) : ExternalAccountService {

  override fun checkForExternalAccount(iban: String): ExternalAccountEntity? {
    val externalAccount = this.externalAccountEntity.getExternalAccountByIban(iban)
    if (externalAccount == null) {
      return null
    } else {
      return externalAccount
    }
  }

  override fun createExternalAccount(externalAccountInfo: ExternalAccountDto): ExternalAccountEntity {
    val externalAccountEntity = ExternalAccountEntity(
      email = externalAccountInfo.email,
      name = externalAccountInfo.name,
      surname = externalAccountInfo.surname,
      phone = externalAccountInfo.phone,
      iban = externalAccountInfo.iban,
      bank = externalAccountInfo.bank,
      dateOfBirth = externalAccountInfo.dateOfBirth,
      externalAccountBilling = externalAccountInfo.externalAccountBilling,
      currency = externalAccountInfo.currency
    )
    this.externalAccountEntity.save<ExternalAccountEntity>(externalAccountEntity)
    return externalAccountEntity
  }

  /*
  * Creating a row in the @Entity ExternalAccountTransaction Table @see - com.nompay.banking_universal.repositories.entities
  * */
  @Transactional
  override fun createExternalAccountTransaction(
    externalAccountTransactionDto: ExternalCreateTransactionDto,
    transactionEntity: TransactionEntity
  ): ExternalAccountTransactionsEntity {
    val externalAccountTransaction: ExternalAccountTransactionsEntity = ExternalAccountTransactionsEntity(
      email = externalAccountTransactionDto.email,
      name = externalAccountTransactionDto.name,
      surname = externalAccountTransactionDto.surname,
      phone = externalAccountTransactionDto.phone,
      iban = externalAccountTransactionDto.iban,
      dateOfBirth = externalAccountTransactionDto.dateOfBirth,
      externalAccountBillingTransaction = externalAccountTransactionDto.externalAccountBilling,
      amount = externalAccountTransactionDto.amount,
      currency = externalAccountTransactionDto.currency,
      toIban = externalAccountTransactionDto.toIban,
      bank = externalAccountTransactionDto.bank,
      toEmail = externalAccountTransactionDto.toEmail,
      merchantTransfer = externalAccountTransactionDto.merchantTransfer,
      externalTransactionId = externalAccountTransactionDto.externalTransactionId,
      notificationUrl = externalAccountTransactionDto.notificationUrl,
      referencedTransaction = transactionEntity,
      transactionId = transactionEntity.transactionId
    )
    externalAccountTransaction.referencedTransaction =
      return this.externalAccountTransactionEntity.save<ExternalAccountTransactionsEntity>(externalAccountTransaction)
  }

  /*

  * */
  override fun handleFailedExternalAccountTransaction(transactionId: String): ExternalAccountTransactionsEntity {
    TODO("Not yet implemented")
  }
}