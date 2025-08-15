package com.nompay.banking_universal.repositories.dto.user

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UpdateUserDto(
  @field:NotBlank(message = "Username is required when updating user")
  @field:Size(min = 5, max = 30, message = "Username must be between 5 and 30 characters long")
  val username: String,

  @field:NotBlank(message = "Name is required when updating user")
  @field:Size(min = 5, max = 30, message = "")
  val name: String,


)
