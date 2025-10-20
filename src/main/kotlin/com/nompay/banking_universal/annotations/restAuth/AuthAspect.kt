package com.nompay.banking_universal.annotations.restAuth

import com.nompay.banking_universal.utils.SessionService
import jakarta.servlet.http.HttpServletRequest
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import org.aspectj.lang.reflect.MethodSignature

/**
 * Custom annotation used for Auth validation on Rest routes.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class RequiresAuthRest(
  val integrationIdArgumentName: String = "integrationId" // Name of the argument in the controller method that holds the user ID
)


@Aspect
@Component
class AuthAspectRest(
  private val sessionService: SessionService
) {
  @Before("@annotation(requiresAuthRest)")
  fun checkAuthorization(
    joinPoints: JoinPoint,
    requiresAuthRest: RequiresAuthRest
  ) {
    val request: HttpServletRequest = try {
      (RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes).request
    } catch (e: IllegalStateException) {
      throw IllegalStateException("Method annotated with @RequiresAuthRest called outside of an HTTP request context.")
    }

    val authorizationHeader = request.getHeader("Authorization")

    if (authorizationHeader.isNullOrBlank() || !authorizationHeader.startsWith("Bearer ")) {
      throw SecurityException("Authentication token not provided or invalid")
    }

    val token = authorizationHeader.substring("Bearer ".length)
    println(token)

    val integrationIdArgumentName = requiresAuthRest.integrationIdArgumentName;

    val args = joinPoints.args

    val signature =
      joinPoints.signature as? MethodSignature ?: throw IllegalStateException("JoinPoint is not a method execution.")
    val parameterNames = signature.parameterNames

    val integrationIdIndex = parameterNames.indexOf(integrationIdArgumentName)

    if (integrationIdIndex == -1 || integrationIdIndex >= args.size) {
      throw IllegalStateException("Method annotated with @RequiresAuthRest must contain an argument named '$integrationIdArgumentName' which holds the user ID.")
    }

    val integrationId: Long = when (val integrationIdArg = args[integrationIdIndex]) {
      is Int -> integrationIdArg.toLong()
      is Long -> integrationIdArg
      else -> throw IllegalStateException("User ID argument must be of type Int or Long.")
    }

    val isTokenValidAndOwned = this.sessionService.checkTokenValidity(token, integrationId)

    if (!isTokenValidAndOwned) {
      throw SecurityException("Unauthorized error for user ID: $integrationId.")
    }

  }
}


