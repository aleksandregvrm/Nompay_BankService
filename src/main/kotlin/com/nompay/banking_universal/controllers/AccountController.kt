package com.nompay.banking_universal.controllers

import com.nompay.banking_universal.annotations.graphAuth.RequiresAuthGraph
import com.nompay.banking_universal.repositories.dto.account.CreateAccountDto
import com.nompay.banking_universal.repositories.dto.account.CreateMerchantAccountDto
import com.nompay.banking_universal.repositories.dto.account.TransferFundsDto
import com.nompay.banking_universal.repositories.entities.AccountEntity
import com.nompay.banking_universal.repositories.entities.TransactionEntity
import com.nompay.banking_universal.repositories.enums.user.UserRoles
import com.nompay.banking_universal.services.impl.AccountServiceImpl
import graphql.schema.DataFetchingEnvironment
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.stereotype.Controller

@Controller
class AccountController(
  private val accountService: AccountServiceImpl
) {

  @MutationMapping(name = "createAccount")
  @RequiresAuthGraph
  fun createAccount(
    @Argument("userId") userId: Long,
    @Argument("input") input: CreateAccountDto,
    environment: DataFetchingEnvironment
  ): AccountEntity {
    return this.accountService.createAccount(input);
  }

  @MutationMapping(name = "transferFunds")
  @RequiresAuthGraph
  fun transferFund(
    @Argument("userId") userId: Long,
    @Argument("input") input: TransferFundsDto,
    environment: DataFetchingEnvironment
  ): TransactionEntity {
    return this.accountService.transferFunds(input);
  }

  @MutationMapping(name = "addMerchantAccount")
  @RequiresAuthGraph(roles = [UserRoles.FINANCIER, UserRoles.ADMIN])
  fun createMerchantAccount(
    @Argument("userId") userId: Long,
    @Argument("input") input: CreateMerchantAccountDto,
    environment: DataFetchingEnvironment
  ): AccountEntity {
    return this.accountService.createMerchantAccount(input, userId)
  }
}