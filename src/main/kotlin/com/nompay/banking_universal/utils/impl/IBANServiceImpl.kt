package com.nompay.banking_universal.utils.impl

import com.nompay.banking_universal.repositories.entities.AccountEntity
import com.nompay.banking_universal.repositories.entities.AccountEntityRepository
import com.nompay.banking_universal.repositories.enums.Currencies
import com.nompay.banking_universal.utils.IBANService
import org.apache.coyote.BadRequestException
import org.springframework.stereotype.Service
import kotlin.random.Random

@Service
class IBANServiceImpl(

  private val ibanStaticPart: String = "GE29NOMP0000000",

  private val NompaySwiftBicCode: String = "NOMPGE22", // Used for international transfers...

  private val accountEntity: AccountEntityRepository

) : IBANService {

  override fun generateAccountIBAN(): String {
    return this.ibanStaticPart + this.generateRandomIntegers();
  }

  private fun generateRandomIntegers(): String {
    val random = Random.Default
    val numbers = (0..9).shuffled(random)
    return numbers.take(9).joinToString("")
  }

  override fun retrieveTransferBankAccountsWithIban(
    fromIban: String,
    toIban: String
  ): Pair<AccountEntity, AccountEntity> {
    val fromIbanAccount: AccountEntity? = this.accountEntity.getAccountByIban(fromIban)
      ?: throw BadRequestException("From Iban account not found")

    val toIbanAccount: AccountEntity? = this.accountEntity.getAccountByIban(toIban)
      ?: throw BadRequestException("To Iban account not found")

    return Pair(fromIbanAccount!!, toIbanAccount!!) // Asserting type as the account should be retrieved.
  }

  override fun retrieveTransferBankAccountsWithEmail(
    fromEmail: String,
    toEmail: String,
    currency: Currencies
  ): Pair<AccountEntity, AccountEntity> {
    val fromEmailAccount: AccountEntity? = this.accountEntity.getAccountByEmail(fromEmail)
      ?: throw BadRequestException("From email account not found")

    val toEmailAccount: AccountEntity? = this.accountEntity.getAccountByEmail(toEmail)
      ?: throw BadRequestException("From email account not found")

    return Pair(fromEmailAccount!!, toEmailAccount!!)
  }

}