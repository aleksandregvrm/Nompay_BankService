package com.nompay.banking_universal.configs.graphql.cofiguration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.graphql.server.WebGraphQlInterceptor
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import com.nompay.banking_universal.configs.graphql.GraphInterceptor

@Configuration
class GraphQlConfig : WebMvcConfigurer {
  @Bean
  fun requestHeaderInterceptor(): WebGraphQlInterceptor {
    return GraphInterceptor()
  }
}