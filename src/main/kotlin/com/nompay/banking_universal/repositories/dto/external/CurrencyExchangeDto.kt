package com.nompay.banking_universal.repositories.dto.external

import java.math.BigDecimal

data class CurrencyExchangeDto(
  val base: String? = null,

  val results: HashMap<String, BigDecimal>? = null,

  val updated: String? = null,

  val ms: String? = null
)