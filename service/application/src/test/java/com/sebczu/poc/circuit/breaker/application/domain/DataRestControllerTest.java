package com.sebczu.poc.circuit.breaker.application.domain;

import com.sebczu.poc.circuit.breaker.application.Application;
import com.sebczu.poc.circuit.breaker.application.resilience4j.ReactiveCircuitBreaker;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.stream.IntStream;

import static io.github.resilience4j.circuitbreaker.CircuitBreaker.State.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DataRestControllerTest {

  private static final String URL_OK = "/ok";
  private static final String URL_ERROR = "/error";

  @Autowired
  private WebTestClient webClient;

  @Autowired
  private ReactiveCircuitBreaker circuitBreaker;

  @BeforeEach
  public void reset() {
    circuitBreaker.reset();
  }

  @Test
  void whenThresholdExceed_ShouldChangeStateToOpen() {
    sendOk(1);
    sendError(2);

    assertThat(circuitBreaker.getState()).isEqualTo(OPEN);
  }

  @Test
  void whenThresholdNotExceed_ShouldNotChangeState() {
    sendOk(2);
    sendError(1);

    assertThat(circuitBreaker.getState()).isEqualTo(CLOSED);
  }

  @Test
  void whenDurationIsElapsedInOpenState_ShouldChangeStateToHalfOpen() {
    sendError(3);
    sleep(2100);
    sendOk(1);

    assertThat(circuitBreaker.getState()).isEqualTo(HALF_OPEN);
  }

  @Test
  void whenThresholdExceededInHalfOpenState_ShouldChangeStateToOpen() {
    sendError(3);
    sleep(2100);
    sendOk(1);
    sendError(2);

    assertThat(circuitBreaker.getState()).isEqualTo(OPEN);
  }

  @Test
  void whenThresholdNotExceededInHalfOpenState_ShouldChangeStateToClosed() {
    sendError(3);
    sleep(2100);
    sendOk(2);
    sendError(1);

    assertThat(circuitBreaker.getState()).isEqualTo(CLOSED);
  }

  private void sendOk(int count) {
    IntStream.range(0, count).forEach(i -> {
      webClient.get()
        .uri(URL_OK)
        .exchange()
        .expectStatus().isOk();
    });
  }

  private void sendError(int count) {
    IntStream.range(0, count).forEach(i -> {
      webClient.get()
        .uri(URL_ERROR)
        .exchange()
        .expectStatus().is5xxServerError();
    });
  }

  @SneakyThrows
  private void sleep(long millis) {
    Thread.sleep(millis);
  }

}
