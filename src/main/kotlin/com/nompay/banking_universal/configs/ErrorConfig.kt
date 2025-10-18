package com.nompay.banking_universal.configs

import com.auth0.jwt.exceptions.JWTVerificationException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import graphql.GraphQLError
import graphql.GraphqlErrorBuilder
import jakarta.validation.ValidationException
import jdk.jshell.spi.ExecutionControl
import org.apache.coyote.BadRequestException
import org.springframework.dao.DataAccessException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.graphql.execution.ErrorType
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.client.HttpClientErrorException.Unauthorized

@ControllerAdvice
class ErrorConfig {

  /*
  * GraphQL error handling
  * */
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

  @GraphQlExceptionHandler
  fun securityException(ex: SecurityException): GraphQLError {
    return GraphqlErrorBuilder.newError()
      .message("Unauthorized: " + ex.message)
      .errorType(ErrorType.UNAUTHORIZED)
      .build()
  }

  /*
  * Rest API Error handling
  * */
  @ExceptionHandler(MethodArgumentNotValidException::class)
  fun handleRestValidationErrors(ex: MethodArgumentNotValidException): ResponseEntity<Map<String, Any>> {
    val errors = ex.bindingResult.fieldErrors.map { error ->
      mapOf(
        "field" to error.field,
        "message" to (error.defaultMessage ?: "Validation failed")
      )
    }

    val errorBody = mapOf(
      "error" to "Validation Failed",
      "status" to HttpStatus.BAD_REQUEST.value(),
      "details" to errors
    )
    return ResponseEntity(errorBody, HttpStatus.BAD_REQUEST)
  }

  @ExceptionHandler(IllegalArgumentException::class)
  fun handleRestIllegalArguments(ex: IllegalArgumentException): ResponseEntity<Any> {
    val errorBody = mapOf("error" to "Bad Request", "message" to ex.message)
    return ResponseEntity(errorBody, HttpStatus.BAD_REQUEST)
  }

  @ExceptionHandler(IllegalStateException::class, BadRequestException::class)
  fun handleRestBadRequests(ex: Exception): ResponseEntity<Any> {
    val errorBody = mapOf("error" to "Bad Request", "message" to ex.message)
    return ResponseEntity(errorBody, HttpStatus.BAD_REQUEST)
  }

  @ExceptionHandler(DataAccessException::class, DataIntegrityViolationException::class)
  fun handleRestDatabaseErrors(ex: DataAccessException): ResponseEntity<Any> {
    val errorBody = mapOf("error" to "Database Error", "message" to "A database error occurred.")
    return ResponseEntity(errorBody, HttpStatus.INTERNAL_SERVER_ERROR)
  }

  @ExceptionHandler(Unauthorized::class, JWTVerificationException::class, SecurityException::class)
  fun handleRestUnauthorized(ex: Exception): ResponseEntity<Any> {
    val errorBody = mapOf("error" to "Unauthorized", "message" to ex.message)
    return ResponseEntity(errorBody, HttpStatus.UNAUTHORIZED)
  }

  @ExceptionHandler(MismatchedInputException::class)
  fun handleJsonParseError(ex: MismatchedInputException): ResponseEntity<Map<String, Any>> {
    val errorBody = mapOf(
      "error" to "Invalid JSON",
      "message" to (ex.originalMessage ?: "Malformed request body")
    )
    return ResponseEntity(errorBody, HttpStatus.BAD_REQUEST)
  }

  @ExceptionHandler(ExecutionControl.NotImplementedException::class)
  fun handleNotImplementedException(ex: ExecutionControl.NotImplementedException): ResponseEntity<Map<String, Any>> {
    val errorBody = mapOf(
      "error" to "Not Implemented",
      "status" to HttpStatus.NOT_IMPLEMENTED.value(),
      "message" to (ex.message ?: "This feature is not yet implemented.")
    )
    return ResponseEntity(errorBody, HttpStatus.NOT_IMPLEMENTED)
  }

  @ExceptionHandler(RuntimeException::class)
  fun handleRestRuntime(ex: RuntimeException): ResponseEntity<Any> {
    val errorBody = mapOf("error" to "Internal Server Error", "message" to "An unexpected error occurred: ${ex.message}")
    return ResponseEntity(errorBody, HttpStatus.INTERNAL_SERVER_ERROR)
  }

}