package com.nompay.banking_universal.utils

import com.nompay.banking_universal.repositories.dto.external.CurrencyExchangeDto
import com.nompay.banking_universal.repositories.enums.Currencies

interface ExternalService {
  fun checkConversion(fromCurrency: Currencies, toCurrencies: List<Currencies>): CurrencyExchangeDto
}