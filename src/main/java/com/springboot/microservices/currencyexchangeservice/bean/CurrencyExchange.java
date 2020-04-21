package com.springboot.microservices.currencyexchangeservice.bean;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.Map;

@Getter
public class CurrencyExchange {
    private String base;
    private Map<String, BigDecimal> rates;
}
