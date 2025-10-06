package com.nompay.banking_universal.services

import com.nompay.banking_universal.repositories.dto.account.CreateAccountDto
import com.nompay.banking_universal.repositories.dto.account.FindAccountsToTransferDto
import com.nompay.banking_universal.repositories.dto.account.TransferFundsDto
import com.nompay.banking_universal.repositories.dto.account.TransferFundsInternallyDto
import com.nompay.banking_universal.repositories.entities.AccountEntity

interface AccountService {
  fun createAccount(createAccountDto: CreateAccountDto): AccountEntity
  fun findAccountsToTransfer(findAccountsToTransferDto: FindAccountsToTransferDto): List<AccountEntity>
  fun transferFunds(transferFundsDto: TransferFundsDto): String
  fun transferFundsInternally(transferFundInternallyDto: TransferFundsInternallyDto): String
  fun transferFundsInternallyWithDiffCurrency(): String
}