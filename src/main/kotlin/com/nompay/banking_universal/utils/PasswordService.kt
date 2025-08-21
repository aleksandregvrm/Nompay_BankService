package com.nompay.banking_universal.utils

interface PasswordService {
  fun hashPassword(password: String): String
  fun verifyPassword(candidate: String, hashed: String): Boolean
}