package com.nompay.banking_universal.repositories.dto.account

import com.nompay.banking_universal.repositories.entities.AccountEntity
import com.nompay.banking_universal.repositories.entities.MerchantEntity

data class GetMerchantAccountsDto(
  val ownerMerchant: MerchantEntity? = null,

  val merchantAccounts: List<AccountEntity>? = null
)
