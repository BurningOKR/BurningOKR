package org.burningokr.controller.cycles;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.burningokr.annotation.RestApiController;
import org.burningokr.dto.cycle.CycleDto;
import org.burningokr.dto.validators.CycleDtoValidator;
import org.burningokr.exceptions.InvalidDtoException;
import org.burningokr.mapper.cycle.CycleMapper;
import org.burningokr.model.cycles.Cycle;
import org.burningokr.service.cycle.CycleService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestApiController
@RequiredArgsConstructor
public class CycleCloneController {

  private final CycleService cycleService;
  private final CycleDtoValidator cycleDtoValidator;
  private final CycleMapper cycleMapper;

  /**
   * API Endpoint to clone a Cycle from an existing Cycle.
   *
   * @param cycleId  a long value
   * @param cycleDto a {@link CycleDto} object
   * @return a {@link ResponseEntity} ok with a Cycle
   * @throws InvalidDtoException if Cycle is invalid
   */
  @PostMapping("/clonecycle/{cycleId}")
  @PreAuthorize("@authorizationService.isAdmin()")
  public ResponseEntity<CycleDto> cloneCycleFromCycleId(@PathVariable long cycleId, @RequestBody @Valid CycleDto cycleDto) throws InvalidDtoException {
    cycleDtoValidator.validateCycleDto(cycleDto);
    Cycle newCycle = cycleMapper.mapDtoToEntity(cycleDto);
    newCycle.setId(null);
    newCycle = this.cycleService.defineCycle(cycleId, newCycle);
    return ResponseEntity.ok(cycleMapper.mapEntityToDto(newCycle));
  }
}
