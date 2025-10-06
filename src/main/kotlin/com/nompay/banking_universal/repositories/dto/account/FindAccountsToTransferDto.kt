package com.nompay.banking_universal.repositories.dto.account

data class FindAccountsToTransferDto(
  val accountNumber: String?,

  val email: String?,

  val currency: String?
)
