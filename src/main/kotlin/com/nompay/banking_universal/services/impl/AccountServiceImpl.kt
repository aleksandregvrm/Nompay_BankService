package com.nompay.banking_universal.services.impl

import com.nompay.banking_universal.repositories.dto.account.CreateAccountDto
import com.nompay.banking_universal.repositories.dto.account.TransferFundsDto
import com.nompay.banking_universal.repositories.entities.AccountEntity
import com.nompay.banking_universal.repositories.entities.AccountEntityRepository
import com.nompay.banking_universal.repositories.entities.UserEntityRepository
import com.nompay.banking_universal.services.AccountService
import org.springframework.stereotype.Service

@Service
class AccountServiceImpl(

  private val accountRepository: AccountEntityRepository,

  private val userRepository: UserEntityRepository
) : AccountService {

  override fun createAccount(createAccountDto: CreateAccountDto): AccountEntity {
    TODO("Not yet implemented")
  }

  override fun transferFunds(transferFundsDto: TransferFundsDto): String {
    TODO("Not yet implemented")
  }
}