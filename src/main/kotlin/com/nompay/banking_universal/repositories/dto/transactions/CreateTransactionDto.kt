package com.nompay.banking_universal.repositories.dto.transactions

import com.nompay.banking_universal.repositories.dto.external.CurrencyExchangeDto
import com.nompay.banking_universal.repositories.enums.Currencies
import com.nompay.banking_universal.repositories.enums.transactions.TransactionStatuses
import java.math.BigDecimal

data class CreateTransactionDto(
  val userId: Long,
  val fromUserId: Long,
  val toUserId: Long,
  val fromEmail: Long,
  val toEmail: Long,
  val transactionId: String,
  val currency: Currencies,
  val amount: BigDecimal,
  val status: TransactionStatuses
) {
  class Builder {
    private var userId: String? = null
    private var fromUserId: Long? = null
    private var toUserId: Long? = null
    private var fromEmail: String? = null
    private var toEmail: String? = null
    private var transactionId: String? = null
    private var currency: CurrencyExchangeDto? = null
    private var amount: BigDecimal? = null
    private var status: TransactionStatuses? = null
  }
}
