package com.nompay.banking_universal.services.impl

import com.nompay.banking_universal.repositories.dto.transactions.RetrieveTransactionsDto
import com.nompay.banking_universal.services.TransactionService
import com.nompay.banking_universal.repositories.dto.transactions.CreateTransactionDto
import com.nompay.banking_universal.repositories.dto.transactions.ReTransferFundsDto
import com.nompay.banking_universal.repositories.entities.TransactionEntity

class TransactionServiceImpl(

) : TransactionService {
  override fun createTransaction(createTransactionDto: CreateTransactionDto) {
    TODO("Not yet implemented")
  }

  override fun reTransferFunds(reTransferFundsDto: ReTransferFundsDto): String {
    TODO("Not yet implemented")
  }


  override fun retrieveTransactions(retrieveTransactionsDto: RetrieveTransactionsDto): List<TransactionEntity> {
    TODO("Not yet implemented")
  }
}