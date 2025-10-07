package com.nompay.banking_universal.repositories.dto.transactions

import com.nompay.banking_universal.repositories.enums.Currencies
import java.math.BigDecimal

data class ReTransferFundsDto(
  val transactionId: String,

  val amount: BigDecimal,

  val currency: Currencies,

  val fromUserId: Long,

  val toUserId: Long
)
