package com.sebczu.poc.circuit.breaker.application.domain;

import com.sebczu.poc.circuit.breaker.application.resilience4j.ReactiveCircuitBreaker;
import com.sebczu.poc.circuit.breaker.application.resilience4j.Resilience4jCircuitBreakerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DataConfiguration {

  private final Resilience4jCircuitBreakerFactory circuitBreakerFactory;

  @Bean
  public ReactiveCircuitBreaker circuitBreaker() {
    return circuitBreakerFactory.circuitBreaker();
  }

}
