package com.nompay.banking_universal.controllers

import com.nompay.banking_universal.annotations.graphAuth.RequiresAuthGraph
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class TransactionController {

  @QueryMapping(name = "retrieveOneTransaction")
  @RequiresAuthGraph
  fun retrieveOneTransaction(){

  }

  @QueryMapping(name = "retrieveAllUserTransactions")
  @RequiresAuthGraph
  fun retrieveAllUserTransaction(){

  }

  @QueryMapping(name = "retryTransaction")
  @RequiresAuthGraph
  fun retryTransaction(){

  }


}