package com.nompay.banking_universal.controllers

import com.nompay.banking_universal.annotations.auth.RequiresAuth
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class TransactionController {

  @QueryMapping(name = "retrieveOneTransaction")
  @RequiresAuth
  fun retrieveOneTransaction(){

  }

  @QueryMapping(name = "retrieveAllUserTransactions")
  @RequiresAuth
  fun retrieveAllUserTransaction(){

  }

  @QueryMapping(name = "retryTransaction")
  @RequiresAuth
  fun retryTransaction(){

  }


}