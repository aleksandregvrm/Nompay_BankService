package com.nompay.banking_universal.repositories.dto.externalAccount

import com.fasterxml.jackson.annotation.JsonProperty
import com.nompay.banking_universal.repositories.enums.other.Currencies
import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotBlank
import java.math.BigDecimal
import java.sql.Date

data class ExternalCreateTransactionDto(
  @get:JsonProperty("email") override val email: String,

  @get:JsonProperty("name") override val name: String,

  @get:JsonProperty("surname") override val surname: String,

  @get:JsonProperty("phone") override val phone: String,

  @get:JsonProperty("accountNumber") override val accountNumber: String,

  @get:JsonProperty("bank") override val bank: String,

  @get:JsonProperty("iban") override val iban: String,

  @get:JsonProperty("dateOfBirth") override val dateOfBirth: Date,

  @get:JsonProperty("externalAccountBilling") override val externalAccountBilling: ExternalAccountBilling,

  @field:NotBlank(message = "Please specify the amount to transfer")
  @field:DecimalMin(value = "0.01", message = "Minimal amount you can transfer is 0.01")
  @field:DecimalMax(value = "1000", message = "Maximum amount you can transfer is 1000")
  @get:JsonProperty("amount") val amount: BigDecimal, // Validating the field with validators

  @get:JsonProperty("currency") val currency: Currencies,

  @get:JsonProperty("toIban") val toIban: String, // To iban is preferred method of transferring funds externally

  @get:JsonProperty("toEmail") val toEmail: String,

  @get:JsonProperty("merchantTransfer") val merchantTransfer: Boolean, // Whether transfer is to a merchant account or not

  @get:JsonProperty("externalTransactionId") val externalTransactionId: String,

  @get:JsonProperty("notificationUrl") val notificationUrl: String,

  ) : ExternalAccountDto(
  email,
  name,
  surname,
  phone,
  accountNumber,
  bank,
  iban,
  dateOfBirth,
  externalAccountBilling,
)