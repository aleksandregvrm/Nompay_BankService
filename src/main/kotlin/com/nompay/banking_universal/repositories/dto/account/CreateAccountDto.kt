package com.nompay.banking_universal.repositories.dto.account

import com.nompay.banking_universal.repositories.enums.other.Currencies
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class CreateAccountDto(
  @field:NotBlank(message = "Email is required")
  @field:Email(message = "Email must be a valid email address")
  val email: String,

  @field:NotBlank(message = "Name is required")
  val name: String,

  @field:NotNull("Please provide the currency")
  val currency: Currencies
  )