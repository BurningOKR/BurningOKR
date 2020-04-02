package org.burningokr.service.log;

import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import org.burningokr.model.log.FrontendLog;
import org.burningokr.repositories.log.FrontendLoggerRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FrontendLoggerServiceTest {

  @Mock private FrontendLoggerRepository frontendLoggerRepository;

  @InjectMocks private FrontendLoggerService frontendLoggerService;

  @Test
  public void test_findByIdShouldFindCorrectly() {
    FrontendLog expectedFrontendLog =
        new FrontendLog(1L, "ERROR", LocalDateTime.now(), "main.ts", "5", "Failed to build file");
    when(frontendLoggerRepository.findByIdOrThrow(expectedFrontendLog.getId()))
        .thenReturn(expectedFrontendLog);

    FrontendLog frontendLog = frontendLoggerService.findById(expectedFrontendLog.getId());

    Assert.assertEquals(expectedFrontendLog, frontendLog);
  }

  @Test(expected = NullPointerException.class)
  public void test_findByIdShouldThrowNullPointerException() {
    FrontendLog expectedFrontendLog =
        new FrontendLog(1L, "ERROR", LocalDateTime.now(), "main.ts", "5", "Failed to build file");
    when(frontendLoggerRepository.findByIdOrThrow(expectedFrontendLog.getId()))
        .thenThrow(NullPointerException.class);

    frontendLoggerService.findById(expectedFrontendLog.getId());
  }

  @Test
  public void test_createFrontendLogShouldSaveCorrectly() {
    FrontendLog expectedFrontendLog =
        new FrontendLog(1L, "ERROR", LocalDateTime.now(), "main.ts", "5", "Failed to build file");
    when(this.frontendLoggerRepository.save(expectedFrontendLog)).thenReturn(expectedFrontendLog);

    FrontendLog frontendLog = frontendLoggerRepository.save(expectedFrontendLog);

    Assert.assertEquals(frontendLog, expectedFrontendLog);
  }
}
