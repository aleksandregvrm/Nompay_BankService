package com.nompay.banking_universal.services.impl

import com.nompay.banking_universal.repositories.dto.account.CreateAccountDto
import com.nompay.banking_universal.repositories.dto.account.FindAccountsToTransferDto
import com.nompay.banking_universal.repositories.dto.account.TransferFundsDto
import com.nompay.banking_universal.repositories.dto.account.TransferFundsInternallyDto
import com.nompay.banking_universal.repositories.dto.transactions.CreateTransactionDto
import com.nompay.banking_universal.repositories.entities.AccountEntity
import com.nompay.banking_universal.repositories.entities.AccountEntityRepository
import com.nompay.banking_universal.repositories.entities.ExternalAccountEntity
import com.nompay.banking_universal.repositories.entities.UserEntityRepository
import com.nompay.banking_universal.repositories.enums.transactions.TransactionStatuses
import com.nompay.banking_universal.repositories.enums.transactions.TransactionTypes
import com.nompay.banking_universal.services.AccountService
import com.nompay.banking_universal.utils.impl.IBANServiceImpl
import com.nompay.banking_universal.utils.impl.SessionServiceImpl
import org.apache.coyote.BadRequestException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.util.UUID

@Service
class AccountServiceImpl(
  private val ibanService: IBANServiceImpl,

  private val accountRepository: AccountEntityRepository,

  private val userRepository: UserEntityRepository,

  private val externalAccountService: ExternalAccountServiceImpl,

  private val sessionService: SessionServiceImpl,

  private val transactionService: TransactionServiceImpl,

  private val logger: Logger = LoggerFactory.getLogger(AccountServiceImpl::class.java),

  private val externalTransferSourceCheck: HashMap<String, Set<TransactionTypes>> = hashMapOf<String, Set<TransactionTypes>>(
    "fromExternal" to setOf<TransactionTypes>(TransactionTypes.EXTERNALTOMERCHANT, TransactionTypes.EXTERNALTOUSER),
    "toExternal" to setOf<TransactionTypes>(TransactionTypes.MERCHANTTOEXTERNAL, TransactionTypes.USERTOEXTERNAL)
  )
) : AccountService {

  override fun createAccount(createAccountDto: CreateAccountDto): AccountEntity {
    val ownerUser = userRepository.findUserByEmailIgnoreCase(createAccountDto.email)
      ?: throw BadRequestException("No User with such email found");
    val userAccounts = accountRepository.getAccountsByEmail(createAccountDto.email);

    if (!userAccounts.isNullOrEmpty()) {
      val sameAccountForCurrency = userAccounts.filter { it.currency == createAccountDto.currency };
      if (sameAccountForCurrency.size > 2) {
        logger.error(
          "Cannot create three accounts with the " +
              "same currency of ${createAccountDto.currency}"
        )
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

  override fun findAccountsToTransfer(findAccountsToTransferDto: FindAccountsToTransferDto): List<AccountEntity> {
    TODO("Not yet implemented")
  }

  @Transactional
  override fun transferFunds(transferFundsDto: TransferFundsDto): String {
    val (amount, currency, fromEmail, toEmail, fromMerchant, toMerchant,
      fromExternal, toExternal, fromAccountNumber, toAccountNumber, transactionType, transferDescription) = transferFundsDto

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
      logger.error("Currency mismatch. Conversions are not supported.")
      throw IllegalStateException("Currency mismatch. Conversions are not supported.")
    }

    if (fromAccount.id == toAccount.id) {
      logger.error("Cannot transfer funds to the same account.")
      throw IllegalStateException("Cannot transfer funds to the same account.")
    }

    if (fromAccount.balance.compareTo(amount) < 0) {
      logger.error("Insufficient balance.")
      throw IllegalStateException("Insufficient balance.")
    }

    if (amount < BigDecimal("0.01")) {
      logger.error("Cannot Transfer such funds")
      throw IllegalStateException("Cannot Transfer such funds")
    }

    val transfersFromExternal = this.externalTransferSourceCheck["fromExternal"] ?: emptySet()

    if (!transfersFromExternal.contains(transactionType)) { // Checks whether funds are coming from the external source
      fromAccount.balance = fromAccount.balance.subtract(amount)
    }

    val transfersToExternal = this.externalTransferSourceCheck["toExternal"] ?: emptySet()

    if (!transfersToExternal.contains(transactionType)) { // checks whether funds are going to teh external source
      toAccount.balance = toAccount.balance.add(amount)
    }

    this.accountRepository.save(fromAccount)
    this.accountRepository.save(toAccount)

    val transaction = CreateTransactionDto.Builder()
      .withFromUser(fromAccount.ownerUser!!)
      .withToUser(fromAccount.ownerUser!!)
      .withFromEmail(fromAccount.email)
      .withToEmail(toAccount.email)
      .withFromMerchant(fromAccount.ownerMerchant)
      .withToMerchant(toAccount.ownerMerchant)
      .withFromAccount(fromAccount)
      .withFromExternal(fromExternal)
      .withToExternal(toExternal)
      .withTransactionType(transactionType)
      .withToAccount(toAccount)
      .withTransactionId(UUID.randomUUID().toString())
      .withCurrency(currency)
      .withAmount(amount)
      .withStatus(TransactionStatuses.SUCCESS)
      .build()
    this.transactionService.createTransaction(transaction)
    return "Funds transferred successfully."
  }

  override fun transferFundsInternally(transferFundInternallyDto: TransferFundsInternallyDto): String {
    TODO("Not yet implemented")
  }

  override fun transferFundsInternallyWithDiffCurrency(): String {
    TODO("Not yet implemented")
  }
}