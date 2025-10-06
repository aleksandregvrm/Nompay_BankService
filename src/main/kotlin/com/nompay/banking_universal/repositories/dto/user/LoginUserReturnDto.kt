package com.nompay.banking_universal.repositories.dto.user

import java.sql.Date

data class LoginUserReturnDto(
  val refreshToken: String,
  val accessToken: String,
  val username: String?,
  val email: String?,
  val timeStamp: Date
) {
  class Builder {w
    private var refreshToken: String? = null
    private var accessToken: String? = null
    private var username: String? = null
    private var email: String? = null
    private var timeStamp: Date? = null

    fun withRefreshToken(refreshToken: String) = apply { this.refreshToken = refreshToken }
    fun withAccessToken(accessToken: String) = apply { this.accessToken = accessToken }
    fun withUsername(username: String?) = apply { this.username = username }
    fun withEmail(email: String?) = apply { this.email = email }
    fun withTimeStamp(timeStamp: Date) = apply { this.timeStamp = timeStamp }

    fun build(): LoginUserReturnDto {
      return LoginUserReturnDto(
        refreshToken = refreshToken ?: throw IllegalStateException("Refresh token is a required field"),
        accessToken = accessToken ?: throw IllegalStateException("Access token is a required field"),
        username = username,
        email = email,
        timeStamp = timeStamp ?: throw IllegalStateException("Timestamp is a required field")
      )
    }
  }
}