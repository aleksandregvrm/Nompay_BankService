package com.nompay.banking_universal.utils.impl

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
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


  // Here we validate the token and check whether that token belongs to the username requesting it.
  override fun checkTokenValidity(token: String, userId: Long): Boolean {
    return try {
      val verifier = JWT.require(Algorithm.HMAC256(this.tokenSecret))
        .withIssuer(this.sessionGiverOrganization)
        .build()
      val decodedJwt = verifier.verify(token);
//      val checkForRole = this.checkUserRole(decodedJwt)
      val tokenUserId = decodedJwt.subject;
      tokenUserId.equals(userId.toString(), ignoreCase = true)
    } catch (e: JWTVerificationException) {
      println(e.message)
      false
    }
  }

  /**
   * This function generates needed attributes for the session and sends user
   * two tokens access token and refresh tokens for effective session management
   * @param user
   * @return
   * **/
  override fun generateSession(user: UserEntity): Pair<String, String> {
    val accessToken = this.generateToken(user.id!!, Integer.parseInt(tokenValidityHours))

    val refreshToken = this.generateToken(user.id!!, Integer.parseInt(tokenValidityHours) * 24 * 30)

    val session = SessionEntity(
      accessToken = accessToken,
      refreshToken = refreshToken,
      userId = user,
    )
    this.sessionRepository.save(session);
    return Pair(accessToken, refreshToken)
  }

  override fun generateToken(userId: Long, validityHours: Int): String {
    val now = UtilDate()
    val expiration = UtilDate(now.time + validityHours * 360000);
    return JWT.create()
      .withIssuer(this.sessionGiverOrganization)
      .withSubject(userId.toString())
      .withClaim("role", UserRoles.USER.name)
      .withExpiresAt(expiration)
      .sign(Algorithm.HMAC256(this.tokenSecret))
  }

  private fun checkUserRole(userData: DecodedJWT): UserRoles {
    TODO()
  }
}