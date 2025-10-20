package com.nompay.banking_universal.controllers

import com.nompay.banking_universal.annotations.graphAuth.RequiresAuth
import com.nompay.banking_universal.repositories.dto.account.CreateAccountDto
import com.nompay.banking_universal.repositories.dto.account.TransferFundsDto
import com.nompay.banking_universal.repositories.entities.AccountEntity
import com.nompay.banking_universal.repositories.entities.TransactionEntity
import com.nompay.banking_universal.services.impl.AccountServiceImpl
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.stereotype.Controller

@Controller
class AccountController(
  private val accountService: AccountServiceImpl
) {

  @MutationMapping(name = "createAccount")
  @RequiresAuth
  fun createAccount(
    @Argument("input") input: CreateAccountDto,
  ): AccountEntity {
    return this.accountService.createAccount(input);
  }

  @MutationMapping(name = "transferFunds")
  @RequiresAuth
  fun transferFund(
    @Argument("input") input: TransferFundsDto,
  ): TransactionEntity {
    return this.accountService.transferFunds(input);
  }
}