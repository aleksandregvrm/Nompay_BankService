package com.nompay.banking_universal.utils.impl

import com.nompay.banking_universal.repositories.enums.user.UserRoles
import com.nompay.banking_universal.utils.SessionService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class SessionServiceImpl(

) : SessionService {

  @Value("\${service.tokenValidity}")
  lateinit var tokenValidityHours: Integer;

  override fun checkTokenValidity(token: String): Boolean {
    TODO("Not yet implemented")
  }

  override fun generateToken(userData: HashMap<String, Any>): String {
    TODO("Not yet implemented")
  }

  override fun checkUserRole(userData: HashMap<String, Any>): UserRoles {
    TODO("Not yet implemented")
  }
}