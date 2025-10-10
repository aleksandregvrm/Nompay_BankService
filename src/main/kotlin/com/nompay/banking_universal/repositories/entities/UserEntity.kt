package com.nompay.banking_universal.repositories.entities

import com.nompay.banking_universal.repositories.enums.user.UserRoles
import jakarta.persistence.*
import org.springframework.data.jpa.repository.JpaRepository
import java.sql.Date
import java.time.Instant

@Entity
@Table(
  name = "user",
  uniqueConstraints = [
    UniqueConstraint(columnNames = ["email"]),
    UniqueConstraint(columnNames = ["username"])
  ],
  indexes = [
    Index(name = "idx_user_email", columnList = "email"),
    Index(name = "idx_user_username", columnList = "username")
  ]
)
class UserEntity(
  @Column(name = "email", nullable = false, unique = true)
  var email: String,

  @Column(name = "name", nullable = false)
  var name: String,

  @Column(name = "username", nullable = false, unique = true)
  var username: String,

  @Column(name = "birth_date", nullable = false)
  var birthDate: Date
) {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long? = null

  @Column(name = "create_date", nullable = false)
  var createDate: Instant? = null

  @Column(name = "password", nullable = false)
  lateinit var password: String

  @Column(name = "update_date")
  var updateDate: Instant? = null

  @Column(name = "role")
  @Enumerated(EnumType.STRING)
  var role: UserRoles? = null;

  @OneToMany(mappedBy = "ownerUser", cascade = [CascadeType.ALL], orphanRemoval = true)
  var accounts: MutableSet<AccountEntity> = mutableSetOf()

  @OneToMany(mappedBy = "fromUserId", cascade = [CascadeType.ALL], orphanRemoval = true)
  var transactions: MutableList<TransactionEntity> = mutableListOf()

  @OneToMany(mappedBy = "toUserId", cascade = [CascadeType.ALL], orphanRemoval = true)
  var receivedTransactions: MutableList<TransactionEntity> = mutableListOf()

  @OneToOne(mappedBy = "userId", cascade = [CascadeType.ALL], orphanRemoval = true)
  val sessionId: SessionEntity? = null

  @PrePersist
  fun prePersist() {
    val now = Instant.now()
    createDate = now
    updateDate = now
  }

  @PreUpdate
  fun preUpdate() {
    updateDate = Instant.now()
  }

  override fun toString(): String {
    return "UserEntity(" +
        "email='$email', " +
        "name='$name', " +
        "username='$username', " +
        "birthDate=$birthDate, " +
        "id=$id, " +
        "createDate=$createDate, " +
        "updateDate=$updateDate)" // Accounts are removed here
  }
}

interface UserEntityRepository : JpaRepository<UserEntity, Long> {
  fun findUserByEmailIgnoreCase(email: String): UserEntity?
  fun findUserByUsername(username: String): UserEntity?
}
