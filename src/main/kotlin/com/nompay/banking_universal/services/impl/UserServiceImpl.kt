package com.nompay.banking_universal.services.impl

import com.auth0.jwt.exceptions.JWTVerificationException
import com.nompay.banking_universal.repositories.dto.user.CreateUserDto
import com.nompay.banking_universal.repositories.dto.user.LoginUserDto
import com.nompay.banking_universal.repositories.dto.user.LoginUserReturnDto
import com.nompay.banking_universal.repositories.dto.user.UpdateUserDto
import com.nompay.banking_universal.repositories.entities.SessionEntityRepository
import com.nompay.banking_universal.repositories.entities.UserEntity
import com.nompay.banking_universal.repositories.entities.UserEntityRepository
import com.nompay.banking_universal.services.UserService
import com.nompay.banking_universal.utils.SessionService
import com.nompay.banking_universal.utils.impl.PasswordServiceImpl
import jakarta.transaction.Transactional
import org.springframework.context.annotation.Lazy
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import java.sql.Date

@Service
class UserServiceImpl(

  @Lazy private val passwordService: PasswordServiceImpl,

  @Lazy private val sessionService: SessionService,

  private val userRepository: UserEntityRepository,

  @Lazy private val sessionRepository: SessionEntityRepository
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
    }
    userRepository.save(user); // Saving the user to the databse
    println(user.toString())
    return user;
  }

  override fun updateUser(updateUserDto: UpdateUserDto): String {
    return "User data updated..."
  }

  @Transactional
  override fun logoutUser(userId: Long, accessToken: String): String { // In case we incorrectly delete the access token...
    val authorizationCheck = this.sessionService.checkTokenValidity((accessToken));

    if(!authorizationCheck){
      throw JWTVerificationException("User Unauthorized")
    }
    // Check if the user is empty if is it is assigned the null value.
    val user = userRepository.findById(userId).orElse(null) ?: throw IllegalArgumentException("no such user Id exists");

    try {
      sessionRepository.deleteByUserId(user)
    } catch (e: DataIntegrityViolationException) {
      println(e.message)
      throw e
    }
    return "User logged out."
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

    if(activeSession != null){
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