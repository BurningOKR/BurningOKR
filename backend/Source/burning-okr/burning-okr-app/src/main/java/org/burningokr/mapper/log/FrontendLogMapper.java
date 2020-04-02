package org.burningokr.mapper.log;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import javax.validation.constraints.NotNull;
import org.burningokr.dto.log.FrontendLogDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.log.FrontendLog;
import org.burningokr.model.log.FrontendLogLevel;
import org.springframework.stereotype.Service;

@Service
public class FrontendLogMapper implements DataMapper<FrontendLog, FrontendLogDto> {

  private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

  @Override
  public FrontendLog mapDtoToEntity(FrontendLogDto frontendLogDto) {
    FrontendLog frontendLog = new FrontendLog();
    frontendLog.setId(frontendLogDto.getId());
    frontendLog.setLevel(FrontendLogLevel.getLevelByIdentifier(frontendLogDto.getLevel()).name());
    frontendLog.setTimestamp(
        LocalDateTime.parse(
            formatStringToValidIsoWithoutMilliseconds(frontendLogDto.getTimestamp()),
            dateTimeFormatter));
    frontendLog.setFileName(frontendLogDto.getFileName());
    frontendLog.setLineNumber(frontendLogDto.getLineNumber());
    frontendLog.setMessage(frontendLogDto.getMessage());

    return frontendLog;
  }

  private String formatStringToValidIsoWithoutMilliseconds(
      @NotNull String potentialTimestampWithMilliseconds) {
    return potentialTimestampWithMilliseconds.contains(".")
        ? potentialTimestampWithMilliseconds.split("\\.")[0]
        : potentialTimestampWithMilliseconds;
  }

  @Override
  public FrontendLogDto mapEntityToDto(FrontendLog frontendLog) {
    FrontendLogDto frontendLogDto = new FrontendLogDto();
    frontendLogDto.setId(frontendLog.getId());
    frontendLogDto.setLevel(
        String.valueOf(FrontendLogLevel.valueOf(frontendLog.getLevel()).getLevelId()));
    frontendLogDto.setTimestamp(frontendLog.getTimestamp().format(dateTimeFormatter));
    frontendLogDto.setFileName(frontendLog.getFileName());
    frontendLogDto.setLineNumber(frontendLog.getLineNumber());
    frontendLogDto.setMessage(frontendLog.getMessage());

    return frontendLogDto;
  }

  @Override
  public Collection<FrontendLog> mapDtosToEntities(Collection<FrontendLogDto> input) {
    Collection<FrontendLog> frontendLogs = new ArrayList<>();
    input.forEach(frontendLogDto -> frontendLogs.add(mapDtoToEntity(frontendLogDto)));
    return frontendLogs;
  }

  @Override
  public Collection<FrontendLogDto> mapEntitiesToDtos(Collection<FrontendLog> frontendLogs) {
    Collection<FrontendLogDto> mappedFrontendLog = new ArrayList<>();
    frontendLogs.forEach(frontendLog -> mappedFrontendLog.add(mapEntityToDto(frontendLog)));
    return mappedFrontendLog;
  }
}
