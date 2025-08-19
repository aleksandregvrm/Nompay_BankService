package com.nompay.banking_universal.controllers

import com.nompay.banking_universal.repositories.dto.user.CreateUserDto
import com.nompay.banking_universal.repositories.entities.UserEntity
import com.nompay.banking_universal.services.UserService
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.stereotype.Controller

@Controller
class UserController(
  private val userService: UserService
) {

  @MutationMapping(name = "createUser")
  fun createUser(@Argument("input") input: CreateUserDto):UserEntity {
    return this.userService.createUser(input);
  }
}