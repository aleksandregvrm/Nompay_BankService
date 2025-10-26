package com.nompay.banking_universal.redis

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class RedisServiceImpl(
  private val redisTemplate: RedisTemplate<String, Any?>
) {
  fun setValue(key: String, value: Any, timeoutSeconds: Long? = null) {
    val ops = redisTemplate.opsForValue()
    if (timeoutSeconds != null) {
      ops.set(key, value, timeoutSeconds, TimeUnit.SECONDS)
    } else {
      ops.set(key, value)
    }
  }

  fun getValue(key: String): Any? {
    return redisTemplate.opsForValue().get(key)
  }

  fun deleteKey(key: String): Boolean? {
    return redisTemplate.delete(key)
  }
}