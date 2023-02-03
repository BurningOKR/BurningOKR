package org.burningokr.controller.okrUnit;

import lombok.RequiredArgsConstructor;
import org.burningokr.annotation.RestApiController;
import org.burningokr.dto.okr.KeyResultDto;
import org.burningokr.dto.okr.ObjectiveDto;
import org.burningokr.dto.okrUnit.OkrChildUnitDto;
import org.burningokr.dto.okrUnit.OkrCompanyDto;
import org.burningokr.dto.okrUnit.OkrUnitSchemaDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.mapper.okr.ObjectiveMapper;
import org.burningokr.mapper.okrUnit.OkrBranchSchemaMapper;
import org.burningokr.mapper.okrUnit.OkrCompanyMapper;
import org.burningokr.mapper.okrUnit.UnitMapperFactory;
import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.okrUnits.OkrChildUnit;
import org.burningokr.model.okrUnits.OkrCompany;
import org.burningokr.model.users.User;
import org.burningokr.service.okrUnit.OkrUnitService;
import org.burningokr.service.okrUnit.OkrUnitServiceFactory;
import org.burningokr.service.okrUnitUtil.EntityCrawlerService;
import org.burningokr.service.userhandling.UserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.UUID;

@RestApiController
@RequiredArgsConstructor
public class OkrUnitController {

  private final OkrUnitServiceFactory<OkrChildUnit> okrUnitServiceFactory;
  private final UnitMapperFactory mapperPicker;
  private final EntityCrawlerService entityCrawlerService;
  private final UserService userService;
  private final OkrBranchSchemaMapper okrBranchSchemaMapper;
  private final ObjectiveMapper objectiveMapper;
  private final OkrCompanyMapper okrCompanyMapper;
  private final DataMapper<KeyResult, KeyResultDto> keyResultMapper;

  @GetMapping("/units/{unitId}")
  public ResponseEntity<OkrChildUnitDto> getUnitByUnitId(@PathVariable long unitId) {
    OkrUnitService<OkrChildUnit> okrUnitService =
        okrUnitServiceFactory.getRoleServiceForDepartment(unitId);
    OkrChildUnit unit = okrUnitService.findById(unitId);
    DataMapper mapper = mapperPicker.getMapper(unit.getClass());
    return ResponseEntity.ok((OkrChildUnitDto) mapper.mapEntityToDto(unit));
  }

  /**
   * API Endpoint to get the okrUnit of all the related Departments.
   *
   * @param unitId a long value
   * @return a {@link ResponseEntity} ok with a {@link Collection} of Department Schema
   */
  @GetMapping("/units/{unitId}/schema")
  public ResponseEntity<Collection<OkrUnitSchemaDto>> getDepartmentSchemaOfDepartment(
      @PathVariable long unitId) {
    OkrUnitService<OkrChildUnit> okrUnitService =
        okrUnitServiceFactory.getRoleServiceForDepartment(unitId);
    OkrChildUnit childUnit = okrUnitService.findById(unitId);
    OkrCompany parentOkrCompany = entityCrawlerService.getCompanyOfUnit(childUnit);
    UUID currentUserId = userService.getCurrentUser().getId();
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
  public ResponseEntity<Collection<ObjectiveDto>> getObjectivesOfUnit(@PathVariable long unitId) {
    OkrUnitService<OkrChildUnit> okrUnitService =
        okrUnitServiceFactory.getRoleServiceForDepartment(unitId);
    Collection<Objective> objectives = okrUnitService.findObjectivesOfUnit(unitId);
    return ResponseEntity.ok(objectiveMapper.mapEntitiesToDtos(objectives));
  }

  /**
   * API Endpoint to get all Key Results of an OkrDepartment.
   *
   * @param unitId a long value
   * @return a {@link ResponseEntity} ok with a {@link Collection} of KeyResult
   */
  @GetMapping("/units/{unitId}/keyresults")
  public ResponseEntity<Collection<KeyResultDto>> getKeyResultsOfUnit(@PathVariable long unitId) {
    OkrUnitService<OkrChildUnit> okrUnitService =
        okrUnitServiceFactory.getRoleServiceForDepartment(unitId);
    Collection<KeyResult> keyResults = okrUnitService.findKeyResultsOfUnit(unitId);
    return ResponseEntity.ok(keyResultMapper.mapEntitiesToDtos(keyResults));
  }

  /**
   * API Endpoint to get the parent-OkrCompany of a OkrDepartment.
   *
   * @param unitId a long value
   * @return a {@link ResponseEntity} ok with the parent-OkrCompany
   */
  @GetMapping("/units/{unitId}/company")
  public ResponseEntity<OkrCompanyDto> getParentCompanyOfUnit(@PathVariable long unitId) {
    OkrUnitService<OkrChildUnit> okrUnitService =
        okrUnitServiceFactory.getRoleServiceForDepartment(unitId);
    OkrChildUnit unit = okrUnitService.findById(unitId);
    OkrCompany parentOkrCompany = entityCrawlerService.getCompanyOfUnit(unit);
    return ResponseEntity.ok(okrCompanyMapper.mapEntityToDto(parentOkrCompany));
  }

  @PutMapping(value = "/units/{unitId}", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<OkrChildUnitDto> updateUnit(
      @PathVariable long unitId, @RequestBody OkrChildUnitDto okrChildUnitDto, User user) {
    OkrUnitService<OkrChildUnit> okrUnitService =
        okrUnitServiceFactory.getRoleServiceForDepartment(unitId);

    OkrChildUnit childUnit = okrUnitService.findById(unitId);
    DataMapper mapper = mapperPicker.getMapper(childUnit.getClass());
    OkrChildUnit receivedUnit = (OkrChildUnit) mapper.mapDtoToEntity(okrChildUnitDto);

    receivedUnit.setId(unitId);

    OkrChildUnit updateUnit = okrUnitService.updateUnit(receivedUnit, user);

    return ResponseEntity.ok((OkrChildUnitDto) mapper.mapEntityToDto(updateUnit));
  }

  /**
   * API Endpoint to add an Objective to a okrUnit.
   *
   * @param unitId a long value
   * @param objectiveDto an {@link ObjectiveDto} object
   * @param user an {@link User} object
   * @return a {@link ResponseEntity} ok with the added objective
   */
  @PostMapping("/units/{unitId}/objectives")
  @PreAuthorize("@authorizationService.hasMemberPrivilegeForDepartment(#unitId)")
  public ResponseEntity<ObjectiveDto> addObjectiveToDepartment(
      @PathVariable long unitId, @Valid @RequestBody ObjectiveDto objectiveDto, User user) {
    OkrUnitService<OkrChildUnit> okrUnitService =
        okrUnitServiceFactory.getRoleServiceForDepartment(unitId);
    Objective objective = objectiveMapper.mapDtoToEntity(objectiveDto);
    objective.setId(null);
    objective = okrUnitService.createObjective(unitId, objective, user);
    return ResponseEntity.ok(objectiveMapper.mapEntityToDto(objective));
  }

  @DeleteMapping("/units/{unitId}")
  public ResponseEntity deleteUnit(@PathVariable long unitId, User user) {
    OkrUnitService<OkrChildUnit> okrUnitService =
        okrUnitServiceFactory.getRoleServiceForDepartment(unitId);
    okrUnitService.deleteUnit(unitId, user);
    return ResponseEntity.ok().build();
  }
}
