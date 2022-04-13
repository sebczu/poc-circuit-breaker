package com.sebczu.poc.circuit.breaker.application.domain;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
public class SlowProvider {

  public Mono<String> apply() {
    return Mono.just("test").delayElement(Duration.ofMillis(300));
  }

}
