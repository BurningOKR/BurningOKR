package org.burningokr.controller.cycles;

import org.burningokr.annotation.RestApiController;
import org.burningokr.dto.cycle.CycleDto;
import org.burningokr.dto.validators.CycleDtoValidator;
import org.burningokr.exceptions.InvalidDtoException;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.cycles.Cycle;
import org.burningokr.service.cycle.CycleService;
import org.burningokr.service.security.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@RestApiController
public class CycleCloneController {

  private CycleService cycleService;
  private CycleDtoValidator cycleDtoValidator;
  private DataMapper<Cycle, CycleDto> cycleMapper;
  private AuthorizationService authorizationService;

  /**
   * Initialize CycleCloneController.
   *
   * @param cycleService a {@link CycleService} object
   * @param cycleDtoValidator a {@link CycleDtoValidator} object
   * @param cycleMapper a {@link DataMapper} object with {@link Cycle} and {@link CycleDto}
   * @param authorizationService an {@link AuthorizationService} object
   */
  @Autowired
  public CycleCloneController(
      CycleService cycleService,
      CycleDtoValidator cycleDtoValidator,
      DataMapper<Cycle, CycleDto> cycleMapper,
      AuthorizationService authorizationService) {
    this.cycleService = cycleService;
    this.cycleDtoValidator = cycleDtoValidator;
    this.cycleMapper = cycleMapper;
    this.authorizationService = authorizationService;
  }

  /**
   * API Endpoint to clone a Cycle from an existing Cycle.
   *
   * @param cycleId a long value
   * @param cycleDto a {@link CycleDto} object
   * @return a {@link ResponseEntity} ok with a Cycle
   * @throws InvalidDtoException if Cycle is invalid
   */
  @PostMapping("/clonecycle/{cycleId}")
  @PreAuthorize("@authorizationService.isAdmin()")
  public ResponseEntity<CycleDto> cloneCycleFromCycleId(
      @PathVariable long cycleId, @RequestBody @Valid CycleDto cycleDto)
      throws InvalidDtoException {
    cycleDtoValidator.validateCycleDto(cycleDto);
    Cycle newCycle = cycleMapper.mapDtoToEntity(cycleDto);
    newCycle.setId(null);
    newCycle = this.cycleService.defineCycle(cycleId, newCycle);
    return ResponseEntity.ok(cycleMapper.mapEntityToDto(newCycle));
  }
}
