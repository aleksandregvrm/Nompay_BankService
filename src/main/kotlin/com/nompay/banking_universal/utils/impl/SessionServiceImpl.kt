package com.nompay.banking_universal.utils.impl

import com.nompay.banking_universal.utils.SessionService
import org.springframework.beans.factory.annotation.Value

class SessionServiceImpl(

) : SessionService {
  @Value("\${service.integratorHost}")
  lateinit var signingSecret: String;

  @Value("\${service.tokenValidity}")
  lateinit var tokenValidityHours: Integer;

  override fun checkTokenValidity(token: String): Boolean {
    TODO("Not yet implemented")
  }

  override fun generateToken(userData: HashMap<String, Any>): String {
    TODO("Not yet implemented")
  }
}