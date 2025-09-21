package com.nompay.banking_universal.utils.impl

import com.nompay.banking_universal.repositories.dto.external.CurrencyExchangeDto
import com.nompay.banking_universal.repositories.enums.Currencies
import com.nompay.banking_universal.utils.ExternalService
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.HttpServerErrorException
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import java.math.BigDecimal

@Component
class ExternalServiceImpl(
  // Average conversion rate to GEL to USD now.
  private val gelConversionRateFor1USD: BigDecimal = BigDecimal.valueOf(2.7356),

  @Value("\${service.fastForexBaseUrl}")
  private val fastForexBaseUrl: String,

  @Value("\${service.fastForexApiKey}")
  private val forexApikey: String,

) : ExternalService {

  private lateinit var fastForexGetMultiUrl: String

  @PostConstruct
  fun init() {
    this.fastForexGetMultiUrl = this.fastForexBaseUrl + "/fetch-multi"
  }

  /*
  * Retrieving present currency exchange rates through external service FastForex IO
  * For more info @see https://console.fastforex.io/
  * */
  override fun checkConversion(fromCurrency: Currencies, toCurrencies: List<Currencies>)
   : CurrencyExchangeDto {
    val restTemplate = RestTemplate()
    try {
      val uri = UriComponentsBuilder.fromUriString(this.fastForexGetMultiUrl)
        .queryParam("from", fromCurrency.name)
        .queryParam("to", toCurrencies.joinToString(","))
        .toUriString();

      val response = restTemplate.getForObject(uri, CurrencyExchangeDto::class.java)

      return response ?: throw IllegalStateException("Currency response is empty")
    } catch (e: HttpServerErrorException) {
      throw e;
    }
  }


}