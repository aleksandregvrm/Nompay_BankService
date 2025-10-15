package com.nompay.banking_universal.services.impl

import com.nompay.banking_universal.repositories.dto.externalAccount.ExternalAccountDto
import com.nompay.banking_universal.repositories.dto.externalAccount.ExternalCreateTransactionDto
import com.nompay.banking_universal.repositories.entities.ExternalAccountEntity
import com.nompay.banking_universal.repositories.entities.ExternalAccountEntityRepository
import com.nompay.banking_universal.repositories.entities.ExternalAccountTransactionEntityRepository
import com.nompay.banking_universal.repositories.entities.ExternalAccountTransactionsEntity
import com.nompay.banking_universal.services.ExternalAccountService
import com.nompay.banking_universal.utils.impl.IBANServiceImpl
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ExternalAccountServiceImpl(
  private val externalAccountEntity: ExternalAccountEntityRepository,

  private val externalAccountTransactionEntity: ExternalAccountTransactionEntityRepository,

  private val ibanServiceImpl: IBANServiceImpl
) : ExternalAccountService {

  override fun checkForExternalAccount(iban: String): ExternalAccountEntity? {
    TODO("Not yet implemented")
  }

  override fun createExternalAccount(externalAccountInfo: ExternalAccountDto): ExternalAccountEntity {
    TODO("Not yet implemented")
  }

  /*
  * Creating a row in the @Entity ExternalAccountTransaction Table @see - com.nompay.banking_universal.repositories.entities
  * */
  override fun createExternalAccountTransaction(externalAccountTransactionDto: ExternalCreateTransactionDto): ExternalAccountTransactionsEntity {
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
      transactionId = UUID.randomUUID().toString(),
      toEmail = externalAccountTransactionDto.toEmail,
      merchantTransfer = externalAccountTransactionDto.merchantTransfer,
      externalTransactionId = externalAccountTransactionDto.externalTransactionId,
      notificationUrl = externalAccountTransactionDto.notificationUrl
    )
    return this.externalAccountTransactionEntity.save<ExternalAccountTransactionsEntity>(externalAccountTransaction)
  }

  /*

  * */
  override fun handleFailedExternalAccountTransaction(transactionId: String): ExternalAccountTransactionsEntity {
    TODO("Not yet implemented")
  }
}