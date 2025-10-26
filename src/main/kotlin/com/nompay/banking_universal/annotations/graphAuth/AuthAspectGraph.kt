package com.nompay.banking_universal.annotations.graphAuth

import com.nompay.banking_universal.repositories.enums.user.UserRoles
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
annotation class RequiresAuthGraph(
  val roles: Array<UserRoles> = []
)

/**
 * Aspect that implements the authorization middleware logic.
 * This runs before any method annotated with @RequiresAuth.
 */
@Aspect
@Component
class AuthAspect(
  private val sessionService: SessionService
) {

  @Before("@annotation(requiresAuthGraph)")
  fun checkAuthorization(joinPoints: JoinPoint, requiresAuthGraph: RequiresAuthGraph) {
    val args = joinPoints.args;

    val userId = args.filterIsInstance<Long>().firstOrNull();

    val environment = args.filterIsInstance<DataFetchingEnvironment>().firstOrNull()

    if (userId == null || environment == null) {
      throw IllegalStateException("Method annotated with @RequiresAuth must contain a 'userId' Long argument and a 'DataFetchingEnvironment' argument.")
    }

    val headers: MultiValueMap<String, String> = environment.graphQlContext.get("headers")
      ?: throw IllegalStateException("HTTP headers not found in GraphQL context.")

    val authorizationHeader = headers.getFirst("Authorization");
    if (authorizationHeader.isNullOrBlank() || !authorizationHeader.startsWith("Bearer")) {
      throw SecurityException("Authentication token not provided or invalid format.")
    }
    val token = authorizationHeader.substring("Bearer ".length);

    val longUserId = userId

    val requiredRoles = requiresAuthGraph.roles;

    val isTokenValidAndOwned = this.sessionService.checkTokenValidity(token, longUserId, requiredRoles);

    if (!isTokenValidAndOwned) {
      throw SecurityException("Unauthorized error for user ID: $userId.")
    }
  }
}
