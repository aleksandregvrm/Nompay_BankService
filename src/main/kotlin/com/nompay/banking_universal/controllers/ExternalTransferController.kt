package com.nompay.banking_universal.controllers

import com.nompay.banking_universal.repositories.dto.account.TransferFundsDto
import com.nompay.banking_universal.repositories.dto.externalAccount.ExternalAccountBilling
import com.nompay.banking_universal.repositories.dto.externalAccount.ExternalAccountDto
import com.nompay.banking_universal.repositories.dto.externalAccount.ExternalCreateTransactionDto
import com.nompay.banking_universal.repositories.entities.ExternalAccountEntityRepository
import com.nompay.banking_universal.repositories.enums.transactions.TransactionTypes
import com.nompay.banking_universal.services.impl.AccountServiceImpl
import com.nompay.banking_universal.services.impl.ExternalAccountServiceImpl
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

  private val externalAccountService: ExternalAccountServiceImpl,

  private val accountServiceImpl: AccountServiceImpl
) {

  @PostMapping
  // Require Authorization with custom Annotation
  fun createExternalTransfer(
    @RequestBody request: ExternalCreateTransactionDto,

    @RequestHeader("Authorization") authorizationHeader: String,
  ) {
    val restTemplate = RestTemplate() // Rest Template for providing webhooks to the external provider.
    val existingExternalAccountCheck = this.externalAccountService.checkForExternalAccount(request.iban)

    // If the First Time Transaction from external sources we create an entity for that customer.
    if (existingExternalAccountCheck == null) {
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
        externalAccountBilling = externalAccountBilling
      )

      val externalAccountEntity = this.externalAccountService.createExternalAccount(externalAccount)

      val transactionType = // Defining the Transaction type in here
        if (request.merchantTransfer) TransactionTypes.EXTERNALTOMERCHANT else TransactionTypes.EXTERNALTOUSER

      val transferFundsDto = TransferFundsDto(
        amount = request.amount,
        currency = request.currency,
        fromExternal = externalAccountEntity,
        toAccountNumber = request.toIban,
        transactionType = transactionType
      )

      try {
        this.accountServiceImpl.transferFunds(transferFundsDto)
        this.externalAccountService.createExternalAccountTransaction(request)
      } catch (e: IllegalStateException ){
        // Handle Failed transaction logic storing it in both internal transactions database as well as external Transactions database
      }

      // Defining the logic of storing the ExternalTransfer separately And Sending a webhook to the user

    }

  }

}