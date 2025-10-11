package com.nompay.banking_universal.services.impl

import com.nompay.banking_universal.repositories.dto.transactions.RetrieveTransactionsDto
import com.nompay.banking_universal.services.TransactionService
import com.nompay.banking_universal.repositories.dto.transactions.CreateTransactionDto
import com.nompay.banking_universal.repositories.dto.transactions.ReTransferFundsDto
import com.nompay.banking_universal.repositories.entities.TransactionEntity
import com.nompay.banking_universal.repositories.entities.TransactionEntityRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service

@Service
class TransactionServiceImpl(
  private val transactionEntityRepository: TransactionEntityRepository,

  @Lazy private val logger: Logger = LoggerFactory.getLogger(TransactionServiceImpl::class.java),

  private val userService: UserServiceImpl,

  private val accountService: AccountServiceImpl,

  ) : TransactionService {
  override fun createTransaction(createTransactionDto: CreateTransactionDto) {

    val transactionEntity = TransactionEntity(
      fromUserId = createTransactionDto.fromUser,
      toUserId = createTransactionDto.toUser,
      fromEmail = createTransactionDto.toEmail,
      toEmail = createTransactionDto.toEmail,
      fromAccountId = createTransactionDto.fromAccount,
      toAccountId = createTransactionDto.toAccount,
      currency = createTransactionDto.currency,
      amount = createTransactionDto.amount,
      transactionId = createTransactionDto.transactionId
    )

    this.transactionEntityRepository.save<TransactionEntity>(transactionEntity)

    this.logger.info("Transaction processed successfully - {}, - {}", transactionEntity.transactionId ,transactionEntity.toString())
  }

  override fun reTransferFunds(reTransferFundsDto: ReTransferFundsDto): String {
    TODO("Not yet implemented")
  }


  override fun retrieveTransactions(retrieveTransactionsDto: RetrieveTransactionsDto): List<TransactionEntity> {
    TODO("Not yet implemented")
  }
}