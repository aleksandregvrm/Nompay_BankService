package com.nompay.banking_universal.configs.redis

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class RedisConfig {

  @Bean
  fun redisTemplate(connectionFactory: RedisConnectionFactory): RedisTemplate<String, Any?> {
    val template = RedisTemplate<String, Any?>()

    // 1. Set the connection factory (Autoconfigured by Spring Boot Starter)
    template.setConnectionFactory(connectionFactory)

    // 2. Define Key Serializer
    // Keys should almost always be Strings for readability in Redis-CLI
    template.keySerializer = StringRedisSerializer()
    template.hashKeySerializer = StringRedisSerializer() // For Hash operations

    // 3. Define Value Serializer
    // Use GenericJackson2JsonRedisSerializer to serialize/deserialize Objects (Any?)
    // using Jackson JSON, which is a good default for complex objects.
    val serializer = GenericJackson2JsonRedisSerializer()
    template.valueSerializer = serializer
    template.hashValueSerializer = serializer // For Hash operations

    // 4. Initialize the template
    template.afterPropertiesSet()

    return template
  }
}