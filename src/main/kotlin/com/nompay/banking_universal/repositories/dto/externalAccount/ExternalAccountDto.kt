package com.nompay.banking_universal.repositories.dto.externalAccount

import com.nompay.banking_universal.repositories.enums.other.Currencies
import java.sql.Date

open class ExternalAccountDto(
  open val email: String? = null,
  open val name: String? = null,
  open val surname: String,
  open val phone: String,
  open val bank: String,
  open val iban: String,
  open val dateOfBirth: Date,
  open val externalAccountBilling: ExternalAccountBilling,
  open val currency: Currencies
) {
  override fun toString(): String {
    return "ExternalAccountDto(email=$email, name=$name, surname='$surname', phone='$phone', bank='$bank', iban='$iban', dateOfBirth=$dateOfBirth, externalAccountBilling=$externalAccountBilling, currency=$currency)"
  }
}

data class ExternalAccountBilling(
  val city: String,
  val country: String,
  val state: String,
  val postalCode: String,
  val ipAddress: String,
  val customerId: String
)



