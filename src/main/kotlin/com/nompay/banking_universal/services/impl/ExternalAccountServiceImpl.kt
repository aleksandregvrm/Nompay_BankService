package com.nompay.banking_universal.services.impl

import com.nompay.banking_universal.repositories.dto.externalAccount.ExternalAccountDto
import com.nompay.banking_universal.repositories.entities.ExternalAccountEntity
import com.nompay.banking_universal.repositories.entities.ExternalAccountEntityRepository
import com.nompay.banking_universal.services.ExternalAccountService
import org.springframework.stereotype.Service

@Service
class ExternalAccountServiceImpl(
  private val externalAccountEntity: ExternalAccountEntityRepository
): ExternalAccountService {

  override fun checkForExternalAccount(externalAccountInfo: ExternalAccountDto): ExternalAccountEntity {
    TODO("Not yet implemented")
  }

  override fun createExternalAccount(externalAccountInfo: ExternalAccountDto): String {
    TODO("Not yet implemented")
  }
}