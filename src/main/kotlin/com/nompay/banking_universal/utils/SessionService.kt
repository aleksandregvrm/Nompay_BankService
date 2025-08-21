package com.nompay.banking_universal.utils

interface SessionService {
  fun checkTokenValidity(token: String): Boolean
  fun generateToken(userData: HashMap<String, Any>): String
}