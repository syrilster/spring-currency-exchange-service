package com.springboot.microservices.currencyexchangeservice;

import com.springboot.microservices.currencyexchangeservice.bean.ExchangeValue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExchangeValueRepository extends JpaRepository<ExchangeValue, Long> {
    //JPA automatically forms a query using from and to property of ExchangeValue as where clause.
    ExchangeValue findByFromAndTo(String from, String to);
}
