package com.sebczu.poc.circuit.breaker.application.domain;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ErrorProvider {

  public Mono<String> apply() {
    return Mono.error(new RuntimeException("error"));
  }

}
