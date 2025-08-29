package com.nompay.banking_universal.controllers

import com.nompay.banking_universal.repositories.dto.user.CreateUserDto
import com.nompay.banking_universal.repositories.dto.user.LoginUserDto
import com.nompay.banking_universal.repositories.dto.user.LoginUserReturnDto
import com.nompay.banking_universal.repositories.entities.UserEntity
import com.nompay.banking_universal.services.UserService
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.server.WebGraphQlRequest
import org.springframework.stereotype.Controller

@Controller
class UserController(
  private val userService: UserService
) {

  @MutationMapping(name = "createUser")
  fun createUser(@Argument("input") input: CreateUserDto): UserEntity {
    return this.userService.createUser(input);
  }

  @MutationMapping(name = "loginUser")
  fun loginUser(@Argument("input") input: LoginUserDto): LoginUserReturnDto {
    return this.userService.loginUser(input);
  }

  @MutationMapping(name = "logoutUser")
  fun logoutUser(
    @Argument("userId") userId: Int,
    webGraphQlRequest: WebGraphQlRequest
  ): String{
    println(webGraphQlRequest.headers)
    val authorization = webGraphQlRequest.headers.getFirst("Authorization");
    if(authorization == null){
      throw IllegalArgumentException("Not Authorized")
    }

    return this.userService.logoutUser(userId.toLong(), authorization);
  }
}