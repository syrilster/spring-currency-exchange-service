package com.springboot.microservices.currencyexchangeservice;

import com.springboot.microservices.currencyexchangeservice.bean.ExchangeValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * Created by Syril on 23-05-2018.
 */

//@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RestController
public class CurrencyExchangeController {
    Logger logger = LoggerFactory.getLogger(CurrencyExchangeController.class);

    @Autowired
    private Environment environment;
    @Autowired
    private ExchangeValueRepository repository;

    //@CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/currency-exchange/from/{from}/to/{to}")
    public ExchangeValue retrieveExchangeValue(@PathVariable String from, @PathVariable String to) {
        ExchangeValue exchangeValue = repository.findByFromAndTo(from, to);
        exchangeValue.setPort(Integer.valueOf(environment.getProperty("local.server.port")));
        logger.info("Exchange Value {} " + exchangeValue);
        return exchangeValue;
    }
}
