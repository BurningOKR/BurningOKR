package org.burningokr.controller.log;

import org.burningokr.annotation.RestApiController;
import org.burningokr.dto.log.FrontendLogDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.log.FrontendLog;
import org.burningokr.service.log.FrontendLoggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestApiController
public class FrontendLogController {

  private FrontendLoggerService frontendLoggerService;
  private DataMapper<FrontendLog, FrontendLogDto> frontEndLogMapper;

  @Autowired
  public FrontendLogController(
      FrontendLoggerService frontendLoggerService,
      DataMapper<FrontendLog, FrontendLogDto> frontEndLogMapper) {
    this.frontendLoggerService = frontendLoggerService;
    this.frontEndLogMapper = frontEndLogMapper;
  }

  /**
   * API Endpoint to add a new Frontend Log.
   *
   * @param frontendLogDto a {@link FrontendLogDto} object
   * @return a {@link ResponseEntity} ok with a FrontendLog
   */
  @PostMapping("/log")
  public ResponseEntity<FrontendLogDto> createNewFrontEndLog(
      @RequestBody FrontendLogDto frontendLogDto) {
    FrontendLog frontendLog = frontEndLogMapper.mapDtoToEntity(frontendLogDto);
    return ResponseEntity.ok(
        frontEndLogMapper.mapEntityToDto(frontendLoggerService.createFrontendLog(frontendLog)));
  }
}
