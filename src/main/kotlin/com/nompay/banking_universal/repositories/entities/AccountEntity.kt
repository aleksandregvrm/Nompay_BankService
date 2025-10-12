package com.nompay.banking_universal.repositories.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import com.nompay.banking_universal.repositories.enums.accounts.AccountTypes
import com.nompay.banking_universal.repositories.enums.other.Currencies
import jakarta.persistence.*
import org.springframework.data.jpa.repository.JpaRepository
import java.math.BigDecimal
import java.time.Instant


@Entity
@Table(
  name = "account", indexes = [
    Index(name = "idx_account_email", columnList = "email"),
    Index(name = "idx_bank_account", columnList = "iban")
  ]
)
class AccountEntity(
  @Column(name = "email", nullable = false)
  var email: String,

  @Column(name = "name", nullable = false)
  var name: String,

  @Enumerated(EnumType.STRING)
  @Column(name = "currency", nullable = false)
  var currency: Currencies,

  @Column(name = "iban", nullable = false)
  val iban: String,

  @ManyToOne
  @JoinColumn(name = "owner_user_id")
  @JsonIgnore
  var ownerUser: UserEntity? = null,

  @ManyToOne
  @JoinColumn(name = "owner_merchant_id")
  var ownerMerchant: MerchantEntity? = null,

  @Column(name = "account_type")
  var accountType: AccountTypes? = null
) {

  @OneToMany(mappedBy = "fromAccount", cascade = [CascadeType.ALL], orphanRemoval = true)
  var transactions: MutableList<TransactionEntity> = mutableListOf()

  @OneToMany(mappedBy = "toAccount", cascade = [CascadeType.ALL], orphanRemoval = true)
  var receivedTransactions: MutableList<TransactionEntity> = mutableListOf()

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long? = null

  @Column(name = "balance", nullable = false, columnDefinition = "DECIMAL(19,2) DEFAULT 0")
  var balance: BigDecimal = BigDecimal.ZERO

  @Column(name = "create_date", nullable = false, columnDefinition = "DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6)")
  var createDate: Instant? = null

  @PrePersist
  fun prePersist() {
    val now = Instant.now()
    createDate = now
  }

  override fun toString(): String {
    return "AccountEntity(email='$email', name='$name', currency=$currency, iban='$iban', id=$id, balance=$balance, ownerUser=$ownerUser)"
  }
}

interface AccountEntityRepository : JpaRepository<AccountEntity, Long> {
  fun getAccountsByEmail(email: String): List<AccountEntity>?
  fun getAccountByIban(iban: String): AccountEntity?
}
