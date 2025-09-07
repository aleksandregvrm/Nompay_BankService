package com.nompay.banking_universal.repositories.entities

import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import java.time.Instant

@Entity
@Table(
  name = "sessions", indexes = [
    Index(name = "idx_session_user_id", columnList = "user_id")
  ]
)
class SessionEntity(
  @Column(name = "access_token")
  val accessToken: String,

  @Column(name = "refresh_token")
  var refreshToken: String,

  @OneToOne(optional = false)
  @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
  val userId: UserEntity
) {
  @Id
  @UuidGenerator
  var id: String? = null

  @Column(name = "create_date", nullable = false)
  var createDate: Instant? = null

  @Column(name = "update_date")
  var updateDate: Instant? = null

  @PrePersist
  fun prePersist() {
    val now = Instant.now()
    createDate = now
    updateDate = now
  }

  override fun toString(): String {
    return "SessionEntity(accessToken='$accessToken', refreshToken='$refreshToken', userId=$userId, id=$id, createDate=$createDate, updateDate=$updateDate)"
  }
}

interface SessionEntityRepository : JpaRepository<SessionEntity, String> {
  fun findByUserId(userId: UserEntity): SessionEntity?

  @Modifying
  @Query("DELETE FROM SessionEntity s WHERE s.userId = :userId")
  fun deleteByUserId(userId: UserEntity): Int
}
