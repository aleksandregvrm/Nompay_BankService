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

    template.setConnectionFactory(connectionFactory)

    template.keySerializer = StringRedisSerializer()
    template.hashKeySerializer = StringRedisSerializer() // For Hash operations

    val serializer = GenericJackson2JsonRedisSerializer()
    template.valueSerializer = serializer
    template.hashValueSerializer = serializer // For Hash operations

    template.afterPropertiesSet()

    return template
  }
}