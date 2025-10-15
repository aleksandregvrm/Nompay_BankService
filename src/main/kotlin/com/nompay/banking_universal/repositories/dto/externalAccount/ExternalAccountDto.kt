package com.nompay.banking_universal.repositories.dto.externalAccount

import java.sql.Date

open class ExternalAccountDto(
  open val email: String,
  open val name: String,
  open val surname: String,
  open val phone: String,
  open val bank: String,
  open val iban: String,
  open val dateOfBirth: Date,
  open val externalAccountBilling: ExternalAccountBilling,
)

data class ExternalAccountBilling(
  val city: String,
  val country: String,
  val state: String,
  val postalCode: String,
  val ipAddress: String,
  val customerId: String
)



