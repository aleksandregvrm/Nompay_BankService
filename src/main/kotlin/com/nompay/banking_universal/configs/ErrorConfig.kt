package com.nompay.banking_universal.configs

import com.auth0.jwt.exceptions.JWTVerificationException
import graphql.GraphQLError
import graphql.GraphqlErrorBuilder
import jakarta.validation.ValidationException
import org.apache.coyote.BadRequestException
import org.springframework.dao.DataAccessException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.graphql.execution.ErrorType
import org.springframework.web.client.HttpClientErrorException.Unauthorized

@ControllerAdvice
class ErrorConfig {

  @GraphQlExceptionHandler
  fun handleIllegalArguments(ex: IllegalArgumentException): GraphQLError {
    return GraphqlErrorBuilder.newError()
      .message("Invalid input: " + ex.message)
      .errorType(ErrorType.BAD_REQUEST)
      .build()
  }

  @GraphQlExceptionHandler
  fun handleIllegalState(ex: IllegalStateException): GraphQLError {
    return GraphqlErrorBuilder.newError()
      .message("Invalid input: " + ex.message)
      .errorType(ErrorType.BAD_REQUEST)
      .build()
  }

  @GraphQlExceptionHandler
  fun handleValidationException(ex: ValidationException): GraphQLError {
    return GraphqlErrorBuilder.newError()
      .message("Validation error: " + ex.message)
      .errorType(ErrorType.BAD_REQUEST)
      .build()
  }

  @GraphQlExceptionHandler
  fun unauthorizedExceptionHandler(ex: Unauthorized): GraphQLError {
    return GraphqlErrorBuilder.newError()
      .message("Unauthorized: " + ex.message)
      .errorType(ErrorType.UNAUTHORIZED)
      .build()
  }

  @GraphQlExceptionHandler
  fun badRequestExceptionHandler(ex: BadRequestException): GraphQLError {
    return GraphqlErrorBuilder.newError()
      .message("Bad Request Exception: " + ex.message)
      .build()
  }

  @GraphQlExceptionHandler
  fun handleDataAccessException(ex: DataAccessException): GraphQLError {
    return GraphqlErrorBuilder.newError()
      .message("Database error: " + ex.message)
      .errorType(ErrorType.BAD_REQUEST)
      .build()
  }

  @GraphQlExceptionHandler
  fun dataIntegrityException(ex: DataIntegrityViolationException): GraphQLError {
    return GraphqlErrorBuilder.newError()
      .message("Database error: " + ex.message)
      .errorType(ErrorType.BAD_REQUEST)
      .build()
  }

  @GraphQlExceptionHandler
  fun handleRuntimeException(ex: RuntimeException): GraphQLError {
    return GraphqlErrorBuilder.newError()
      .message("Runtime Exception: " + ex.message)
      .errorType(ErrorType.INTERNAL_ERROR)
      .build()
  }

  @GraphQlExceptionHandler
  fun jwtVerificationException(ex: JWTVerificationException): GraphQLError {
    return GraphqlErrorBuilder.newError()
      .message("Unauthorized: " + ex.message)
      .errorType(ErrorType.UNAUTHORIZED)
      .build()
  }
}