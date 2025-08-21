package com.nompay.banking_universal.repositories.dto.user

import java.sql.Date

data class LoginUserReturnDto(
  val refreshToken: String,
  val accessToken: String,
  val username: String?,
  val email: String?,
  val timeStamp: Date
)