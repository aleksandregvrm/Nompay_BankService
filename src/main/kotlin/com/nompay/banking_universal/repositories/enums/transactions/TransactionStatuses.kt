package com.nompay.banking_universal.repositories.enums.transactions

enum class TransactionStatuses(val symbol: String, val status: String) {
  SUCCESS("SCS", "SUCCESS"),
  FAILED("FLD", "FAILED"),
  PENDING("PDG", "PENDING"),
  CREATED("CTD", "CREATED");

  companion object {
    fun checkForTransactionStatus(symbolToCheck: String): Boolean {
      return entries.find {
        it.symbol.equals(symbolToCheck, ignoreCase = true)
      } != null
    }
  }
}