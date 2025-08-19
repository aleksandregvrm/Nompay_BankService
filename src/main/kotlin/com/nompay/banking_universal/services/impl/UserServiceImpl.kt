package com.nompay.banking_universal.services.impl

import com.nompay.banking_universal.repositories.dto.user.CreateUserDto
import com.nompay.banking_universal.repositories.dto.user.UpdateUserDto
import com.nompay.banking_universal.repositories.entities.UserEntity
import com.nompay.banking_universal.repositories.entities.UserEntityRepository
import com.nompay.banking_universal.services.UserService
import com.nompay.banking_universal.utils.impl.PasswordServiceImpl
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
  private val passwordService: PasswordServiceImpl,
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


}