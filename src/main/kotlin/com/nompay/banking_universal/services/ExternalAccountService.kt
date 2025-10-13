package com.nompay.banking_universal.services

import com.nompay.banking_universal.repositories.dto.externalAccount.ExternalAccountDto
import com.nompay.banking_universal.repositories.entities.ExternalAccountEntity

interface ExternalAccountService {
  fun checkForExternalAccount(externalAccountInfo: ExternalAccountDto): ExternalAccountEntity
  fun createExternalAccount(externalAccountInfo: ExternalAccountDto): String
}