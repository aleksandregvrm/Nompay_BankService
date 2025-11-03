package com.nompay.banking_universal.controllers

import com.nompay.banking_universal.annotations.graphAuth.RequiresAuthGraph
import com.nompay.banking_universal.repositories.dto.user.CreateUserDto
import com.nompay.banking_universal.repositories.dto.user.LoginUserDto
import com.nompay.banking_universal.repositories.dto.user.LoginUserReturnDto
import com.nompay.banking_universal.repositories.dto.user.RefreshSessionReturnDto
import com.nompay.banking_universal.repositories.entities.UserEntity
import com.nompay.banking_universal.services.UserService
import com.nompay.banking_universal.utils.SessionService
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.stereotype.Controller
import graphql.schema.DataFetchingEnvironment

@Controller
class UserController(
  private val userService: UserService,

  private val sessionService: SessionService
) {

  @MutationMapping(name = "createUser")
  fun createUser(@Argument("input") input: CreateUserDto): UserEntity {
    return this.userService.createUser(input);
  }

  @MutationMapping(name = "loginUser")
  fun loginUser(@Argument("input") input: LoginUserDto): LoginUserReturnDto {
    return this.userService.loginUser(input);
  }

  @MutationMapping(name = "refreshSession")
  fun refreshSession(
    @Argument("userId") userId: Int,
  ): RefreshSessionReturnDto {
    return this.sessionService.refreshSession(userId.toLong())
  }

  @MutationMapping(name = "logoutUser")
  @RequiresAuthGraph
  fun logoutUser(
    @Argument("userId") userId: Int,
    environment: DataFetchingEnvironment
  ): String {
    return this.userService.logoutUser(userId.toLong());
  }
}