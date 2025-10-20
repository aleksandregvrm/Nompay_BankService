package com.nompay.banking_universal.controllers

import com.nompay.banking_universal.annotations.graphAuth.RequiresAuthGraph
import com.nompay.banking_universal.repositories.dto.merchants.CreateMerchantDto
import com.nompay.banking_universal.repositories.entities.MerchantEntity
import com.nompay.banking_universal.services.MerchantService
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.stereotype.Controller

@Controller
class MerchantController(
  private val merchantServiceImpl: MerchantService
) {

  @MutationMapping(name = "createMerchant")
  @RequiresAuthGraph
  fun createMerchant(@Argument("input") input: CreateMerchantDto): MerchantEntity {
    return this.merchantServiceImpl.createMerchant(input)
  }

  @MutationMapping(name = "addUserMerchant")
  @RequiresAuthGraph
  fun addUserMerchant() {

  }

}