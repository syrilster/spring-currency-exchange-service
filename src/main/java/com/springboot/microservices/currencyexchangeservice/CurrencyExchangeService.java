package com.springboot.microservices.currencyexchangeservice;

import com.springboot.microservices.currencyexchangeservice.bean.CurrencyExchange;
import com.springboot.microservices.currencyexchangeservice.bean.ExchangeValue;
import com.springboot.microservices.currencyexchangeservice.restTemplate.RestTemplateConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@Service
public class CurrencyExchangeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CurrencyExchangeService.class);
    @Autowired
    private Environment environment;
    @Autowired
    private ExchangeValue exchangeValue;

    @Autowired
    RestTemplateConfig restTemplateConfig;

    private CurrencyExchange getExchangeRate() {
        final String uri = "https://openexchangerates.org/api/latest.json/";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(uri)
                .queryParam("app_id", environment.getProperty("currencyExchange.appId"));

        HttpEntity<?> entity = new HttpEntity<>(headers);
        LOGGER.info("Calling the openexchangerates API to get exchange rates");
        ResponseEntity<CurrencyExchange> exchangeResult = restTemplateConfig.getRestTemplate().exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                CurrencyExchange.class);
        LOGGER.info("Status code from the openexchangerates API " + exchangeResult.getStatusCode());
        return exchangeResult.getBody();
    }

    ExchangeValue getConversionMultiple(String fromCurrency, String toCurrency) {
        CurrencyExchange exchangeRateMap = getExchangeRate();
        BigDecimal conversionMultiple = BigDecimal.ZERO;
        BigDecimal exchangeRate;
        try {
            if (fromCurrency.equalsIgnoreCase("USD")) {
                exchangeRate = getRateForCurrency(exchangeRateMap.getRates(), toCurrency);
                conversionMultiple = exchangeRate;
            } else if (toCurrency.equalsIgnoreCase("USD")) {
                exchangeRate = getRateForCurrency(exchangeRateMap.getRates(), fromCurrency);
                conversionMultiple = BigDecimal.ONE.divide(exchangeRate, 5, RoundingMode.HALF_EVEN);
            } else {
                // FromCurrency to USD and then USD to toCurrency
                exchangeRate = getRateForCurrency(exchangeRateMap.getRates(), toCurrency);
                BigDecimal usdToFromCurrency = getRateForCurrency(exchangeRateMap.getRates(), fromCurrency);
                BigDecimal toCurrencyToUSD = BigDecimal.ONE.divide(exchangeRate, 5, RoundingMode.HALF_EVEN);
                BigDecimal foreignCurrencyFactor = BigDecimal.ONE.divide(usdToFromCurrency, 5, RoundingMode.HALF_EVEN);
                conversionMultiple = foreignCurrencyFactor.divide(toCurrencyToUSD, 5, RoundingMode.HALF_EVEN);
            }
        } catch (ArithmeticException are) {
            LOGGER.error("Error calculating conversion multiple " + are.getMessage(), are);
        }
        exchangeValue.setConversionMultiple(conversionMultiple);
        exchangeValue.setFrom(fromCurrency);
        exchangeValue.setTo(toCurrency);
        return exchangeValue;
    }

    private BigDecimal getRateForCurrency(Map<String, BigDecimal> exchangeRateMap, String currency) {
        return exchangeRateMap.get(currency) != null ? exchangeRateMap.get(currency) : BigDecimal.ZERO;
    }
}
