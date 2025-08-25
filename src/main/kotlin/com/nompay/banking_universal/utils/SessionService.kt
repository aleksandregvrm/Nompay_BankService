package com.nompay.banking_universal.utils

import com.nompay.banking_universal.repositories.entities.UserEntity
import com.nompay.banking_universal.repositories.enums.user.UserRoles

interface SessionService {
  fun checkTokenValidity(token: String): Boolean
  fun generateToken(subject: String, validityHours: Int): String
  fun generateSession(user: UserEntity): Pair<String, String>
  fun checkUserRole(userData: HashMap<String, Any>): UserRoles
}