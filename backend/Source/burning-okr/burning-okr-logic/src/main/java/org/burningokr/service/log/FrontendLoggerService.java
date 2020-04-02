package org.burningokr.service.log;

import org.burningokr.model.log.FrontendLog;
import org.burningokr.repositories.log.FrontendLoggerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FrontendLoggerService {

  private final Logger logger = LoggerFactory.getLogger(FrontendLoggerService.class);

  private final FrontendLoggerRepository frontendLoggerRepository;

  @Autowired
  public FrontendLoggerService(FrontendLoggerRepository frontendLoggerRepository) {
    this.frontendLoggerRepository = frontendLoggerRepository;
  }

  public FrontendLog findById(Long frontendLogId) {
    return frontendLoggerRepository.findByIdOrThrow(frontendLogId);
  }

  public FrontendLog createFrontendLog(FrontendLog frontendLog) {
    return this.frontendLoggerRepository.save(frontendLog);
  }
}
