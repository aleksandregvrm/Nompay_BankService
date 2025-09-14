package com.nompay.banking_universal.controllers

import com.nompay.banking_universal.repositories.dto.user.CreateUserDto
import com.nompay.banking_universal.repositories.dto.user.LoginUserDto
import com.nompay.banking_universal.repositories.dto.user.LoginUserReturnDto
import com.nompay.banking_universal.repositories.entities.UserEntity
import com.nompay.banking_universal.services.UserService
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.stereotype.Controller
import graphql.schema.DataFetchingEnvironment
import org.springframework.util.MultiValueMap

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
    environment: DataFetchingEnvironment
  ): String {

    // Retrieve the headers from the GraphQLContext
    val headers: MultiValueMap<String, String> = environment.graphQlContext.get("headers")
      ?: throw IllegalStateException("HTTP headers not found in GraphQL context.")
    val authorization = headers.getFirst("Authorization");
    if (authorization.isNullOrBlank()) {
      throw IllegalArgumentException("Not Authorized to logout")
    }

    return this.userService.logoutUser(userId.toLong(), authorization);
  }
}