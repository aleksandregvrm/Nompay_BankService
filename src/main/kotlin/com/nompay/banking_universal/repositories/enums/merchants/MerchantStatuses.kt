package com.nompay.banking_universal.repositories.enums.merchants


enum class MerchantStatuses(val symbol: String, val status: String) {
  CREATED("CREATED","CREATED"),
  ACTIVE("ACTIVE","ACTIVE"),
  DELETED("DELETED","DELETED"),
  SUSPENDED("SUSPENDED","SUSPENDED");

  companion object {

    // Validating Whether Merchant Status Type exists...
    fun checkMerchantStatusTypes(candidateSymbol: String): Boolean {
      return MerchantStatuses.entries.find {
        it.symbol.equals(candidateSymbol, ignoreCase = true)
      } != null
    }

  }
}