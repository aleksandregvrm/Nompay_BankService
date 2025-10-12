package com.nompay.banking_universal.repositories.enums.other

enum class Currencies(val code: String, val symbol: String) {
  USD("USD", "UNITED STATES DOLLAR"),
  EUR("EUR", "EURO"),
  GBP("GBP", "BRITISH POUND"),
  GEL("GEL", "GEORGIAN LARI");

  companion object {

    // Validating Whether incoming currency exists...
    fun checkForCurrency(candidateCurrency: String): Boolean {
      return entries.find { it.code.equals(candidateCurrency, ignoreCase = true) }?.code.toBoolean()
    }

  }
}