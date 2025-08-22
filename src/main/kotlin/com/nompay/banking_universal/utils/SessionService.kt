package com.nompay.banking_universal.utils

import com.nompay.banking_universal.repositories.enums.user.UserRoles

interface SessionService {
  fun checkTokenValidity(token: String): Boolean
  fun generateToken(userData: HashMap<String, Any>): String
  fun checkUserRole(userData: HashMap<String, Any>): UserRoles
}