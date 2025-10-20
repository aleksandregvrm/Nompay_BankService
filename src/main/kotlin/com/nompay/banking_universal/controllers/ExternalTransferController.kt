package com.nompay.banking_universal.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.nompay.banking_universal.annotations.restAuth.RequiresAuthRest
import com.nompay.banking_universal.repositories.dto.account.TransferFundsDto
import com.nompay.banking_universal.repositories.dto.externalAccount.ExternalAccountBilling
import com.nompay.banking_universal.repositories.dto.externalAccount.ExternalAccountDto
import com.nompay.banking_universal.repositories.dto.externalAccount.ExternalCreateTransactionDto
import com.nompay.banking_universal.repositories.entities.ExternalAccountEntityRepository
import com.nompay.banking_universal.repositories.enums.transactions.TransactionStatuses
import com.nompay.banking_universal.repositories.enums.transactions.TransactionTypes
import com.nompay.banking_universal.services.impl.AccountServiceImpl
import com.nompay.banking_universal.services.impl.ExternalAccountServiceImpl
import com.nompay.banking_universal.services.impl.TransactionServiceImpl
import jakarta.validation.Valid
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import java.lang.IllegalStateException

@RestController
@RequestMapping("/api/v1/external/transfer")
class ExternalTransferController(
  private val externalAccountEntity: ExternalAccountEntityRepository,

  private val transactionService: TransactionServiceImpl,

  private val externalAccountService: ExternalAccountServiceImpl,

  private val accountServiceImpl: AccountServiceImpl,

  private val logger: Logger = LoggerFactory.getLogger(ExternalTransferController::class.java)
) {

  // Formatting data for logging purposes
  private val objectMapper = ObjectMapper().apply {
    enable(SerializationFeature.INDENT_OUTPUT)
  }

  private fun assembleFailedTransactionLog(externalCreateTransactionDto: ExternalCreateTransactionDto) {
    val hashmapToLog: HashMap<String, String> = hashMapOf(
      "externalTransactionId" to externalCreateTransactionDto.externalTransactionId,
      "status" to TransactionStatuses.FAILED.toString(),
      "toEmail" to externalCreateTransactionDto.toEmail,
      "fromEmail" to externalCreateTransactionDto.email!!,
      "currency" to externalCreateTransactionDto.currency.toString(),
      "bank" to externalCreateTransactionDto.bank,
      "iban" to externalCreateTransactionDto.iban,
      "toIban" to externalCreateTransactionDto.toIban,
      "merchantTransfer" to externalCreateTransactionDto.merchantTransfer.toString()
    )

    val jsonString = this.objectMapper.writeValueAsString(hashmapToLog);
    this.logger.info("Transaction has failed for = \n{}", jsonString)
  }

  @PostMapping
  @RequiresAuthRest // Custom Rest Auth annotation, That Authorizes the route for the user
  fun createExternalTransfer(
    @Valid @RequestBody request: ExternalCreateTransactionDto,

    @RequestHeader("Authorization") authorizationHeader: String,
  ) {
    print(request.toString())
    val restTemplate = RestTemplate()
    val existingExternalAccountCheck = this.externalAccountService.checkForExternalAccount(request.iban)
    println(existingExternalAccountCheck)

    val externalAccountEntity = if (existingExternalAccountCheck == null) {
      val externalAccountBilling = ExternalAccountBilling(
        city = request.externalAccountBilling.city,
        country = request.externalAccountBilling.country,
        state = request.externalAccountBilling.state,
        postalCode = request.externalAccountBilling.city,
        ipAddress = request.externalAccountBilling.ipAddress,
        customerId = request.externalAccountBilling.customerId
      )

      val externalAccount = ExternalAccountDto(
        email = request.email,
        name = request.name,
        surname = request.surname,
        phone = request.phone,
        bank = request.bank,
        iban = request.iban,
        dateOfBirth = request.dateOfBirth,
        externalAccountBilling = externalAccountBilling,
        currency = request.currency
      )

      this.externalAccountService.createExternalAccount(externalAccount)
    } else {
      existingExternalAccountCheck
    }

    val transactionType =
      if (request.merchantTransfer) TransactionTypes.EXTERNALTOMERCHANT else TransactionTypes.EXTERNALTOUSER

    val transferFundsDto = TransferFundsDto(
      amount = request.amount,
      currency = request.currency,
      fromExternal = externalAccountEntity,
      toAccountNumber = request.toIban,
      transactionType = transactionType,
      fromEmail = request.email,
      toEmail = request.toEmail,
    )

    try {
      val transactionEntity = this.accountServiceImpl.transferFunds(transferFundsDto)
      val externalAccountTransactionEntity =
        this.externalAccountService.createExternalAccountTransaction(request, existingExternalAccountCheck!!, transactionEntity)

    } catch (e: IllegalStateException) {
      this.logger.info("Transaction Failed with cause {}", e.message)
      this.assembleFailedTransactionLog(request)
      this.transactionService.handleFailedTransaction(transferFundsDto)
    }

  }

}