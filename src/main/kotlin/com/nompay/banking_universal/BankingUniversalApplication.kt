package com.nompay.banking_universal

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.util.concurrent.locks.Lock

@SpringBootApplication
class BankingUniversalApplication

fun main(args: Array<String>) {
  class LockOwner(val lock: Lock) {

    fun runUnderLock(body: () -> Unit) {
      synchronized(lock, body)
    }

  }

  val goer = LockOwner()
  runApplication<BankingUniversalApplication>(*args)
}
