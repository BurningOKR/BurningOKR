package org.burningokr.controller.demoWebsite;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.burningokr.annotation.RestApiController;
import org.burningokr.service.demoWebsite.DatabaseScheduleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestApiController
@RequiredArgsConstructor
public class DeleteTimerController {

  private final DatabaseScheduleService scheduleService;

  /**
   * Get the Date for the next database reset.
   *
   * @return
   */
  @GetMapping("/demo/reset")
  public ResponseEntity<Long> getNextResetTime() {
    return ResponseEntity.ok(scheduleService.getNextSchedule());
  }
}
