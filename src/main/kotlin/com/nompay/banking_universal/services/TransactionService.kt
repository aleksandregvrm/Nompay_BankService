package com.nompay.banking_universal.services

import com.nompay.banking_universal.repositories.dto.transactions.CreateTransactionDto
import com.nompay.banking_universal.repositories.dto.transactions.ReTransferFundsDto
import com.nompay.banking_universal.repositories.dto.transactions.RetrieveTransactionsDto
import com.nompay.banking_universal.repositories.entities.TransactionEntity

interface TransactionService {
  fun createTransaction(createTransactionDto: CreateTransactionDto)
  fun retrieveAllUserTransactions(retrieveTransactionsDto: RetrieveTransactionsDto): List<TransactionEntity>
  fun retryTransaction(reTransferFundsDto: ReTransferFundsDto): String
}