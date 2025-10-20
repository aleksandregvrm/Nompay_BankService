package com.nompay.banking_universal.utils

import com.nompay.banking_universal.repositories.entities.UserEntity

interface SessionService {
  fun checkTokenValidity(token: String, userId: Long): Boolean
  fun generateToken(userId: Long, validityHours: Int): String
  fun generateSession(user: UserEntity): Pair<String, String>

  fun checkExternalTokenValidity(token: String, integrationId: String): Boolean
  fun generateExternalToken(integrationId: String): String
}