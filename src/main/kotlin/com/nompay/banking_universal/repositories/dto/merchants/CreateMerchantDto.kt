package com.nompay.banking_universal.repositories.dto.merchants

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class CreateMerchantDto(
  @field:NotNull(message = "Please specify the owner user id")
  val ownerUserId: Long,

  @field:NotNull(message = "Please specify the accessor users")
  val accessorUsers: List<Long>,

  @field:NotBlank(message = "Please specify the legal name")
  val legalName: String,

  @field:NotBlank(message = "Please specify the email")
  val email: String,

  @field:NotNull(message = "Please specify the merchant billing")
  val billing: MerchantBilling
)

data class MerchantBilling(
  @field:NotBlank(message = "Please specify the billing:address")
  val address: String,

  @field:NotBlank(message = "Please specify the billing:city")
  val city: String,

  @field:NotBlank(message = "Please specify the billing:country")
  val country: String,

  @field:NotBlank(message = "Please specify the billing:postalCode")
  val postalCode: String,

  @field:NotBlank(message = "Please specify the billing:director")
  val director: String,

  @field:NotBlank(message = "Please specify the billing:primaryPhone")
  val primaryPhone: String
)