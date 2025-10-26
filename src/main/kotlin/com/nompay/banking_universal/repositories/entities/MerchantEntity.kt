package com.nompay.banking_universal.repositories.entities

import com.nompay.banking_universal.repositories.dto.merchants.MerchantBilling
import com.nompay.banking_universal.repositories.enums.merchants.MerchantStatuses
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.PrePersist
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.annotations.UuidGenerator
import org.hibernate.type.SqlTypes
import org.springframework.data.jpa.repository.JpaRepository
import java.time.Instant

@Entity
@Table(
  name = "merchants",
  uniqueConstraints = [
    UniqueConstraint(columnNames = ["legal_name"]),
  ],
  indexes = [
    Index(name = "idx_merchant_email", columnList = "email"),
    Index(name = "idx_owner_user", columnList = "owner_user")
  ]
)
class MerchantEntity(
  @ManyToOne
  @JoinColumn(name = "owner_user")
  val ownerUser: UserEntity,

  @OneToMany(mappedBy = "ownerMerchant", cascade = [CascadeType.ALL], orphanRemoval = true)
  val merchantAccounts: MutableList<AccountEntity> = mutableListOf(),

  @Column(name = "legal_name")
  val legalName: String,

  @Column(name = "merchant_status")
  @Enumerated(EnumType.STRING)
  val status: MerchantStatuses,

  @Column(name = "email")
  val email: String,

  @Column(name = "billing")
  @JdbcTypeCode(SqlTypes.JSON)
  val billing: MerchantBilling,
) {
  @Id
  @UuidGenerator
  var id: String? = null

  @ManyToMany
  @JoinTable(
    name = "merchant_accessor_user",
    joinColumns = [JoinColumn(name = "merchant_id")],
    inverseJoinColumns = [JoinColumn(name = "user_id")],
  )
  var accessorUsers: MutableList<UserEntity>? = mutableListOf()

  @Column(name = "create_date", nullable = false)
  var createDate: Instant? = null

  @PrePersist
  fun prePersist() {
    val now = Instant.now()
    createDate = now
  }

  override fun toString(): String {
    return "MerchantEntity(ownerUser=$ownerUser, merchantAccounts=$merchantAccounts, legalName='$legalName', status=$status, email='$email', billing=$billing, id=$id, accessorUsers=$accessorUsers, createDate=$createDate)"
  }
}

interface MerchantEntityRepository : JpaRepository<MerchantEntity, String> {

}