package com.nompay.banking_universal.utils.impl

import org.mindrot.jbcrypt.BCrypt
import org.springframework.stereotype.Component

@Component
class PasswordServiceImpl : PasswordService {

  override fun hashPassword(password: String): String {
    return BCrypt.hashpw(password, BCrypt.gensalt())
  }

  override fun verifyPassword(candidate: String, hashed: String): Boolean {
    return BCrypt.checkpw(candidate, hashed);
  }
}