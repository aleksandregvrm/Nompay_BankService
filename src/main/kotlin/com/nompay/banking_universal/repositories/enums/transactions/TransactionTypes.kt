package com.nompay.banking_universal.repositories.enums.transactions

enum class TransactionTypes(val symbol: String, val type: String) {
  MERCHANTTOUSER("MTU", "MERCHANT_TO_USER"),
  USERTOMERCHANT("UTM", "USER_TO_MERCHANT"),
  MERCHANTTOMERCHANT("MTM", "MERCHANT_TO_MERCHANT"),
  EXTERNALTOMERCHANT("ETM", "EXTERNAL_TO_MERCHANT"),
  MERCHANTTOEXTERNAL("MTE", "MERCHANT_TO_EXTERNAL"),
  EXTERNALTOUSER("ETU", "EXTERNAL_TO_USER"),
  USERTOEXTERNAL("UTE", "USER_TO_EXTERNAL");

  companion object {
    fun checkForTransactionType(typeToCheck: String): Boolean {
      return entries.find {
        it.symbol.equals(typeToCheck, ignoreCase = true)
      } != null
    }
  }
}