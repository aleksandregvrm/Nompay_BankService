package com.nompay.banking_universal.controllers

import com.nompay.banking_universal.annotations.graphAuth.RequiresAuthGraph
import com.nompay.banking_universal.repositories.dto.merchants.CreateMerchantDto
import com.nompay.banking_universal.repositories.entities.MerchantEntity
import com.nompay.banking_universal.repositories.enums.user.UserRoles
import com.nompay.banking_universal.services.MerchantService
import graphql.schema.DataFetchingEnvironment
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.stereotype.Controller

@Controller
class MerchantController(
  private val merchantServiceImpl: MerchantService
) {

  @MutationMapping(name = "createMerchant")
  @RequiresAuthGraph(roles = [UserRoles.FINANCIER]) // Permitted roles to access this route
  fun createMerchant(
    @Argument("userId") userId: Long,
    @Argument("input") input: CreateMerchantDto,
    environment: DataFetchingEnvironment
  ): MerchantEntity {
    println(input)
    println("logging the input of create merchant input")
    return this.merchantServiceImpl.createMerchant(input)
  }

  @MutationMapping(name = "addUserMerchant")
  @RequiresAuthGraph
  fun addUserMerchant(
    @Argument("userId") userId: Long,

    environment: DataFetchingEnvironment
  ) {

  }

}