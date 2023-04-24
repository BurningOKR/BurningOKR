package org.burningokr.controller.cycles;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.burningokr.annotation.RestApiController;
import org.burningokr.dto.cycle.CycleDto;
import org.burningokr.dto.validators.CycleDtoValidator;
import org.burningokr.exceptions.InvalidDtoException;
import org.burningokr.mapper.CycleMapper;
import org.burningokr.model.cycles.Cycle;
import org.burningokr.service.cycle.CycleService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;

@RestApiController
@RequiredArgsConstructor
public class CycleController {

  private final CycleService cycleService;
  private final CycleDtoValidator cycleDtoValidator;
  private final CycleMapper cycleMapper;

  /**
   * API Endpoint to get all Cycles.
   *
   * @return a {@link ResponseEntity} ok with {@link Collection} of Cycles
   */
  @GetMapping("/cycles")
  public ResponseEntity<Collection<CycleDto>> getAllCycles() {
    Collection<CycleDto> cycleDtos = new ArrayList<>();
    for (Cycle cycle : cycleService.getAllCycles()) {
      cycleDtos.add(cycleMapper.mapEntityToDto(cycle));
    }
    return ResponseEntity.ok(cycleDtos);
  }

  @GetMapping("/cycles/{cycleId}")
  public ResponseEntity<CycleDto> getCycleById(
    @PathVariable Long cycleId
  ) {
    Cycle cycle = cycleService.findById(cycleId);
    return ResponseEntity.ok(cycleMapper.mapEntityToDto(cycle));
  }

  /**
   * API Endpoint to update a Cycle.
   *
   * @param cycleId  a long value
   * @param cycleDto a {@link CycleDto} object
   * @return a {@link ResponseEntity} ok with a Cycle
   * @throws InvalidDtoException if Cycle is invalid
   */
  @PutMapping("/cycles/{cycleId}")
  @PreAuthorize("@authorizationService.isAdmin()")
  public ResponseEntity<CycleDto> updateCycleById(
    @PathVariable Long cycleId,
    @RequestBody
    @Valid
    CycleDto cycleDto
  )
    throws InvalidDtoException {
    cycleDtoValidator.validateCycleDto(cycleDto);
    Cycle cycle = cycleMapper.mapDtoToEntity(cycleDto);
    cycle.setId(cycleId);
    cycle = this.cycleService.updateCycle(cycle);
    return ResponseEntity.ok(cycleMapper.mapEntityToDto(cycle));
  }

  @DeleteMapping("/cycles/{cycleId}")
  public ResponseEntity<Boolean> deleteCycleById(
    @PathVariable Long cycleId
  )
    throws Exception {
    cycleService.deleteCycle(cycleId);
    return ResponseEntity.ok().build();
  }
}
