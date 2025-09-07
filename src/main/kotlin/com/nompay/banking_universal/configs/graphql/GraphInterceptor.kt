package com.nompay.banking_universal.configs.graphql

import org.springframework.graphql.server.WebGraphQlInterceptor
import org.springframework.graphql.server.WebGraphQlRequest
import org.springframework.graphql.server.WebGraphQlResponse
import reactor.core.publisher.Mono

class GraphInterceptor : WebGraphQlInterceptor {
  override fun intercept(request: WebGraphQlRequest, chain: WebGraphQlInterceptor.Chain): Mono<WebGraphQlResponse> {
    val headers = request.headers

    // This is where you store the headers in the GraphQLContext
    request.configureExecutionInput { _, builder ->
      builder.graphQLContext(mapOf("headers" to headers)).build()
    }

    return chain.next(request)
  }
}
