package com.nompay.banking_universal.redis

import kotlinx.coroutines.CoroutineScope
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.RedisTemplate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.springframework.data.redis.core.ScanOptions
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class RedisServiceImpl(
  private val redisTemplate: RedisTemplate<String, Any?>,
) {

  val logger: Logger = LoggerFactory.getLogger(RedisServiceImpl::class.java)

  fun setValue(key: String, value: Any, timeoutSeconds: Long? = null) {
    val ops = redisTemplate.opsForValue()
    if (timeoutSeconds != null) {
      ops.set(key, value, timeoutSeconds, TimeUnit.SECONDS)
    } else {
      ops.set(key, value)
    }
  }

  fun getValue(key: String): Any? = redisTemplate.opsForValue().get(key)

  fun deleteKey(key: String): Boolean? = redisTemplate.delete(key)

  fun ramLoadCheck() {
    val runtime = Runtime.getRuntime();

    val usedMemory =
      ((runtime.totalMemory()) - runtime.freeMemory()) / (1024 * 1024) // Amount of free memory available in megabytes.
    val totalMemory =
      runtime.totalMemory() / (1024 * 1024) // Amount of total memory available on hardware in megabytes.
    val maxMemory =
      runtime.maxMemory() / (1024 * 1024) // Max amount of memory that can be allocated to the execution.
    println(
      """
        []
        Used Memory:  ${usedMemory} MB
        Total Memory: ${totalMemory} MB
        Max Memory:   ${maxMemory} MB
        """.trimIndent()
    ) // In Future will be used to invalidate sessions once every three days when there's too many concurrent sessions active. In order to preserve the Ram storage

  }

  fun getKeyExpiration(key: String): Long? = redisTemplate.getExpire(key, TimeUnit.SECONDS)

  fun performPrematureCacheCleanup(
    keyPattern: String = "*_refreshToken",
    batchSize: Int = 1000,
    maxNumericPrefix: Int = 100_000
  ) {
    logger.info("Starting cleanup for refresh tokens with numeric prefix up to $maxNumericPrefix")

    CoroutineScope(Dispatchers.IO).launch {
      var processedCount = 0

      redisTemplate.execute { connection ->
        val options = ScanOptions.scanOptions()
          .match(keyPattern)
          .count(batchSize.toLong())
          .build()

        val cursor = connection.keyCommands().scan(options)

        cursor.use {
          while (cursor.hasNext()) {
            val keyBytes = cursor.next()
            val key = redisTemplate.stringSerializer.deserialize(keyBytes) ?: continue

            val prefix = key.substringBefore("_refreshToken").toIntOrNull() ?: continue
            if (prefix > maxNumericPrefix) continue

            val expiration = getKeyExpiration(key) ?: continue
            if (expiration <= 0) {
              deleteKey(key)
              logger.info("Deleted expired key: $key")
            }

            processedCount++
            if (processedCount % 10_000 == 0) {
              logger.info("Processed $processedCount eligible keys so far...")
            }
          }
        }
      }

      logger.info("Redis cleanup finished — processed $processedCount keys.")
    }
  }

  companion object {

    @Volatile
    private var triggeredCleanup: Boolean =
      false // Used for static identifier per instance to trigger redis cleanup job...

    fun triggerCleanup(service: RedisServiceImpl, condition: Boolean) {
      if (condition && !triggeredCleanup) {
        try {
          service.performPrematureCacheCleanup()
        } finally {
          triggeredCleanup = false
        }
      }
    }
  }
}