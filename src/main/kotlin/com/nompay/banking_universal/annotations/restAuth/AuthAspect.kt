package com.nompay.banking_universal.annotations.restAuth

import com.nompay.banking_universal.utils.SessionService
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.springframework.stereotype.Component

/**
 * Custom annotation used for Auth validation on Graphql routes.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class RequiresAuthRest

@Aspect
@Component
class AuthAspectRest(
  private val sessionService: SessionService
) {
  @Before("@annotation(com.nompay.banking_universal.annotations.graphAuth.RequiresAuth)")
  fun checkAuthorization(
    joinPoints: JoinPoint
  ) {
    val args = joinPoints.args;
  }
}
