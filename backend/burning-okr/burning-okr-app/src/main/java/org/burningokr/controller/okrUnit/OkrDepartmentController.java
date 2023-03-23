package org.burningokr.controller.okrUnit;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.burningokr.annotation.RestApiController;
import org.burningokr.dto.okr.ObjectiveDto;
import org.burningokr.dto.okr.OkrTopicDescriptionDto;
import org.burningokr.dto.okrUnit.OkrCompanyDto;
import org.burningokr.dto.okrUnit.OkrDepartmentDto;
import org.burningokr.dto.okrUnit.OkrUnitSchemaDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.mapper.okr.OkrTopicDescriptionMapper;
import org.burningokr.mapper.okrUnit.OkrBranchSchemaMapper;
import org.burningokr.mapper.okrUnit.OkrCompanyMapper;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.okr.OkrTopicDescription;
import org.burningokr.model.okrUnits.OkrCompany;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.burningokr.model.users.IUser;
import org.burningokr.service.exceptions.DuplicateTeamMemberException;
import org.burningokr.service.okr.ObjectiveService;
import org.burningokr.service.okr.OkrTopicDescriptionService;
import org.burningokr.service.okrUnit.CompanyService;
import org.burningokr.service.okrUnit.OkrChildUnitService;
import org.burningokr.service.okrUnit.departmentservices.BranchHelper;
import org.burningokr.service.okrUnitUtil.EntityCrawlerService;
import org.burningokr.service.security.AuthorizationUserContextService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.UUID;

@RestApiController
@RequiredArgsConstructor
public class OkrDepartmentController {
  private final DataMapper<OkrDepartment, OkrDepartmentDto> departmentMapper;
  private final DataMapper<Objective, ObjectiveDto> objectiveMapper;
  private final OkrBranchSchemaMapper okrBranchSchemaMapper;
  private final ObjectiveService objectiveService;
  private final OkrCompanyMapper okrCompanyMapper;
  private final OkrTopicDescriptionMapper okrTopicDescriptionMapper;
  private final OkrTopicDescriptionService okrTopicDescriptionService;
  private final CompanyService companyService;
  private final EntityCrawlerService entityCrawlerService;
  private final OkrChildUnitService<OkrDepartment> okrDepartmentService;
  private final AuthorizationUserContextService userContextService;

  /**
   * API Endpoint to get a OkrDepartment by it's ID.
   *
   * @param departmentId a long value
   * @return a {@link ResponseEntity} ok the OkrDepartment
   */
  @GetMapping("/departments/{departmentId}")
  public ResponseEntity<OkrDepartmentDto> getDepartmentByDepartmentId(
    @PathVariable long departmentId
  ) {
    OkrDepartment okrDepartment = okrDepartmentService.findById(departmentId);
    return ResponseEntity.ok(departmentMapper.mapEntityToDto(okrDepartment));
  }

  /**
   * API Endpoint to get the okrUnit of all the related Departments.
   *
   * @param departmentId a long value
   * @return a {@link ResponseEntity} ok with a {@link Collection} of Department Schema
   */
  @GetMapping("/departments/{departmentId}/schema")
  public ResponseEntity<Collection<OkrUnitSchemaDto>> getDepartmentSchemaOfDepartment(
    @PathVariable long departmentId
  ) {
    OkrDepartment okrDepartment = okrDepartmentService.findById(departmentId);
    OkrCompany parentOkrCompany = entityCrawlerService.getCompanyOfUnit(okrDepartment);

    UUID currentUserId = userContextService.getAuthenticatedUser().getId();
    return ResponseEntity.ok(
      okrBranchSchemaMapper.mapOkrChildUnitListToOkrChildUnitSchemaList(
        parentOkrCompany.getOkrChildUnits(), currentUserId));
  }

  /**
   * API Endpoint to get the parent-OkrCompany of a OkrDepartment.
   *
   * @param departmentId a long value
   * @return a {@link ResponseEntity} ok with the parent-OkrCompany
   */
  @GetMapping("/departments/{departmentId}/company")
  public ResponseEntity<OkrCompanyDto> getParentCompanyOfDepartment(
    @PathVariable long departmentId
  ) {
    OkrDepartment okrDepartment = okrDepartmentService.findById(departmentId);
    OkrCompany parentOkrCompany = entityCrawlerService.getCompanyOfUnit(okrDepartment);
    return ResponseEntity.ok(okrCompanyMapper.mapEntityToDto(parentOkrCompany));
  }

  /**
   * API Endpoint to get all Objectives of a OkrDepartment.
   *
   * @param departmentId a long value
   * @return a {@link ResponseEntity} ok with a {@link Collection} of Objectives
   */
  @GetMapping("/departments/{departmentId}/objectives")
  public ResponseEntity<Collection<ObjectiveDto>> getObjectivesOfDepartment(
    @PathVariable long departmentId
  ) {
    Collection<Objective> objectives = okrDepartmentService.findObjectivesOfUnit(departmentId);
    return ResponseEntity.ok(objectiveMapper.mapEntitiesToDtos(objectives));
  }

