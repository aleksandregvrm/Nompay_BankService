package com.nompay.banking_universal.services.impl

import com.nompay.banking_universal.repositories.dto.account.CreateAccountDto
import com.nompay.banking_universal.repositories.dto.account.TransferFundsDto
import com.nompay.banking_universal.repositories.entities.AccountEntity
import com.nompay.banking_universal.repositories.entities.AccountEntityRepository
import com.nompay.banking_universal.repositories.entities.UserEntityRepository
import com.nompay.banking_universal.services.AccountService
import com.nompay.banking_universal.utils.impl.IBANServiceImpl
import org.apache.coyote.BadRequestException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Service
class AccountServiceImpl(
  private val ibanService: IBANServiceImpl,

  private val accountRepository: AccountEntityRepository,

  private val userRepository: UserEntityRepository
) : AccountService {

  override fun createAccount(createAccountDto: CreateAccountDto): AccountEntity {
    val ownerUser = userRepository.findUserByEmailIgnoreCase(createAccountDto.email)
      ?: throw BadRequestException("No User with such email found");
    val account = AccountEntity(
      email = createAccountDto.email,
      name = createAccountDto.name,
      currency = createAccountDto.currency,
      iban = this.ibanService.generateAccountIBAN(),
      ownerUser = ownerUser
    ).apply {
      this.balance = BigDecimal.ZERO
    }

    this.accountRepository.save(account);

    return account
  }

  @Transactional
  override fun transferFunds(transferFundsDto: TransferFundsDto): String {
    TODO("Not yet implemented")
  }
}