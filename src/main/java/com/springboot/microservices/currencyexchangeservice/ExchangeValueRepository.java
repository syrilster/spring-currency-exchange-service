package com.springboot.microservices.currencyexchangeservice;

import com.springboot.microservices.currencyexchangeservice.jpaApproach.JPAExchangeValue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExchangeValueRepository extends JpaRepository<JPAExchangeValue, Long> {
    //JPA automatically forms a query using from and to property of ExchangeValue as where clause.
    JPAExchangeValue findByFromAndTo(String from, String to);
}
