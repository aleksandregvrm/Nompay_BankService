package com.nompay.banking_universal.services.impl

import com.nompay.banking_universal.repositories.dto.account.CreateAccountDto
import com.nompay.banking_universal.repositories.dto.account.CreateMerchantAccountDto
import com.nompay.banking_universal.repositories.dto.account.FindAccountsToTransferDto
import com.nompay.banking_universal.repositories.dto.account.TransferFundsDto
import com.nompay.banking_universal.repositories.dto.account.TransferFundsInternallyDto
import com.nompay.banking_universal.repositories.entities.AccountEntity
import com.nompay.banking_universal.repositories.entities.AccountEntityRepository
import com.nompay.banking_universal.repositories.entities.MerchantEntity
import com.nompay.banking_universal.repositories.entities.MerchantEntityRepository
import com.nompay.banking_universal.repositories.entities.TransactionEntity
import com.nompay.banking_universal.repositories.entities.UserEntity
import com.nompay.banking_universal.repositories.entities.UserEntityRepository
import com.nompay.banking_universal.repositories.enums.transactions.TransactionTypes
import com.nompay.banking_universal.services.AccountService
import com.nompay.banking_universal.services.MerchantService
import com.nompay.banking_universal.utils.impl.IBANServiceImpl
import com.nompay.banking_universal.utils.impl.SessionServiceImpl
import org.apache.coyote.BadRequestException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Service
class AccountServiceImpl(
  private val ibanService: IBANServiceImpl,

  private val accountRepository: AccountEntityRepository,

  private val userRepository: UserEntityRepository,

  private val merchantRepository: MerchantEntityRepository,

  private val merchantService: MerchantService,

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

  override fun createMerchantAccount(createMerchantAccountDto: CreateMerchantAccountDto): AccountEntity {
    val merchant = this.merchantService.getMerchantById(createMerchantAccountDto.merchantId!!)

    val merchantAccount = AccountEntity(
      name = createMerchantAccountDto.name!!,
      email = createMerchantAccountDto.email!!,
      ownerMerchant = merchant,
      currency = createMerchantAccountDto.currency!!,
      iban = this.ibanService.generateAccountIBAN(),
      accountType = createMerchantAccountDto.accountType
    )

    val merchantAccountEntity = this.accountRepository.save<AccountEntity>(merchantAccount)

    merchant.merchantAccounts.add(merchantAccountEntity) // Adding an account to the merchant accounts list

    this.merchantRepository.save<MerchantEntity>(merchant) // Updating the existing merchant with an updated bound accounts list

    return merchantAccountEntity
  }

  override fun findAccountsToTransfer(findAccountsToTransferDto: FindAccountsToTransferDto): List<AccountEntity> {
    TODO("Not yet implemented")
  }

  @Transactional
  override fun transferFunds(transferFundsDto: TransferFundsDto): TransactionEntity {
    val (amount, currency, fromEmail, toEmail, fromMerchant, toMerchant,
      fromExternal, toExternal, fromAccountNumber, toAccountNumber, transactionType, transferDescription) = transferFundsDto
    println(transferFundsDto)

    val transfersFromExternal = this.externalTransferSourceCheck["fromExternal"] ?: emptySet()
    val transfersToExternal = this.externalTransferSourceCheck["toExternal"] ?: emptySet()

    val fromAccount: AccountEntity? = when {
      transfersFromExternal.contains(transactionType) -> null
      !fromAccountNumber.isNullOrBlank() -> this.accountRepository.getAccountByIban(fromAccountNumber)
      !fromEmail.isNullOrBlank() && currency != null -> {
        this.accountRepository.getAccountsByEmail(fromEmail)?.firstOrNull { it.currency == currency }
      }

      else -> throw IllegalArgumentException("Source account details are required.")
    }

    if (!transfersFromExternal.contains(transactionType) && fromAccount == null) {
      throw IllegalStateException("Source account not found.")
    }

    val toAccount: AccountEntity? = when {
      transfersToExternal.contains(transactionType) -> null
      !toAccountNumber.isNullOrBlank() -> this.accountRepository.getAccountByIban(toAccountNumber)
      !toEmail.isNullOrBlank() && currency != null -> {
        this.accountRepository.getAccountsByEmail(toEmail)?.firstOrNull { it.currency == currency }
      }

      else -> throw IllegalArgumentException("Destination account details are required.")
    }

    if (!transfersToExternal.contains(transactionType) && toAccount == null) {
      throw IllegalStateException("Destination account not found.")
    }

    if (fromAccount != null && toAccount != null) {
      if (fromAccount.currency != toAccount.currency) {
        logger.error("Currency mismatch. Conversions are not supported.")
        throw IllegalStateException("Currency mismatch. Conversions are not supported.")
      }

      if (fromAccount.id == toAccount.id) {
        logger.error("Cannot transfer funds to the same account.")
        throw IllegalStateException("Cannot transfer funds to the same account.")
      }
    }

    if ((fromAccount?.currency ?: toAccount?.currency) != currency) {
      logger.error("Currency mismatch with the account.")
      throw IllegalStateException("Currency mismatch with the account")
    }

    if (amount < BigDecimal("0.01")) {
      logger.error("Cannot Transfer such funds")
      throw IllegalStateException("Cannot Transfer such funds")
    }

    if (fromAccount != null) {
      if (fromAccount.balance.compareTo(amount) < 0) {
        logger.error("Insufficient balance.")
        throw IllegalStateException("Insufficient balance.")
      }
      fromAccount.balance = fromAccount.balance.subtract(amount)
      this.accountRepository.save(fromAccount)
    }

    if (toAccount != null) {
      toAccount.balance = toAccount.balance.add(amount)
      this.accountRepository.save(toAccount)
    }

    val transaction =
      this.transactionService.constructTransactionPerTransactionType(
        transactionType,
        fromAccount,
        toAccount,
        transferFundsDto
      )
    println("printing transaction inhere..")
    println(transaction)
    val transactionEntity = this.transactionService.createTransaction(transaction)
    return transactionEntity
  }

  override fun transferFundsInternally(transferFundInternallyDto: TransferFundsInternallyDto): String {
    TODO("Not yet implemented")
  }

  override fun transferFundsInternallyWithDiffCurrency(): String {
    TODO("Not yet implemented")
  }
}