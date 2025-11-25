package com.nompay.banking_universal.services

import com.nompay.banking_universal.repositories.dto.merchants.CreateMerchantDto
import com.nompay.banking_universal.repositories.dto.merchants.GetAllMerchantsDto
import com.nompay.banking_universal.repositories.entities.MerchantEntity
import com.nompay.banking_universal.repositories.enums.merchants.MerchantStatuses

interface MerchantService {
  fun createMerchant(createMerchantDto: CreateMerchantDto): MerchantEntity
  fun getMerchantById(merchantId: String): MerchantEntity?

  // Db Selection operations
  fun getAllMerchants(getAllMerchantsDto: GetAllMerchantsDto): List<MerchantEntity>
  fun updateMerchantStatus(status: MerchantStatuses, merchantId: String): String
}