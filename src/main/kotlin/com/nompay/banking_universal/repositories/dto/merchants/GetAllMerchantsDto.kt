package com.nompay.banking_universal.repositories.dto.merchants

import com.nompay.banking_universal.repositories.entities.AccountEntity
import com.nompay.banking_universal.repositories.entities.MerchantEntity
import com.nompay.banking_universal.repositories.entities.UserEntity
import jakarta.persistence.criteria.JoinType
import org.springframework.data.jpa.domain.Specification

data class GetAllMerchantsDto(
  val createDate: String? = null,

  val legalName: String? = null,

  val merchantCountry: String? = null,

  val ownerUser: String? = null,

  val fromAccountCount: Int? = null,

  val toAccountCount: Int? = null
)

// Static Object used for iterating over the provided filters for the getAllMerchants query.
object MerchantSpecifications {
  fun hasLegalNameLike(legalName: String?): Specification<MerchantEntity>? = legalName?.let { name ->
    if (name.isBlank()) return@let null
    Specification { root, query, cb ->
      // Use LIKE and surround the parameter with wildcards (%)
      cb.like(root.get("legalName"), "%$name%")
    }
  }

  fun hasCountry(merchantCountry: String?): Specification<MerchantEntity>? = merchantCountry?.let { country ->
    if (country.isBlank()) return@let null
    Specification { root, query, cb ->

      val jsonPath = cb.literal("$.country")

      val jsonExtract = cb.function(
        "json_extract",
        String::class.java,
        root.get<Any>("billing"),
        jsonPath
      )

      cb.equal(jsonExtract, country)
    }
  }

  fun isOwnedByUserProvided(ownerUserId: String?): Specification<MerchantEntity>? = ownerUserId?.let { userId ->
    if (userId.isBlank()) return@let null
    Specification { root, query, cb ->
      cb.equal(root.get<UserEntity>("ownerUser").get<String>("id"), userId)
    }
  }

  fun accountCountBetween(from: Int?, to: Int?): Specification<MerchantEntity>? = when {
    from != null || to != null -> Specification { root, query, cb ->

      val existingWhere = query?.restriction

      val accounts = root.join<MerchantEntity, AccountEntity>("merchantAccounts", JoinType.LEFT)

      query?.distinct(true)?.groupBy(root.get<String>("id"))

      val countPredicate = cb.count(accounts)

      val havingPredicate = when {
        from != null && to != null -> cb.between(countPredicate, from.toLong(), to.toLong())
        from != null -> cb.greaterThanOrEqualTo(countPredicate, from.toLong())
        to != null -> cb.lessThanOrEqualTo(countPredicate, to.toLong())
        else -> null
      }

      if (existingWhere != null && havingPredicate != null) {
        query.having(cb.and(existingWhere, havingPredicate))
      } else if (havingPredicate != null) {
        query?.having(havingPredicate)
      }

      cb.conjunction()
    }

    else -> null
  }
}
