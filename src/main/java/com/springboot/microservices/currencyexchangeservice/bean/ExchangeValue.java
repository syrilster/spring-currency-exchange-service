package com.springboot.microservices.currencyexchangeservice.bean;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Getter
@Setter
@Component
public class ExchangeValue {
    private String from;
    private String to;
    private BigDecimal conversionMultiple;
}
