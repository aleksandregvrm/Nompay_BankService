package com.nompay.banking_universal.services

import com.nompay.banking_universal.repositories.dto.user.CreateUserDto
import com.nompay.banking_universal.repositories.dto.user.LoginUserDto
import com.nompay.banking_universal.repositories.dto.user.LoginUserReturnDto
import com.nompay.banking_universal.repositories.dto.user.UpdateUserDto
import com.nompay.banking_universal.repositories.entities.UserEntity

interface UserService {
  fun createUser(createUserDto: CreateUserDto): UserEntity;
  fun updateUser(updateUserDto: UpdateUserDto): String
  fun loginUser(loginUserDto: LoginUserDto): LoginUserReturnDto
  fun logoutUser(userId: Long): String;
}