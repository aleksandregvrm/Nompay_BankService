package com.nompay.banking_universal.services.impl

import com.nompay.banking_universal.repositories.dto.merchants.CreateMerchantDto
import com.nompay.banking_universal.repositories.entities.MerchantEntity
import com.nompay.banking_universal.repositories.entities.MerchantEntityRepository
import com.nompay.banking_universal.repositories.entities.UserEntity
import com.nompay.banking_universal.repositories.enums.merchants.MerchantStatuses
import com.nompay.banking_universal.services.MerchantService
import com.nompay.banking_universal.services.UserService
import org.springframework.stereotype.Service

@Service
class MerchantServiceImpl(
  private val merchantEntityRepository: MerchantEntityRepository,

  private val userService: UserService,
  ) : MerchantService {

  override fun createMerchant(createMerchantDto: CreateMerchantDto): MerchantEntity {
    val (ownerUserId, accessorUsers) = createMerchantDto

    val ownerUser = this.userService.getUserByUserId(ownerUserId)
      ?: throw IllegalArgumentException("No Owner user found - ${ownerUserId}");

    val allAccessorUsers = this.userService.getUsersByUserId(accessorUsers) ?: mutableListOf()

    val merchant = MerchantEntity(
      ownerUser = ownerUser,
      legalName = createMerchantDto.legalName,
      status = MerchantStatuses.CREATED,
      email = createMerchantDto.email,
      billing = createMerchantDto.billing,
    ).apply {
      this.accessorUsers = allAccessorUsers as MutableList<UserEntity> // Assigning the accessor users to the merchant
    }

    val merchantEntity = this.merchantEntityRepository.save<MerchantEntity>(merchant);
    println(merchantEntity)
    return merchantEntity
  }

  override fun getMerchantById(merchantId: String): MerchantEntity {
    return this.merchantEntityRepository.findById(merchantId)
      .orElseThrow {
        IllegalArgumentException("No merchant found with an id of - $merchantId")
      }
  }
}