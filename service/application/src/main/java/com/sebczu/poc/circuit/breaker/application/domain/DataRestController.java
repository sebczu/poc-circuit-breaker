package com.sebczu.poc.circuit.breaker.application.domain;

import com.sebczu.poc.circuit.breaker.application.resilience4j.ReactiveCircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping
public class DataRestController {

  private final ReactiveCircuitBreaker circuitBreaker;
  private final OkProvider okProvider;
  private final ErrorProvider errorProvider;
  private final SlowProvider slowProvider;

  @GetMapping("/ok")
  public Mono<String> ok() {
    return circuitBreaker.apply(okProvider.apply());
  }

  @GetMapping("/error")
  public Mono<String> error() {
    return circuitBreaker.apply(errorProvider.apply());
  }

  @GetMapping("/slow")
  public Mono<String> slow() {
    return circuitBreaker.apply(slowProvider.apply());
  }

}
