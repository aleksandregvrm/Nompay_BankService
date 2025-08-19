package com.nompay.banking_universal.controllers

import com.nompay.banking_universal.repositories.dto.account.CreateAccountDto
import com.nompay.banking_universal.repositories.entities.AccountEntity
import com.nompay.banking_universal.services.impl.AccountServiceImpl
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.stereotype.Controller

@Controller
class AccountController(
  private val accountService: AccountServiceImpl
) {

  @MutationMapping(name = "createAccount")
  fun createAccount(@Argument("input") input: CreateAccountDto): AccountEntity{
    throw RuntimeException("asdsd")
  }

}