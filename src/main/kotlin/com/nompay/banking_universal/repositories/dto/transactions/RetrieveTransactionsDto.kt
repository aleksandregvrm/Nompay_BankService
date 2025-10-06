package com.nompay.banking_universal.repositories.dto.transactions

import com.nompay.banking_universal.repositories.enums.Currencies
import java.math.BigDecimal
import java.util.Date

data class RetrieveTransactionsDto(
  val userId: Long,

  val currency: Currencies?,

  val amountMin: BigDecimal?,

  val amountMax: BigDecimal?,

  val fromDate: Date?,

  val toDate: Date?
)
