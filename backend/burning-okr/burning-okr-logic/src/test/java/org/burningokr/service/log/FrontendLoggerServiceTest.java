package org.burningokr.service.log;

import org.burningokr.model.log.FrontendLog;
import org.burningokr.repositories.log.FrontendLoggerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FrontendLoggerServiceTest {

  @Mock
  private FrontendLoggerRepository frontendLoggerRepository;

  @InjectMocks
  private FrontendLoggerService frontendLoggerService;

  @Test
  public void findById_shouldFindCorrectly() {
    FrontendLog expectedFrontendLog =
      new FrontendLog(1L, "ERROR", LocalDateTime.now(), "main.ts", "5", "Failed to build file");
    when(frontendLoggerRepository.findByIdOrThrow(expectedFrontendLog.getId()))
      .thenReturn(expectedFrontendLog);

    FrontendLog frontendLog = frontendLoggerService.findById(expectedFrontendLog.getId());

    assertEquals(expectedFrontendLog, frontendLog);
  }

  @Test()
  public void findById_shouldThrowNullPointerException() {
    FrontendLog expectedFrontendLog =
      new FrontendLog(1L, "ERROR", LocalDateTime.now(), "main.ts", "5", "Failed to build file");
    when(frontendLoggerRepository.findByIdOrThrow(expectedFrontendLog.getId()))
      .thenThrow(NullPointerException.class);
    assertThrows(NullPointerException.class, () -> frontendLoggerService.findById(expectedFrontendLog.getId()));
  }

  @Test
  public void createFrontendLog_shouldSaveCorrectly() {
    FrontendLog expectedFrontendLog =
      new FrontendLog(1L, "ERROR", LocalDateTime.now(), "main.ts", "5", "Failed to build file");
    when(this.frontendLoggerRepository.save(expectedFrontendLog)).thenReturn(expectedFrontendLog);

    FrontendLog frontendLog = frontendLoggerService.createFrontendLog(expectedFrontendLog);

    assertEquals(frontendLog, expectedFrontendLog);
  }
}
