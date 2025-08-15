package com.nompay.banking_universal.repositories.dto.user

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CreateUserDto(
  @field:NotBlank(message = "Username is required")
  @field:Size(min = 5, max = 30, message = "Username must be between 5 and 30 characters long")
  val username: String,

  @field:NotBlank(message = "Email is required")
  @field:Email(message = "Email must be a valid email address")
  val email: String,

  @field:NotBlank(message = "Password is required")
  @field:Size(min = 8, message = "Password must be at least 8 characters long")
  val password: String,

  @field:NotBlank(message = "Name is required")
  val name: String,

  val birthDate: java.sql.Date
)
