package org.burningokr.mapper.log;

import org.burningokr.dto.log.FrontendLogDto;
import org.burningokr.model.log.FrontendLog;
import org.burningokr.model.log.FrontendLogLevel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FrontendLogMapperTest {

  private FrontendLog frontendLog;
  private FrontendLogDto frontendLogDto;
  private FrontendLogMapper frontendLogMapper;

  @BeforeEach
  public void setup() {
    this.frontendLog = new FrontendLog();
    this.frontendLogDto = new FrontendLogDto();
    this.frontendLogMapper = new FrontendLogMapper();
  }

  @Test
  public void mapDtoToEntity_shouldMapCorrectly() {
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
  public void mapDtosToEntities_shouldMapCorrectly() {
    FrontendLogDto frontendLogDto1 =
            new FrontendLogDto(1L, String.valueOf(FrontendLogLevel.INFO.getLevelId()), "2019-11-21T09:35:05", "main.ts", "5", "Failed to build file");
    FrontendLogDto frontendLogDto2 =
            new FrontendLogDto(2L, String.valueOf(FrontendLogLevel.ERROR.getLevelId()), "2020-11-22T03:36:05", "main.ts", "4", "Joining new entry");
    Collection<FrontendLogDto> frontendLogDtos = Arrays.asList(frontendLogDto1, frontendLogDto2);

    Collection<FrontendLog> frontendLogs = frontendLogMapper.mapDtosToEntities(frontendLogDtos);

    assertEquals(frontendLogs.size(), frontendLogDtos.size());
    assertFrontendDtoWithLog(frontendLogDto1, (FrontendLog) frontendLogs.toArray()[0]);
    assertFrontendDtoWithLog(frontendLogDto2, (FrontendLog) frontendLogs.toArray()[1]);
  }

  @Test
  public void mapEntityToDto_shouldMapEntityToDtoCorrectly() {
    frontendLog.setId(1L);
    frontendLog.setLevel(FrontendLogLevel.INFO.name());
    frontendLog.setTimestamp(LocalDateTime.of(2018, 3, 14, 15, 56, 0));
    frontendLog.setFileName("main.ts");
    frontendLog.setLineNumber("1");
    frontendLog.setMessage("Test message");
    frontendLogDto = frontendLogMapper.mapEntityToDto(frontendLog);
    assertFrontendLogWithDto(frontendLog, frontendLogDto);
  }

  @Test
  public void mapEntitiesToDtos_shouldMapDtosToEntitiesCorrectly() {
    FrontendLog frontendLog1 =
      new FrontendLog(1L, "ERROR", LocalDateTime.of(2018, 3, 14, 15, 56, 0), "main.ts", "5", "Failed to build file");
    FrontendLog frontendLog2 =
      new FrontendLog(2L, "DEBUG", LocalDateTime.of(2018, 3, 14, 15, 56, 1), "main.ts", "4", "Joining new entry");
    Collection<FrontendLog> frontendLogs = Arrays.asList(frontendLog1, frontendLog2);

    Collection<FrontendLogDto> frontendLogDtos = frontendLogMapper.mapEntitiesToDtos(frontendLogs);

    assertEquals(frontendLogs.size(), frontendLogDtos.size());
    assertFrontendLogWithDto(frontendLog1, (FrontendLogDto) frontendLogDtos.toArray()[0]);
    assertFrontendLogWithDto(frontendLog2, (FrontendLogDto) frontendLogDtos.toArray()[1]);
  }



  private void assertFrontendLogWithDto(FrontendLog frontendLog, FrontendLogDto frontendLogDto) {
    assertEquals(frontendLog.getId(), frontendLogDto.getId());
    assertEquals(
      frontendLog.getLevel(),
      FrontendLogLevel.getLevelByIdentifier(frontendLogDto.getLevel()).name()
    );
    assertEquals(
      frontendLog.getTimestamp().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
      frontendLogDto.getTimestamp()
    );
    assertEquals(frontendLog.getFileName(), frontendLogDto.getFileName());
    assertEquals(frontendLog.getLineNumber(), frontendLogDto.getLineNumber());
    assertEquals(frontendLog.getMessage(), frontendLogDto.getMessage());
  }

  private void assertFrontendDtoWithLog(FrontendLogDto frontendLogDto, FrontendLog frontendLog) {
    assertEquals(frontendLogDto.getId(), frontendLog.getId());
    assertEquals(
            FrontendLogLevel.getLevelByIdentifier(frontendLogDto.getLevel()).name(),
            frontendLog.getLevel()
    );
    assertEquals(
            frontendLogDto.getTimestamp(),
            frontendLog.getTimestamp().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)

    );
    assertEquals(frontendLogDto.getFileName(), frontendLog.getFileName());
    assertEquals(frontendLogDto.getLineNumber(), frontendLog.getLineNumber());
    assertEquals(frontendLogDto.getMessage(), frontendLog.getMessage());
  }
}
