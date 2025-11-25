package com.nompay.banking_universal.services.impl

import com.nompay.banking_universal.repositories.dto.merchants.CreateMerchantDto
import com.nompay.banking_universal.repositories.dto.merchants.GetAllMerchantsDto
import com.nompay.banking_universal.repositories.dto.merchants.MerchantSpecifications
import com.nompay.banking_universal.repositories.entities.MerchantEntity
import com.nompay.banking_universal.repositories.entities.MerchantEntityRepository
import com.nompay.banking_universal.repositories.entities.UserEntity
import com.nompay.banking_universal.repositories.enums.merchants.MerchantStatuses
import com.nompay.banking_universal.services.MerchantService
import com.nompay.banking_universal.services.UserService
import jakarta.persistence.EntityManager
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import java.time.Instant

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

  // Db Selection operations
  override fun getAllMerchants(getAllMerchantsDto: GetAllMerchantsDto): List<MerchantEntity> {
    val (createDate, legalName, merchantCountry, ownerUser, fromAccountCount, toAccountCount) = getAllMerchantsDto;

    val specs = listOfNotNull(
      MerchantSpecifications.hasLegalNameLike(legalName),
      MerchantSpecifications.hasCountry(merchantCountry),
      MerchantSpecifications.isOwnedByUserProvided(ownerUser),
      MerchantSpecifications.accountCountBetween(
        fromAccountCount,
        toAccountCount
      )
    )

    val finalSpec: Specification<MerchantEntity>? = specs.reduceOrNull { acc, spec -> acc.and(spec) }

    val sortDirection = if (createDate.equals("desc", ignoreCase = true)) {
      Sort.Direction.DESC
    } else {
      Sort.Direction.ASC
    }

    val sort = Sort.by(sortDirection, "createDate")

    return if (finalSpec != null) {
      this.merchantEntityRepository.findAll(finalSpec, sort)
    } else {
      this.merchantEntityRepository.findAll(sort)
    }
  }

  override fun updateMerchantStatus(status: MerchantStatuses, merchantId: String): String {
    val merchant = this.getMerchantById(merchantId)

    merchant.status = status;

    this.merchantEntityRepository.saveAndFlush<MerchantEntity>(merchant)

    return "Merchant status updated - ${status} for the merchant with an id - ${merchantId}"
  }
}