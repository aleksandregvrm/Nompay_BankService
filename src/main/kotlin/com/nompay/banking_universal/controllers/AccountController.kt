package com.nompay.banking_universal.controllers

import com.nompay.banking_universal.repositories.dto.account.CreateAccountDto
import com.nompay.banking_universal.repositories.dto.account.TransferFundsDto
import com.nompay.banking_universal.repositories.entities.AccountEntity
import com.nompay.banking_universal.services.impl.AccountServiceImpl
import graphql.schema.DataFetchingEnvironment
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.stereotype.Controller
import org.springframework.util.MultiValueMap

@Controller
class AccountController(
  private val accountService: AccountServiceImpl
) {

  @MutationMapping(name = "createAccount")
  fun createAccount(
    @Argument("input") input: CreateAccountDto,
    environment: DataFetchingEnvironment
  ): AccountEntity {
    val headers: MultiValueMap<String, String> = environment.graphQlContext.get("headers")
      ?: throw IllegalStateException("HTTP headers not found in GraphQL context.")

    val authorization = headers.getFirst("Authorization");

    if (authorization.isNullOrBlank()) {
      throw IllegalArgumentException("Not Authorized to create account")
    }
    return this.accountService.createAccount(input);
  }

  @MutationMapping(name = "transferFunds")
  fun transferFund(
    @Argument("input") input: TransferFundsDto,
    environment: DataFetchingEnvironment
  ): String {
    val headers: MultiValueMap<String, String> = environment.graphQlContext.get("headers")
      ?: throw IllegalStateException("HTTP headers not found in GraphQL context.")

    val authorization = headers.getFirst("Authorization");

    if (authorization.isNullOrBlank()) {
      throw IllegalArgumentException("Not Authorized to create account")
    }

    return "returning the transfered funds in here..."
  }
}