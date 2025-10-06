package com.nompay.banking_universal.services

import com.nompay.banking_universal.repositories.dto.transactions.RetrieveTransactionsDto

interface TransactionService {
  fun createTransaction(createTransactionDto: CreateTransactionDto)
  fun retrieveTransactions(retrieveTransactionsDto: RetrieveTransactionsDto): List<TransactionEntity>
  fun reTransferFunds(reTransferFundsDto: ReTransferFundsDto): String
}