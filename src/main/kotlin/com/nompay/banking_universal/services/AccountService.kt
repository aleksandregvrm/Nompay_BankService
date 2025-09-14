package com.nompay.banking_universal.services

import com.nompay.banking_universal.repositories.dto.account.CreateAccountDto
import com.nompay.banking_universal.repositories.dto.account.TransferFundsDto
import com.nompay.banking_universal.repositories.entities.AccountEntity

interface AccountService {
  fun createAccount(createAccountDto: CreateAccountDto): AccountEntity
  fun transferFunds(transferFundsDto: TransferFundsDto): String
}