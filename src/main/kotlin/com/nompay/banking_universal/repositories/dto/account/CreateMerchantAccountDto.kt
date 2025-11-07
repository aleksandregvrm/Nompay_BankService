package com.nompay.banking_universal.repositories.dto.account

import com.nompay.banking_universal.repositories.enums.accounts.AccountTypes
import com.nompay.banking_universal.repositories.enums.other.Currencies
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class CreateMerchantAccountDto(
  @field:NotBlank(message = "Please specify the merchant id")
  val merchantId: String? = null,

  @field:NotBlank(message = "Please specify the email")
  val email: String? = null,

  @field:NotBlank(message = "Please specify the name")
  val name: String? = null,

  @field:NotNull(message = "Please specify the currency")
  val currency: Currencies? = null,

  val accountType: AccountTypes = AccountTypes.MERCHANTACCOUNT,
)
