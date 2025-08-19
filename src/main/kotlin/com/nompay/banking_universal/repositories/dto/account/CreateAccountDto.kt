package com.nompay.banking_universal.repositories.dto.account

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class CreateAccountDto(
  @field:NotBlank(message = "Email is required")
  @field:Email(message = "Email must be a valid email address")
  val email: String,

  // TODO!!!
  )