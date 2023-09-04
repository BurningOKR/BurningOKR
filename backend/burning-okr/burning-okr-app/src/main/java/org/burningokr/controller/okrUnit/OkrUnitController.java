package org.burningokr.controller.okrUnit;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.burningokr.annotation.RestApiController;
import org.burningokr.dto.okr.KeyResultDto;
import org.burningokr.dto.okr.ObjectiveDto;
import org.burningokr.dto.okrUnit.OkrChildUnitDto;
import org.burningokr.dto.okrUnit.OkrCompanyDto;
import org.burningokr.dto.okrUnit.OkrUnitSchemaDto;
import org.burningokr.mapper.okr.KeyResultMapper;
import org.burningokr.mapper.okr.ObjectiveMapper;
import org.burningokr.mapper.okrUnit.OkrBranchSchemaMapper;
import org.burningokr.mapper.okrUnit.OkrChildUnitMapper;
import org.burningokr.mapper.okrUnit.OkrCompanyMapper;
import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.okrUnits.OkrChildUnit;
import org.burningokr.model.okrUnits.OkrCompany;
import org.burningokr.service.okr.KeyResultService;
import org.burningokr.service.okr.ObjectiveService;
import org.burningokr.service.okrUnit.OkrChildUnitService;
import org.burningokr.service.okrUnitUtil.EntityCrawlerService;
import org.burningokr.service.security.authenticationUserContext.AuthenticationUserContextService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.UUID;

@RestApiController
@RequiredArgsConstructor
@Slf4j
public class OkrUnitController {

  private final OkrChildUnitService<OkrChildUnit> okrChildUnitService;
  private final KeyResultService keyResultService;
  private final EntityCrawlerService entityCrawlerService;
  private final OkrBranchSchemaMapper okrBranchSchemaMapper;
  private final ObjectiveMapper objectiveMapper;
  private final OkrCompanyMapper okrCompanyMapper;
  private final KeyResultMapper keyResultMapper;
  private final OkrChildUnitMapper okrChildUnitMapper;
  private final ObjectiveService objectiveService;
  private final AuthenticationUserContextService authenticationUserContextService;

  @GetMapping("/units/{unitId}")
  public ResponseEntity<OkrChildUnitDto> getUnitByUnitId(
    @PathVariable long unitId
  ) {
    OkrChildUnit unit = okrChildUnitService.findById(unitId);
    return ResponseEntity.ok(okrChildUnitMapper.mapEntityToDto(unit));
  }

  /**
   * API Endpoint to get the okrUnit of all the related Departments.
   *
   * @param unitId a long value
   * @return a {@link ResponseEntity} ok with a {@link Collection} of Department Schema
   */
  @GetMapping("/units/{unitId}/schema")
  public ResponseEntity<Collection<OkrUnitSchemaDto>> getDepartmentSchemaOfDepartment(
    @PathVariable long unitId
  ) {
    OkrChildUnit childUnit = okrChildUnitService.findById(unitId);
    OkrCompany parentOkrCompany = entityCrawlerService.getCompanyOfUnit(childUnit);
    UUID currentUserId = authenticationUserContextService.getAuthenticatedUser().getId();
    return ResponseEntity.ok(
      okrBranchSchemaMapper.mapOkrChildUnitListToOkrChildUnitSchemaList(
        parentOkrCompany.getOkrChildUnits(), currentUserId));
  }

  /**
   * API Endpoint to get all Objectives of a OkrDepartment.
   *
   * @param unitId a long value
   * @return a {@link ResponseEntity} ok with a {@link Collection} of Objectives
   */
  @GetMapping("/units/{unitId}/objectives")
  public ResponseEntity<Collection<ObjectiveDto>> getObjectivesOfUnit(
    @PathVariable long unitId
  ) {
    Collection<Objective> objectives = okrChildUnitService.findObjectivesOfUnit(unitId);
    return ResponseEntity.ok(objectiveMapper.mapEntitiesToDtos(objectives));
  }

  /**
   * API Endpoint to get all Key Results of an OkrDepartment.
   *
   * @param unitId a long value
   * @return a {@link ResponseEntity} ok with a {@link Collection} of KeyResult
   */
  @GetMapping("/units/{unitId}/keyresults")
  public ResponseEntity<Collection<KeyResultDto>> getKeyResultsOfUnit(
    @PathVariable long unitId
  ) {
    Collection<KeyResult> keyResults = keyResultService.findKeyResultsOfUnit(unitId);
    return ResponseEntity.ok(keyResultMapper.mapEntitiesToDtos(keyResults));
  }

  /**
   * API Endpoint to get the parent-OkrCompany of a OkrDepartment.
   *
   * @param unitId a long value
   * @return a {@link ResponseEntity} ok with the parent-OkrCompany
   */
  @GetMapping("/units/{unitId}/company")
  public ResponseEntity<OkrCompanyDto> getParentCompanyOfUnit(
    @PathVariable long unitId
  ) {
    OkrChildUnit unit = okrChildUnitService.findById(unitId);
    OkrCompany parentOkrCompany = entityCrawlerService.getCompanyOfUnit(unit);
    return ResponseEntity.ok(okrCompanyMapper.mapEntityToDto(parentOkrCompany));
  }

  @PutMapping(value = "/units/{unitId}", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<OkrChildUnitDto> updateUnit(
    @PathVariable long unitId,
    @RequestBody
    @Valid OkrChildUnitDto okrChildUnitDto
  ) {
    okrChildUnitDto.setOkrUnitId(unitId);
    OkrChildUnit childUnit = okrChildUnitMapper.mapDtoToEntity(okrChildUnitDto);

    childUnit = okrChildUnitService.updateUnit(childUnit);
    return ResponseEntity.ok(okrChildUnitMapper.mapEntityToDto(childUnit));

  }

  @PostMapping("/units/{unitId}/objectives")
  @PreAuthorize("@childUnitAuthorizationService.hasMemberPrivilegesForChildUnit(#unitId)")
  public ResponseEntity<ObjectiveDto> addObjectiveToDepartment(
    @PathVariable long unitId,
    @Valid
    @RequestBody
    ObjectiveDto objectiveDto
  ) {
    Objective objective = objectiveMapper.mapDtoToEntity(objectiveDto);
    objective.setId(null);
    objective = objectiveService.createObjective(unitId, objective);
    return ResponseEntity.ok(objectiveMapper.mapEntityToDto(objective));
  }

  @DeleteMapping("/units/{unitId}")
  public ResponseEntity<Object> deleteUnit(
    @PathVariable long unitId
  ) {
    okrChildUnitService.deleteChildUnit(unitId);
    return ResponseEntity.ok().build();
  }
}
