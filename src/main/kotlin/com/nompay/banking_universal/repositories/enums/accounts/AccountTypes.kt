package com.nompay.banking_universal.repositories.enums.accounts


enum class AccountTypes(val symbol: String, val type: String) {
  USERACCOUNT("USER_ACCOUNT", "USER_ACCOUNT"),
  MERCHANTACCOUNT("MERCHANT_ACCOUNT", "MERCHANT_ACCOUNT");

  companion object {

    // Validating Whether incoming Account Type exists...
    fun checkAccountType(candidateSymbol: String): Boolean {
      return AccountTypes.entries.find {
        it.symbol.equals(candidateSymbol, ignoreCase = true)
      } != null
    }

  }
}