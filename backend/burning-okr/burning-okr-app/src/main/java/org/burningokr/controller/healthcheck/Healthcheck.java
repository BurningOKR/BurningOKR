package org.burningokr.controller.healthcheck;

import org.burningokr.annotation.RestApiController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestApiController
public class Healthcheck {

  @GetMapping("/isAlive")
  public ResponseEntity<Boolean> isAlive() {
    return ResponseEntity.ok(true);
  }
}
