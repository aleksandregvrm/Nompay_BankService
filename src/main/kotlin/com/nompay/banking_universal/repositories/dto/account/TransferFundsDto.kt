package com.nompay.banking_universal.repositories.dto.account

import com.nompay.banking_universal.repositories.entities.ExternalAccountEntity
import com.nompay.banking_universal.repositories.enums.other.Currencies
import com.nompay.banking_universal.repositories.enums.transactions.TransactionTypes
import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

data class TransferFundsDto(
  @field:NotBlank(message = "Please specify the amount to transfer")
  @field:DecimalMin(value = "0.01", message = "Minimal amount you can transfer is 0.01")
  @field:DecimalMax(value = "1000", message = "Maximum amount you can transfer is 1000")
  val amount: BigDecimal,

  @field:NotNull(message = "Please provide the currency")
  val currency: Currencies,

  val fromEmail: String? = null,

  val toEmail: String? = null,

  val fromMerchant: String? = null,

  val toMerchant: String? = null,

  val fromExternal: ExternalAccountEntity? = null,

  val toExternal: ExternalAccountEntity? = null,

  val fromAccountNumber: String? = null, // Meaning IBAN

  val toAccountNumber: String? = null,

  var transactionType: TransactionTypes, // <- Type of Transaction made, you can check out the enum for more info

  val transferDescription: String? = null // Description of the transfer made

)
