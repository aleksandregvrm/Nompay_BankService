package com.nompay.banking_universal.repositories.dto.user

// Class used for assembling the login credentials...
data class LoginUserDto(
  val username: String?,
  val email: String?,
  val password: String
)