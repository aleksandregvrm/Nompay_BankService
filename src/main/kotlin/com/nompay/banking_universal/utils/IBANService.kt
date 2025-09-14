package com.nompay.banking_universal.utils

import com.nompay.banking_universal.repositories.entities.AccountEntity
import com.nompay.banking_universal.repositories.enums.Currencies

interface IBANService {
  fun generateAccountIBAN(): String
  fun retrieveTransferBankAccountsWithIban(
    fromIban: String,
    toIban: String
  ): Pair<AccountEntity, AccountEntity>

  fun retrieveTransferBankAccountsWithEmail(
    fromEmail: String,
    toEmail: String,
    currency: Currencies
  ): Pair<AccountEntity, AccountEntity>
}