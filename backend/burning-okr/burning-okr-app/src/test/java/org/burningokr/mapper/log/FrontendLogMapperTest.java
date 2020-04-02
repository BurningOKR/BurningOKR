package org.burningokr.mapper.log;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collection;
import org.burningokr.dto.log.FrontendLogDto;
import org.burningokr.model.log.FrontendLog;
import org.burningokr.model.log.FrontendLogLevel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FrontendLogMapperTest {

  private FrontendLog frontendLog;
  private FrontendLogDto frontendLogDto;
  private FrontendLogMapper frontendLogMapper;

  @Before
  public void setup() {
    this.frontendLog = new FrontendLog();
    this.frontendLogDto = new FrontendLogDto();
    this.frontendLogMapper = new FrontendLogMapper();
  }

  @Test
  public void test_mapDtoToEntityShouldMapCorrectly() {
    frontendLogDto.setId(1L);
    frontendLogDto.setLevel(String.valueOf(FrontendLogLevel.INFO.getLevelId()));
    frontendLogDto.setTimestamp("2019-11-21T09:35:05");
    frontendLogDto.setFileName("main.ts");
    frontendLogDto.setLineNumber("1");
    frontendLogDto.setMessage("Test message");
    frontendLog = frontendLogMapper.mapDtoToEntity(frontendLogDto);
    assertFrontendLogWithDto(frontendLog, frontendLogDto);
  }

  @Test
  public void test_mapEntityToDtoShouldMapCorrectly() {
    frontendLog.setId(1L);
    frontendLog.setLevel(FrontendLogLevel.INFO.name());
    frontendLog.setTimestamp(LocalDateTime.now());
    frontendLog.setFileName("main.ts");
    frontendLog.setLineNumber("1");
    frontendLog.setMessage("Test message");
    frontendLogDto = frontendLogMapper.mapEntityToDto(frontendLog);
    assertFrontendLogWithDto(frontendLog, frontendLogDto);
  }

  @Test
  public void test_mapEntitiesToDtosShouldMapCorrectly() {
    FrontendLog frontendLog1 =
        new FrontendLog(1L, "ERROR", LocalDateTime.now(), "main.ts", "5", "Failed to build file");
    FrontendLog frontendLog2 =
        new FrontendLog(2L, "DEBUG", LocalDateTime.now(), "main.ts", "4", "Joining new entry");
    Collection<FrontendLog> frontendLogs = Arrays.asList(frontendLog1, frontendLog2);

    Collection<FrontendLogDto> frontendLogDtos = frontendLogMapper.mapEntitiesToDtos(frontendLogs);

    Assert.assertEquals(frontendLogs.size(), frontendLogDtos.size());
    assertFrontendLogWithDto(frontendLog1, (FrontendLogDto) frontendLogDtos.toArray()[0]);
    assertFrontendLogWithDto(frontendLog2, (FrontendLogDto) frontendLogDtos.toArray()[1]);
  }

  private void assertFrontendLogWithDto(FrontendLog frontendLog, FrontendLogDto frontendLogDto) {
    Assert.assertEquals(frontendLog.getId(), frontendLogDto.getId());
    Assert.assertEquals(
        frontendLog.getLevel(),
        FrontendLogLevel.getLevelByIdentifier(frontendLogDto.getLevel()).name());
    Assert.assertEquals(
        frontendLog.getTimestamp().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
        frontendLogDto.getTimestamp());
    Assert.assertEquals(frontendLog.getFileName(), frontendLogDto.getFileName());
    Assert.assertEquals(frontendLog.getLineNumber(), frontendLogDto.getLineNumber());
    Assert.assertEquals(frontendLog.getMessage(), frontendLogDto.getMessage());
  }
}
