package com.nompay.banking_universal.services

import com.nompay.banking_universal.repositories.dto.account.TransferFundsDto
import com.nompay.banking_universal.repositories.dto.notification.ExternalNotificationDto
import com.nompay.banking_universal.repositories.dto.transactions.CreateTransactionDto
import com.nompay.banking_universal.repositories.dto.transactions.ReTransferFundsDto
import com.nompay.banking_universal.repositories.dto.transactions.RetrieveTransactionsDto
import com.nompay.banking_universal.repositories.entities.TransactionEntity

interface TransactionService {
  fun createTransaction(createTransactionDto: CreateTransactionDto): TransactionEntity
  fun retrieveAllUserTransactions(retrieveTransactionsDto: RetrieveTransactionsDto): List<TransactionEntity>
  fun retryTransaction(reTransferFundsDto: ReTransferFundsDto): String
  fun handleFailedTransaction(transferFundsDto: TransferFundsDto): ExternalNotificationDto
}