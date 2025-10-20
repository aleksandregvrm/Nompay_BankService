package com.nompay.banking_universal.services

import com.nompay.banking_universal.repositories.dto.account.TransferFundsDto
import com.nompay.banking_universal.repositories.dto.notification.ExternalNotificationDto
import com.nompay.banking_universal.repositories.dto.transactions.CreateTransactionDto
import com.nompay.banking_universal.repositories.dto.transactions.ReTransferFundsDto
import com.nompay.banking_universal.repositories.dto.transactions.RetrieveTransactionsDto
import com.nompay.banking_universal.repositories.entities.AccountEntity
import com.nompay.banking_universal.repositories.entities.MerchantEntity
import com.nompay.banking_universal.repositories.entities.TransactionEntity
import com.nompay.banking_universal.repositories.enums.transactions.TransactionTypes

interface TransactionService {
  fun createTransaction(createTransactionDto: CreateTransactionDto): TransactionEntity
  fun retrieveAllUserTransactions(retrieveTransactionsDto: RetrieveTransactionsDto): List<TransactionEntity>
  fun retryTransaction(reTransferFundsDto: ReTransferFundsDto): String
  fun handleFailedTransaction(transferFundsDto: TransferFundsDto): ExternalNotificationDto
  fun constructTransactionPerTransactionType(
    transactionType: TransactionTypes,
    fromAccount: AccountEntity?,
    toAccount: AccountEntity?,
    toMerchant: MerchantEntity?,
    fromMerchant: MerchantEntity?,
    transferFundsDto: TransferFundsDto,
  ): CreateTransactionDto // Used for differentiation between different Transaction Types, Constructing Transaction Data Class
  // according to that
}