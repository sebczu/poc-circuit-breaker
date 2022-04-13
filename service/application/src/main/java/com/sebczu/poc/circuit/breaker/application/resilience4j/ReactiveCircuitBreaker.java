package com.sebczu.poc.circuit.breaker.application.resilience4j;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ReactiveCircuitBreaker {

  private final CircuitBreaker circuitBreaker;

  public <T> Mono<T> apply(Mono<T> provider) {
    return provider
      .transformDeferred(CircuitBreakerOperator.of(circuitBreaker));
  }

  public CircuitBreaker.State getState() {
    return circuitBreaker.getState();
  }

  public void reset() {
    circuitBreaker.reset();
  }

}
