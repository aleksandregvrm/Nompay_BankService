package com.nompay.banking_universal.repositories.dto.externalAccount

import java.sql.Date

data class ExternalAccountDto(
  val email: String,

  val name: String,

  val surname: String,

  val phone: String,

  val accountNumber: String,

  val bank: String,

  val dateOfBirth: Date,

  val externalAccountBilling: ExternalAccountBilling,

  val customerId: String
)

data class ExternalAccountBilling(

  val city: String,

  val country: String,

  val state: String,

  val postalCode: String,

  val ipAddress: String,

)



