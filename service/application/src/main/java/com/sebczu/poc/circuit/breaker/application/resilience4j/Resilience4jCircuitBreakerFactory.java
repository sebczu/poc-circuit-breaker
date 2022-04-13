package com.sebczu.poc.circuit.breaker.application.resilience4j;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;

import static io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.SlidingWindowType.COUNT_BASED;

@Component
public class Resilience4jCircuitBreakerFactory {

  private final CircuitBreakerRegistry registry;

  public Resilience4jCircuitBreakerFactory() {
    CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
      .slidingWindowType(COUNT_BASED)
      .failureRateThreshold(50)
      .slowCallRateThreshold(50)
      .slowCallDurationThreshold(Duration.ofMillis(200))
      .waitDurationInOpenState(Duration.ofSeconds(2))
      .permittedNumberOfCallsInHalfOpenState(3)
      .minimumNumberOfCalls(3)
      .slidingWindowSize(5)
      .build();

    registry = CircuitBreakerRegistry.of(circuitBreakerConfig);
  }

  public ReactiveCircuitBreaker circuitBreaker() {
    CircuitBreaker circuitBreaker = registry.circuitBreaker("name");
    return new ReactiveCircuitBreaker(circuitBreaker);
  }

}
