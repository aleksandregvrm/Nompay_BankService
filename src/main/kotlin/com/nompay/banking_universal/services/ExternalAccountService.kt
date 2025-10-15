package com.nompay.banking_universal.services

import com.nompay.banking_universal.repositories.dto.externalAccount.ExternalAccountDto
import com.nompay.banking_universal.repositories.entities.ExternalAccountEntity

interface ExternalAccountService {
  fun checkForExternalAccount(iban: String): ExternalAccountEntity?
  fun createExternalAccount(externalAccountInfo: ExternalAccountDto): ExternalAccountEntity
}