  /**
   * API Endpoint to get the OkrTopicDescription of an OkrDepartment
   *
   * @param departmentId a long value
   * @return a {@link ResponseEntity} ok with a {@link OkrTopicDescriptionDto}.
   */
  @GetMapping("/departments/{departmentId}/topicdescription")
  public ResponseEntity<OkrTopicDescriptionDto> getTopicDescriptionOfDepartment(
    @PathVariable long departmentId
  ) {
    OkrDepartment department = okrDepartmentService.findById(departmentId);
    OkrTopicDescription topicDescription = department.getOkrTopicDescription();

    OkrTopicDescriptionDto dto = okrTopicDescriptionMapper.mapEntityToDto(topicDescription);

    return ResponseEntity.ok(dto);
  }

  /**
   * API Endpoint to get all Departments for a given OkrCompany.
   *
   * @param companyId a long value
   * @return a {@link ResponseEntity} ok with a {@link Collection} of Departments
   */
  @GetMapping("/departments/flatted/{companyId}")
  public ResponseEntity<Collection<OkrDepartmentDto>> getAllDepartmentsForCompany(
    @PathVariable long companyId
  ) {
    OkrCompany okrCompany = companyService.findById(companyId);
    Collection<OkrDepartment> okrDepartments = BranchHelper.collectDepartments(okrCompany);
    return ResponseEntity.ok(departmentMapper.mapEntitiesToDtos(okrDepartments));
  }

  @PutMapping("/departments/{departmentId}")
  @PreAuthorize("@authorizationService.hasManagerPrivilegeForDepartment(#departmentId)")
  public ResponseEntity<OkrDepartmentDto> updateDepartment(
    @PathVariable long departmentId,
    @Valid
    @RequestBody
    OkrDepartmentDto okrDepartmentDto
  )
    throws DuplicateTeamMemberException {
    OkrDepartment okrDepartment = departmentMapper.mapDtoToEntity(okrDepartmentDto);
    okrDepartment.setId(departmentId);
    okrDepartment = okrDepartmentService.updateUnit(okrDepartment);
    return ResponseEntity.ok(departmentMapper.mapEntityToDto(okrDepartment));
  }

  /**
   * API Endpoint to update the OkrTopicDescription of an OkrDepartment
   *
   * @param departmentId           the id of the OkrDepartment
   * @param okrTopicDescriptionDto an {@link OkrTopicDescriptionDto} object
   * @param IUser                  an {@link IUser} object
   * @return the updated OkrTopicDescription
   */
  @PutMapping("/departments/{departmentId}/topicdescription")
  @PreAuthorize("@authorizationService.hasManagerPrivilegeForDepartment(#departmentId)")
  public ResponseEntity<OkrTopicDescriptionDto> updateOkrTopicDescription(
    @PathVariable long departmentId,
    @Valid
    @RequestBody
    OkrTopicDescriptionDto okrTopicDescriptionDto,
    IUser IUser
  ) {

    OkrDepartment okrDepartment = okrDepartmentService.findById(departmentId);
    OkrTopicDescription oldOkrTopicDescription = okrDepartment.getOkrTopicDescription();
    OkrTopicDescription updatedOkrTopicDescription =
      okrTopicDescriptionMapper.mapDtoToEntity(okrTopicDescriptionDto);

    updatedOkrTopicDescription.setId(oldOkrTopicDescription.getId());

    updatedOkrTopicDescription =
      okrTopicDescriptionService.updateOkrTopicDescription(updatedOkrTopicDescription, IUser);

    return ResponseEntity.ok(okrTopicDescriptionMapper.mapEntityToDto(updatedOkrTopicDescription));
  }

  /**
   * API Endpoint to add an Objective to a OkrDepartment.
   *
   * @param departmentId a long value
   * @param objectiveDto an {@link ObjectiveDto} object
   * @param IUser        an {@link IUser} object
   * @return a {@link ResponseEntity} ok with the added objective
   */
  @PostMapping("/departments/{departmentId}/objectives")
  @PreAuthorize("@authorizationService.hasManagerPrivilegeForDepartment(#departmentId)")
  public ResponseEntity<ObjectiveDto> addObjectiveToDepartment(
    @PathVariable long departmentId,
    @Valid
    @RequestBody
    ObjectiveDto objectiveDto,
    IUser IUser
  ) {
    Objective objective = objectiveMapper.mapDtoToEntity(objectiveDto);
    objective.setId(null);
    objective = objectiveService.createObjective(departmentId, objective);
    return ResponseEntity.ok(objectiveMapper.mapEntityToDto(objective));
  }

  /**
   * API Endpoint to delete a OkrDepartment.
   *
   * @param departmentId a long value
   * @param IUser        an {@link IUser} object
   * @return a {@link ResponseEntity} ok
   */
  @DeleteMapping("/departments/{departmentId}")
  @PreAuthorize("@authorizationService.isAdmin()")
  public ResponseEntity deleteDepartment(
    @PathVariable Long departmentId, IUser IUser
  ) {
    okrDepartmentService.deleteChildUnit(departmentId);
    return ResponseEntity.ok().build();
  }
}
