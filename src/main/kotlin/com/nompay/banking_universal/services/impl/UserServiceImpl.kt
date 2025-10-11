package com.nompay.banking_universal.services.impl

import com.auth0.jwt.exceptions.JWTVerificationException
import com.nompay.banking_universal.repositories.dto.user.CreateUserDto
import com.nompay.banking_universal.repositories.dto.user.LoginUserDto
import com.nompay.banking_universal.repositories.dto.user.LoginUserReturnDto
import com.nompay.banking_universal.repositories.dto.user.UpdateUserDto
import com.nompay.banking_universal.repositories.entities.SessionEntityRepository
import com.nompay.banking_universal.repositories.entities.UserEntity
import com.nompay.banking_universal.repositories.entities.UserEntityRepository
import com.nompay.banking_universal.repositories.enums.user.UserRoles
import com.nompay.banking_universal.services.UserService
import com.nompay.banking_universal.utils.SessionService
import com.nompay.banking_universal.utils.impl.PasswordServiceImpl
import org.apache.coyote.BadRequestException
import org.springframework.context.annotation.Lazy
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.sql.Date

@Service
class UserServiceImpl(

  @Lazy private val passwordService: PasswordServiceImpl,

  private val sessionService: SessionService,

  private val userRepository: UserEntityRepository,

  private val sessionRepository: SessionEntityRepository
) : UserService {

  override fun createUser(createUserDto: CreateUserDto): UserEntity {

    val user: UserEntity = UserEntity(
      name = createUserDto.name,
      email = createUserDto.email,
      username = createUserDto.username,
      birthDate = createUserDto.birthDate,
    ).apply {
      this.password = try {
        this@UserServiceImpl.passwordService.hashPassword(createUserDto.password)
      } catch (e: RuntimeException) {
        throw RuntimeException("Password hashing has failed")
      } // Hashing password validation for runtime purposes...
      this.role = UserRoles.USER // By Default we assign a role of user to the Registered User.
    }
    userRepository.save(user); // Saving the user to the databse
    return user;
  }

  // Internal function that looks for the user based on the userId
  fun getUserByUserId(userId: Long): UserEntity? {
    val user: UserEntity? = userRepository.findById(userId).orElse(null);
    return user
  }

  override fun updateUser(updateUserDto: UpdateUserDto): String {
    return "User data updated..."
  }

  @Transactional
  override fun logoutUser(
    userId: Long,
  ): String { // In case we incorrectly delete the access token...
    val availableUserSession =
      this.getUserByUserId(userId);

    if (availableUserSession == null) { // Writing comparison manually and null inline null validation fails.
      throw BadRequestException("User with user id - ${userId} Not found.")
    }

    val userSession = this.sessionRepository.findByUserId(availableUserSession);
    if (userSession == null) {
      throw BadRequestException("User with user id - ${userId} is not active.")
    }
    try {
      sessionRepository.deleteByUserId(availableUserSession)
      return "User logged out."
    } catch (e: DataIntegrityViolationException) {
      throw e
    }
  }

  override fun loginUser(loginUserDto: LoginUserDto): LoginUserReturnDto {
    val (username, email, password) = loginUserDto;

    val user = when {
      username != null -> userRepository.findUserByUsername(username)
      email != null -> userRepository.findUserByEmailIgnoreCase(email)
      else -> throw IllegalArgumentException("Invalid Credentials")
    }

    if (user == null) {
      throw IllegalArgumentException("Invalid Credentials")
    } // Making sure such user exists...

    val activeSession = sessionRepository.findByUserId(user);

    if (activeSession != null) {
      throw IllegalArgumentException("User is already logged in")
    }

    if (!this.passwordService.verifyPassword(password!!, user.password)) {
      throw IllegalArgumentException("Invalid Credentials")
    } // Checking wether the password is correct...

    val tokens: Pair<String, String> = this.sessionService.generateSession(user)

    val accessToken = tokens.first;
    val refreshToken = tokens.second;

    return LoginUserReturnDto.Builder()
      .withEmail(user.email)
      .withUsername(user.username)
      .withTimeStamp(Date(System.currentTimeMillis()))
      .withAccessToken(accessToken)
      .withRefreshToken(refreshToken)
      .build()

  }

}