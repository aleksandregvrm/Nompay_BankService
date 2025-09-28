package com.nompay.banking_universal.annotations.auth

import com.nompay.banking_universal.utils.SessionService
import graphql.schema.DataFetchingEnvironment
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.springframework.stereotype.Component
import org.springframework.util.MultiValueMap


/**
 * Custom annotation used for Auth validation on Graphql routes.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class RequiresAuth

/**
 * Aspect that implements the authorization middleware logic.
 * This runs before any method annotated with @RequiresAuth.
 */
@Aspect
@Component
class AuthAspect(
  private val sessionService: SessionService
) {

  @Before("@annotation(com.nompay.banking_universal.annotations.auth.RequiresAuth)")
  fun checkAuthorization(joinPoints: JoinPoint){
    val args = joinPoints.args;
    println("printing args in here...")
    println(args.filterIsInstance<Int>().firstOrNull())
    val userId = args.filterIsInstance<Int>().firstOrNull();
    println(userId)
    println("printing user id in here...")
    val environment = args.filterIsInstance<DataFetchingEnvironment>().firstOrNull()

    if (userId == null || environment == null) {
      throw IllegalStateException("Method annotated with @RequiresAuth must contain a 'userId' Int argument and a 'DataFetchingEnvironment' argument.")
    }

    val headers: MultiValueMap<String, String> = environment.graphQlContext.get("headers")
      ?: throw IllegalStateException("HTTP headers not found in GraphQL context.")

    val authorizationHeader = headers.getFirst("Authorization");
    if (authorizationHeader.isNullOrBlank() || !authorizationHeader.startsWith("Bearer")) {
      throw SecurityException("Authentication token not provided or invalid format.")
    }

    val token = authorizationHeader.substring("Bearer ".length);
    val longUserId = userId.toLong()

    val isTokenValidAndOwned = this.sessionService.checkTokenValidity(token, longUserId);

    if (!isTokenValidAndOwned) {
      throw SecurityException("Unauthorized error for user ID: $userId.")
    }
  }
}