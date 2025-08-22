package com.nompay.banking_universal.services.impl

import com.nompay.banking_universal.repositories.dto.user.CreateUserDto
import com.nompay.banking_universal.repositories.dto.user.LoginUserDto
import com.nompay.banking_universal.repositories.dto.user.LoginUserReturnDto
import com.nompay.banking_universal.repositories.dto.user.UpdateUserDto
import com.nompay.banking_universal.repositories.entities.UserEntity
import com.nompay.banking_universal.repositories.entities.UserEntityRepository
import com.nompay.banking_universal.services.UserService
import com.nompay.banking_universal.utils.SessionService
import com.nompay.banking_universal.utils.impl.PasswordServiceImpl
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import java.sql.Date

@Service
class UserServiceImpl(

  @Lazy private val passwordService: PasswordServiceImpl,

  @Lazy private val sessionService: SessionService,

  private val userRepository: UserEntityRepository
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

  override fun loginUser(loginUserDto: LoginUserDto): LoginUserReturnDto {
    val (username, password, email) = loginUserDto;

    var user: UserEntity? = null;

    if (username != null) user = userRepository.findUserByUsername(username) else {
      user = userRepository.findUserByEmail(email);
    } // Looking up the relevant user...

    if (user == null) {
      throw IllegalArgumentException("Invalid Credentials")
    } // Making sure such user exists...

    println(user.toString())

    if (!this.passwordService.verifyPassword(password!!, user.password)) {
      throw IllegalArgumentException("Invalid Credentials")
    } // Checking wether the password is correct...

    val refreshToken: String = ""

    val accessToken: String = ""

    return LoginUserReturnDto.Builder()
      .withEmail(user.email)
      .withUsername(user.username)
      .withTimeStamp(Date(System.currentTimeMillis()))
      .withAccessToken(accessToken)
      .withRefreshToken(refreshToken)
      .build()

  }

}