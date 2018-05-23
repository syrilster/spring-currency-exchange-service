package com.springboot.microservices.currencyexchangeservice;

import com.springboot.microservices.currencyexchangeservice.bean.ExchangeValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * Created by Syril on 23-05-2018.
 */
@RestController
public class CurrencyExchangeController {

    @GetMapping("/currency-exchange/from/{from}/to/{to}")
    public ExchangeValue retrieveExchangeValue(@PathVariable String from, @PathVariable String to) {
        return new ExchangeValue(1L, from, to, new BigDecimal(61));
    }
}
