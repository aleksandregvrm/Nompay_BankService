package com.nompay.banking_universal.repositories.dto.externalAccount

import com.fasterxml.jackson.annotation.JsonProperty
import com.nompay.banking_universal.repositories.enums.other.Currencies
import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.Length
import java.math.BigDecimal
import java.sql.Date

data class ExternalCreateTransactionDto(
  @field:NotBlank(message = "Please specify the email")
  @field:Length(min = 3, max = 30)
  @get:JsonProperty("email") override val email: String? = null,

  @field:NotBlank(message = "Please specify the name")
  @field:Length(min = 3, max = 30)
  @get:JsonProperty("name") override val name: String? = null,

  @field:NotBlank(message = "Please specify the surname")
  @get:JsonProperty("surname") override val surname: String,

  @field:NotBlank(message = "Please specify the phone")
  @get:JsonProperty("phone") override val phone: String,

  @field:NotBlank(message = "Please specify the bank")
  @get:JsonProperty("bank") override val bank: String,

  @field:NotBlank(message = "Please specify the iban")
  @get:JsonProperty("iban") override val iban: String,

  @field:NotNull(message = "Please specify the date of birth")
  @get:JsonProperty("dateOfBirth") override val dateOfBirth: Date,

  @field:NotNull(message = "Please specify the External Account Billing")
  @get:JsonProperty("externalAccountBilling") override val externalAccountBilling: ExternalAccountBilling,

  @field:NotNull(message = "Please specify the amount to transfer")
  @field:DecimalMin(value = "0.01", message = "Minimal amount you can transfer is 0.01")
  @field:DecimalMax(value = "1000", message = "Maximum amount you can transfer is 1000")
  @get:JsonProperty("amount") val amount: BigDecimal, // Validating the field with validators

  @field:NotNull(message = "Please specify the currency")
  @get:JsonProperty("currency") override val currency: Currencies,

  @field:NotBlank(message = "Please specify the iban you are transferring the funds to")
  @get:JsonProperty("toIban") val toIban: String, // To iban is preferred method of transferring funds externally

  @field:NotBlank(message = "Please specify the email")
  @get:JsonProperty("toEmail") val toEmail: String,

  @field:NotNull(message = "Please specify the Merchant Transfer Type")
  @get:JsonProperty("merchantTransfer") val merchantTransfer: Boolean, // Whether transfer is to a merchant account or not

  @field:NotBlank(message = "Please specify the external transaction id")
  @get:JsonProperty("externalTransactionId") val externalTransactionId: String,

  @field:NotBlank(message = "Please specify the notification url")
  @get:JsonProperty("notificationUrl") val notificationUrl: String,

  ) : ExternalAccountDto(
  email,
  name,
  surname,
  phone,
  bank,
  iban,
  dateOfBirth,
  externalAccountBilling,
  currency
)