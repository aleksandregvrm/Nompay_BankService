package com.nompay.banking_universal.services.impl

import com.nompay.banking_universal.repositories.dto.account.CreateAccountDto
import com.nompay.banking_universal.repositories.dto.account.TransferFundsDto
import com.nompay.banking_universal.repositories.entities.AccountEntity
import com.nompay.banking_universal.repositories.entities.AccountEntityRepository
import com.nompay.banking_universal.repositories.entities.UserEntityRepository
import com.nompay.banking_universal.services.AccountService
import com.nompay.banking_universal.utils.impl.IBANServiceImpl
import com.nompay.banking_universal.utils.impl.SessionServiceImpl
import org.apache.coyote.BadRequestException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Service
class AccountServiceImpl(
  private val ibanService: IBANServiceImpl,

  private val accountRepository: AccountEntityRepository,

  private val userRepository: UserEntityRepository,

  private val sessionService: SessionServiceImpl
) : AccountService {

  override fun createAccount(createAccountDto: CreateAccountDto): AccountEntity {
    val ownerUser = userRepository.findUserByEmailIgnoreCase(createAccountDto.email)
      ?: throw BadRequestException("No User with such email found");
    val userAccounts = accountRepository.getAccountsByEmail(createAccountDto.email);

    if (!userAccounts.isNullOrEmpty()) {
      val sameAccountForCurrency = userAccounts.filter { it.currency == createAccountDto.currency };
      if (sameAccountForCurrency.size > 2) {
        throw IllegalArgumentException(
          "Cannot create three accounts with the " +
              "same currency of ${createAccountDto.currency}"
        )
      }
    }

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
    val (amount, currency, fromEmail, toEmail, fromAccountNumber, toAccountNumber) = transferFundsDto

    // Retrieving the correct Account and if the email is used retrieve the first account with that email
    val fromAccount: AccountEntity = when {
      !fromAccountNumber.isNullOrBlank() -> this.accountRepository.getAccountByIban(fromAccountNumber)
      !fromEmail.isNullOrBlank() && currency != null -> {
        this.accountRepository.getAccountsByEmail(fromEmail)?.firstOrNull { it.currency == currency }
      }

      else -> throw IllegalArgumentException("Source account details are required.")
    } ?: throw IllegalStateException("Source account not found.")

    // Retrieving the correct to Account and if the email is used retrieve the first account with that email
    val toAccount: AccountEntity = when {
      !toAccountNumber.isNullOrBlank() -> this.accountRepository.getAccountByIban(toAccountNumber)
      !toEmail.isNullOrBlank() && currency != null -> {
        this.accountRepository.getAccountsByEmail(toEmail)?.firstOrNull { it.currency == currency }
      }

      else -> throw IllegalArgumentException("Destination account details are required.")
    } ?: throw IllegalStateException("Destination account not found.")

    if (fromAccount.currency != toAccount.currency) {
      throw IllegalStateException("Currency mismatch. Conversions are not supported.")
    }

    if (fromAccount.id == toAccount.id) {
      throw IllegalStateException("Cannot transfer funds to the same account.")
    }

    if (fromAccount.balance.compareTo(amount) < 0) {
      throw IllegalStateException("Insufficient balance.")
    }

    fromAccount.balance = fromAccount.balance.subtract(amount)
    toAccount.balance = toAccount.balance.add(amount)

    this.accountRepository.save(fromAccount)
    this.accountRepository.save(toAccount)

    return "Funds transferred successfully."
  }
}