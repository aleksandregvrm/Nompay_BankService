package com.nompay.banking_universal.controllers

import com.nompay.banking_universal.repositories.entities.ExternalAccountEntityRepository
import com.nompay.banking_universal.services.impl.ExternalAccountServiceImpl
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/external/transfer")
class ExternalTransferController(
  private val externalAccountEntity: ExternalAccountEntityRepository,

  private val externalAccountService: ExternalAccountServiceImpl
) {

  @PostMapping
  // Require Authorization with custom Annotation
  fun createExternalTransfer(
    @RequestBody request: HashMap<String, Any>,

    @RequestHeader("Authorization") authorizationHeader: String,
  ){

  }
}