package com.nompay.banking_universal.services

import com.nompay.banking_universal.repositories.dto.merchants.CreateMerchantDto
import com.nompay.banking_universal.repositories.entities.MerchantEntity

interface MerchantService {
  fun createMerchant(createMerchantDto: CreateMerchantDto): MerchantEntity
  fun getMerchantById(merchantId: String): MerchantEntity
}