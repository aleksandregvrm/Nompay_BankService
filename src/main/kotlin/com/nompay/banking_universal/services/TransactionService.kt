package com.nompay.banking_universal.services

import com.nompay.banking_universal.repositories.dto.transactions.CreateTransactionDto
import com.nompay.banking_universal.repositories.dto.transactions.ReTransferFundsDto
import com.nompay.banking_universal.repositories.dto.transactions.RetrieveTransactionsDto
import com.nompay.banking_universal.repositories.entities.TransactionEntity

interface TransactionService {
  fun createTransaction(createTransactionDto: CreateTransactionDto)
  fun retrieveTransactions(retrieveTransactionsDto: RetrieveTransactionsDto): List<TransactionEntity>
  fun reTransferFunds(reTransferFundsDto: ReTransferFundsDto): String
}