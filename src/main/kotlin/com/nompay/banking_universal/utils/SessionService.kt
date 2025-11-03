package com.nompay.banking_universal.utils

import com.nompay.banking_universal.repositories.dto.user.RefreshSessionReturnDto
import com.nompay.banking_universal.repositories.entities.UserEntity
import com.nompay.banking_universal.repositories.enums.user.UserRoles

interface SessionService {
  fun checkTokenValidity(token: String, userId: Long, permittedRoles: Array<UserRoles>): Boolean
  fun generateToken(userId: Long, validityHours: Int, userRole: UserRoles): String
  fun generateSession(user: UserEntity): Pair<String, String>
  fun refreshSession(userId: Long): RefreshSessionReturnDto

  fun checkExternalTokenValidity(token: String, integrationId: String): Boolean
  fun generateExternalToken(integrationId: String): String
}