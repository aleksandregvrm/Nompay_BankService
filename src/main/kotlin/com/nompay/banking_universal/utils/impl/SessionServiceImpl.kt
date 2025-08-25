package com.nompay.banking_universal.utils.impl

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.nompay.banking_universal.repositories.entities.SessionEntity
import com.nompay.banking_universal.repositories.entities.SessionEntityRepository
import com.nompay.banking_universal.repositories.entities.UserEntity
import com.nompay.banking_universal.repositories.enums.user.UserRoles
import com.nompay.banking_universal.utils.SessionService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.Date as UtilDate

@Component
class SessionServiceImpl(

  private var sessionGiverOrganization: String = "Nompany_banking",

  private val sessionRepository: SessionEntityRepository,

  @Value("\${service.tokenValidity}")
  private val tokenValidityHours: String,

  @Value("\${service.tokenSecret}")
  private val tokenSecret: String,
) : SessionService {


  override fun checkTokenValidity(token: String): Boolean {
    TODO("Not yet implemented")
  }

  /**
   * This function generates needed attributes for the session and sends user
   * two tokens access token and refresh tokens for effective session management
   * @param user
   * @return
   * **/
  override fun generateSession(user: UserEntity): Pair<String, String> {
    val accessToken = this.generateToken(user.username, Integer.parseInt(tokenValidityHours))

    val refreshToken = this.generateToken(user.username, Integer.parseInt(tokenValidityHours) * 24 * 30)

    val session = SessionEntity(
      accessToken = accessToken,
      refreshToken = refreshToken,
      userId = user,
    )
    this.sessionRepository.save(session);
    return Pair(accessToken, refreshToken)
  }

  override fun generateToken(subject: String, validityHours: Int): String {
    val now = UtilDate()
    val expiration = UtilDate(now.time + validityHours * 360000);
    return JWT.create()
      .withIssuer(this.sessionGiverOrganization)
      .withSubject(subject)
      .withClaim("role", UserRoles.USER.name)
      .withExpiresAt(expiration)
      .sign(Algorithm.HMAC256(this.tokenSecret))
  }

  override fun checkUserRole(userData: HashMap<String, Any>): UserRoles {
    TODO("Not yet implemented")
  }
}