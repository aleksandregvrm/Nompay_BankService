package com.nompay.banking_universal.services

import com.nompay.banking_universal.repositories.dto.externalAccount.ExternalAccountDto
import com.nompay.banking_universal.repositories.dto.externalAccount.ExternalCreateTransactionDto
import com.nompay.banking_universal.repositories.entities.ExternalAccountEntity
import com.nompay.banking_universal.repositories.entities.ExternalAccountTransactionsEntity
import com.nompay.banking_universal.repositories.entities.TransactionEntity

interface ExternalAccountService {
  fun checkForExternalAccount(iban: String): ExternalAccountEntity?
  fun createExternalAccount(externalAccountInfo: ExternalAccountDto): ExternalAccountEntity
  fun createExternalAccountTransaction(
    externalAccountTransactionDto: ExternalCreateTransactionDto,
    transactionEntity: TransactionEntity
  ): ExternalAccountTransactionsEntity

  fun handleFailedExternalAccountTransaction(transactionId: String): ExternalAccountTransactionsEntity
}