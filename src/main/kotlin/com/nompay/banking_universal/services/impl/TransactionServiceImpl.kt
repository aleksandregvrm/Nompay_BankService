package com.nompay.banking_universal.services.impl

import com.nompay.banking_universal.repositories.dto.account.TransferFundsDto
import com.nompay.banking_universal.repositories.dto.notification.ExternalNotificationDto
import com.nompay.banking_universal.repositories.dto.transactions.RetrieveTransactionsDto
import com.nompay.banking_universal.services.TransactionService
import com.nompay.banking_universal.repositories.dto.transactions.CreateTransactionDto
import com.nompay.banking_universal.repositories.dto.transactions.ReTransferFundsDto
import com.nompay.banking_universal.repositories.entities.AccountEntity
import com.nompay.banking_universal.repositories.entities.TransactionEntity
import com.nompay.banking_universal.repositories.entities.TransactionEntityRepository
import com.nompay.banking_universal.repositories.enums.transactions.TransactionStatuses
import com.nompay.banking_universal.repositories.enums.transactions.TransactionTypes
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class TransactionServiceImpl(
  private val transactionEntityRepository: TransactionEntityRepository,

  private val logger: Logger = LoggerFactory.getLogger(TransactionServiceImpl::class.java),

  ) : TransactionService {
  override fun createTransaction(createTransactionDto: CreateTransactionDto): TransactionEntity {

    val transactionEntity = TransactionEntity(
      fromUser = createTransactionDto.fromUser,
      toUser = createTransactionDto.toUser,
      fromEmail = createTransactionDto.toEmail,
      toEmail = createTransactionDto.toEmail,
      fromMerchant = createTransactionDto.fromMerchant,
      toMerchant = createTransactionDto.toMerchant,
      fromExternal = createTransactionDto.fromExternal,
      toExternal = createTransactionDto.toExternal,
      fromAccount = createTransactionDto.fromAccount,
      toAccount = createTransactionDto.toAccount,
      currency = createTransactionDto.currency,
      amount = createTransactionDto.amount,
      transactionId = createTransactionDto.transactionId
    ).apply {
      this.transactionType =
        createTransactionDto.transactionType // Assigning the transaction Type of the payment transaction
      this.transactionDescription = createTransactionDto.description
    }

    this.transactionEntityRepository.save<TransactionEntity>(transactionEntity)

    this.logger.info(
      "Transaction processed successfully - {}, - from:{}, - to:{}",
      transactionEntity.transactionId,
      transactionEntity.fromEmail,
      transactionEntity.toEmail
    )
    return transactionEntity
  }

  override fun retryTransaction(reTransferFundsDto: ReTransferFundsDto): String {
    TODO("Not yet implemented")
  }


  override fun retrieveAllUserTransactions(retrieveTransactionsDto: RetrieveTransactionsDto): List<TransactionEntity> {
    TODO("Not yet implemented")
  }

  override fun handleFailedTransaction(transferFundsDto: TransferFundsDto): ExternalNotificationDto {
    TODO("Not yet implemented")
  }

  // Function that constructs relevant transaction Dto to create a transaction Entity
  override fun constructTransactionPerTransactionType(
    transactionType: TransactionTypes,
    fromAccount: AccountEntity?,
    toAccount: AccountEntity?,
    transferFundsDto: TransferFundsDto,
  ): CreateTransactionDto {
    return when (transactionType) {
      TransactionTypes.USERTOUSER -> {
        return CreateTransactionDto.Builder()
          .withFromUser(fromAccount?.ownerUser)
          .withToUser(toAccount?.ownerUser)
          .withFromEmail(fromAccount?.email!!)
          .withToEmail(toAccount?.email!!)
          .withFromAccount(fromAccount)
          .withToAccount(toAccount)
          .withTransactionType(transactionType)
          .withTransactionId(UUID.randomUUID().toString())
          .withCurrency(transferFundsDto.currency)
          .withAmount(transferFundsDto.amount)
          .withStatus(TransactionStatuses.SUCCESS)
          .withDescription(transferFundsDto.transferDescription)
          .build()
      }

      TransactionTypes.EXTERNALTOUSER -> {
        return CreateTransactionDto.Builder()
          .withToUser(toAccount?.ownerUser)
          .withFromEmail(transferFundsDto.fromExternal?.email!!)
          .withToEmail(toAccount?.email!!)
          .withFromExternal(transferFundsDto.fromExternal)
          .withToAccount(toAccount)
          .withTransactionType(transactionType)
          .withTransactionId(UUID.randomUUID().toString())
          .withCurrency(transferFundsDto.currency)
          .withAmount(transferFundsDto.amount)
          .withStatus(TransactionStatuses.SUCCESS)
          .withDescription(transferFundsDto.transferDescription)
          .build()
      }

      else -> throw IllegalArgumentException("Incorrect transaction type provided")
    }
  }

}