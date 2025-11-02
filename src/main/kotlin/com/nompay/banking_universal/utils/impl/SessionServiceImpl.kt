package com.nompay.banking_universal.utils.impl

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import com.nompay.banking_universal.redis.RedisServiceImpl
import com.nompay.banking_universal.repositories.entities.SessionEntity
import com.nompay.banking_universal.repositories.entities.SessionEntityRepository
import com.nompay.banking_universal.repositories.entities.UserEntity
import com.nompay.banking_universal.repositories.entities.UserEntityRepository
import com.nompay.banking_universal.repositories.enums.user.UserRoles
import com.nompay.banking_universal.utils.SessionService
import org.apache.coyote.BadRequestException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.util.Date as UtilDate

/**
 * Service for Session related stuff for the user. Which includes
 * Session acquiring, Session Refreshing and Session validation.
 * */
@Component
class SessionServiceImpl(

  private val regexExpiredTokenText: String = """The Token has expired""",

  private var sessionGiverOrganization: String = "Nompany_banking",

  private val sessionRepository: SessionEntityRepository,

  private val userEntity: UserEntityRepository,

  @Value("\${service.tokenValidity}")
  private val tokenValidityHours: String,

  @Value("\${service.tokenSecret}")
  private val tokenSecret: String,

  @Lazy private val cachingService: RedisServiceImpl, // Using Redis to cache session related data...

  private val logger: Logger = LoggerFactory.getLogger(SessionServiceImpl::class.java)
) : SessionService {

  // Used for regex validation of the patterns in the sentence. Mainly used in error handling
  private fun validateSentence(sentence: String?): Boolean {

    if (sentence !is String) { // Checking in case arrived value is not String
      false
    }

    return sentence?.matches(this.regexExpiredTokenText.toRegex()) == false // Check whether the sentence contains the error pattern
  }

  // Here we validate the token and check whether that token belongs to the username requesting it.
  override fun checkTokenValidity(token: String, userId: Long, permittedRoles: Array<UserRoles>): Boolean {
    return try {
      val verifier = JWT.require(Algorithm.HMAC256(this.tokenSecret))
        .withIssuer(this.sessionGiverOrganization)
        .build()
      val decodedJwt = verifier.verify(token);

      this.checkUserRole(
        decodedJwt,
        permittedRoles
      ) // Checking whether route authorized role matches the provided user role

      val tokenUserId = decodedJwt.subject;
      tokenUserId.equals(userId.toString(), ignoreCase = true)
    } catch (e: JWTVerificationException) {
      var clientActionText: String = ""
      // Assigning an error message based on refresh token availability
      when (this.checkRefreshTokenExpiration(userId)) {
        false -> clientActionText = "Please refresh the token"
        true -> clientActionText = "Please re-login"
      }

      if (this.validateSentence(e.message)) throw JWTVerificationException("${e.message} - ${clientActionText}") else false
    }
  }

  /**
   * This function generates needed attributes for the session and sends user
   * two tokens access token and refresh tokens for effective session management
   * @param user
   * @return
   * **/
  override fun generateSession(user: UserEntity): Pair<String, String> {
    val tokenValidityHours = Integer.parseInt(tokenValidityHours)

    val accessToken = this.generateToken(user.id!!, tokenValidityHours, user.role!!)

    val refreshToken = this.generateToken(user.id!!, tokenValidityHours * 24 * 30, user.role!!)

    val session = SessionEntity(
      refreshToken = refreshToken,
      userId = user,
    )

    this.cachingService.setValue( // Caching the logged users refresh token
      "${user.id}_refreshToken", // Defining refresh token key with userId and create date of the token...
      refreshToken,
      (tokenValidityHours * 24 * 30).toLong()
    )

    this.sessionRepository.save(session);
    return Pair(accessToken, refreshToken)
  }

  //Function used for Checking Whether refreshToken has expired
  @Transactional
  private fun checkRefreshTokenExpiration(userId: Long): Boolean {
    if (this.cachingService.getValue("${userId}_refreshToken") == null) {
      // Removing refresh token if stored from the db as well
      this.removeSessionFromDbIfActive(userId)
      return true
    } else {
      // The token is valid...
      return false
    }
  }

  private fun removeSessionFromDbIfActive(userId: Long) {
    val sessionUser = userEntity.findById(userId).orElseThrow {
      BadRequestException("User with user id - $userId not found.")
    }

    sessionRepository.findByUserId(sessionUser)?.let {
      sessionRepository.deleteByUserId(sessionUser)
    }
  }

  override fun refreshSession(user: UserEntity): Pair<String, String> {
    TODO("Not yet implemented")
  }

  override fun generateToken(userId: Long, validityHours: Int, userRole: UserRoles): String {
    val now = UtilDate()
    val expiration = UtilDate(now.time + validityHours * 360000);
    return JWT.create()
      .withIssuer(this.sessionGiverOrganization)
      .withSubject(userId.toString())
      .withClaim("role", userRole.name)
      .withExpiresAt(expiration)
      .sign(Algorithm.HMAC256(this.tokenSecret))
  }

  private fun checkUserRole(userData: DecodedJWT, permittedRoles: Array<UserRoles>): Unit {
    // Checking if the role based Authorization is not enforced on the route.
    val providedRole = if (!permittedRoles.isEmpty()) userData.getClaim("role").asString() else return;

    val permittedRoleNames = permittedRoles.map { it.name }

    if (permittedRoleNames.contains(providedRole)) {
      return
    }

    throw SecurityException("Unauthorized role to access this route")
  }

  // Externally integrated API-s

  override fun checkExternalTokenValidity(token: String, integrationId: String): Boolean {
    TODO("Not yet implemented")
  }

  override fun generateExternalToken(integrationId: String): String {
    TODO("Not yet implemented")
  }
}