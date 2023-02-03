package org.burningokr.controller.okrUnit;

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
import org.burningokr.model.users.User;
import org.burningokr.service.exceptions.DuplicateTeamMemberException;
import org.burningokr.service.okr.OkrTopicDescriptionService;
import org.burningokr.service.okrUnit.CompanyService;
import org.burningokr.service.okrUnit.OkrUnitService;
import org.burningokr.service.okrUnit.OkrUnitServiceFactory;
import org.burningokr.service.okrUnit.departmentservices.BranchHelper;
import org.burningokr.service.okrUnitUtil.EntityCrawlerService;
import org.burningokr.service.security.AuthorizationService;
import org.burningokr.service.userhandling.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.UUID;

@RestApiController
public class OkrDepartmentController {

  private OkrUnitServiceFactory<OkrDepartment> departmentServicePicker;
  private DataMapper<OkrDepartment, OkrDepartmentDto> departmentMapper;
  private DataMapper<Objective, ObjectiveDto> objectiveMapper;
  private OkrBranchSchemaMapper okrBranchSchemaMapper;
  private OkrCompanyMapper okrCompanyMapper;
  private OkrTopicDescriptionMapper okrTopicDescriptionMapper;
  private OkrTopicDescriptionService okrTopicDescriptionService;
  private CompanyService companyService;
  private AuthorizationService authorizationService;
  private EntityCrawlerService entityCrawlerService;
  private UserService userService;

  /**
   * Initialize DepartmentController.
   *
   * @param departmentServicePicker {@link OkrUnitServiceFactory}
   * @param departmentMapper {@link DataMapper} with OkrDepartment and OkrDepartmentDto
   * @param objectiveMapper {@link DataMapper} with Objective and ObjectiveDto
   * @param authorizationService {@link AuthorizationService}
   * @param entityCrawlerService {@link EntityCrawlerService}
   * @param okrBranchSchemaMapper {@link OkrBranchSchemaMapper}
   * @param okrTopicDescriptionMapper {@link OkrTopicDescriptionMapper}
   * @param userService {@link UserService}
   * @param okrCompanyMapper {@link OkrCompanyMapper}
   * @param companyService {@link CompanyService}
   */
  @Autowired
  public OkrDepartmentController(
      OkrUnitServiceFactory<OkrDepartment> departmentServicePicker,
      DataMapper<OkrDepartment, OkrDepartmentDto> departmentMapper,
      DataMapper<Objective, ObjectiveDto> objectiveMapper,
      AuthorizationService authorizationService,
      EntityCrawlerService entityCrawlerService,
      OkrBranchSchemaMapper okrBranchSchemaMapper,
      OkrTopicDescriptionMapper okrTopicDescriptionMapper,
      OkrTopicDescriptionService okrTopicDescriptionService,
      UserService userService,
      OkrCompanyMapper okrCompanyMapper,
      CompanyService companyService) {
    this.departmentServicePicker = departmentServicePicker;
    this.departmentMapper = departmentMapper;
    this.objectiveMapper = objectiveMapper;
    this.authorizationService = authorizationService;
    this.entityCrawlerService = entityCrawlerService;
    this.okrBranchSchemaMapper = okrBranchSchemaMapper;
    this.userService = userService;
    this.okrCompanyMapper = okrCompanyMapper;
    this.companyService = companyService;
    this.okrTopicDescriptionMapper = okrTopicDescriptionMapper;
    this.okrTopicDescriptionService = okrTopicDescriptionService;
  }

  /**
   * API Endpoint to get a OkrDepartment by it's ID.
   *
   * @param departmentId a long value
   * @return a {@link ResponseEntity} ok the OkrDepartment
   */
  @GetMapping("/departments/{departmentId}")
  public ResponseEntity<OkrDepartmentDto> getDepartmentByDepartmentId(
      @PathVariable long departmentId) {
    OkrUnitService<OkrDepartment> departmentService =
        departmentServicePicker.getRoleServiceForDepartment(departmentId);
    OkrDepartment okrDepartment = departmentService.findById(departmentId);
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
      @PathVariable long departmentId) {
    OkrUnitService<OkrDepartment> departmentService =
        departmentServicePicker.getRoleServiceForDepartment(departmentId);
    OkrDepartment okrDepartment = departmentService.findById(departmentId);
    OkrCompany parentOkrCompany = entityCrawlerService.getCompanyOfUnit(okrDepartment);
    UUID currentUserId = userService.getCurrentUser().getId();
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
      @PathVariable long departmentId) {
    OkrUnitService<OkrDepartment> departmentService =
        departmentServicePicker.getRoleServiceForDepartment(departmentId);
    OkrDepartment okrDepartment = departmentService.findById(departmentId);
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
      @PathVariable long departmentId) {
    OkrUnitService<OkrDepartment> departmentService =
        departmentServicePicker.getRoleServiceForDepartment(departmentId);
    Collection<Objective> objectives = departmentService.findObjectivesOfUnit(departmentId);
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
      @PathVariable long departmentId) {
    OkrUnitService<OkrDepartment> departmentService =
        departmentServicePicker.getRoleServiceForDepartment(departmentId);
    OkrDepartment department = departmentService.findById(departmentId);
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
      @PathVariable long companyId) {
    OkrCompany okrCompany = companyService.findById(companyId);
    Collection<OkrDepartment> okrDepartments = BranchHelper.collectDepartments(okrCompany);
    return ResponseEntity.ok(departmentMapper.mapEntitiesToDtos(okrDepartments));
  }

  /**
   * API Endpoint to update an existing OkrDepartment.
   *
   * @param departmentId a long value
   * @param okrDepartmentDto a {@link OkrDepartmentDto} object
   * @param user an {@link User} object
   * @return the updated department
   */
  @PutMapping("/departments/{departmentId}")
  @PreAuthorize("@authorizationService.hasManagerPrivilegeForDepartment(#departmentId)")
  public ResponseEntity<OkrDepartmentDto> updateDepartment(
      @PathVariable long departmentId,
      @Valid @RequestBody OkrDepartmentDto okrDepartmentDto,
      User user)
      throws DuplicateTeamMemberException {
    OkrUnitService<OkrDepartment> departmentService =
        departmentServicePicker.getRoleServiceForDepartment(departmentId);
    OkrDepartment okrDepartment = departmentMapper.mapDtoToEntity(okrDepartmentDto);
    okrDepartment.setId(departmentId);
    okrDepartment = departmentService.updateUnit(okrDepartment, user);
    return ResponseEntity.ok(departmentMapper.mapEntityToDto(okrDepartment));
  }

  /**
   * API Endpoint to update the OkrTopicDescription of an OkrDepartment
   *
   * @param departmentId the id of the OkrDepartment
   * @param okrTopicDescriptionDto an {@link OkrTopicDescriptionDto} object
   * @param user an {@link User} object
   * @return the updated OkrTopicDescription
   */
  @PutMapping("/departments/{departmentId}/topicdescription")
  @PreAuthorize("@authorizationService.hasManagerPrivilegeForDepartment(#departmentId)")
  public ResponseEntity<OkrTopicDescriptionDto> updateOkrTopicDescription(
      @PathVariable long departmentId,
      @Valid @RequestBody OkrTopicDescriptionDto okrTopicDescriptionDto,
      User user) {
    OkrUnitService<OkrDepartment> departmentService =
        departmentServicePicker.getRoleServiceForDepartment(departmentId);
    OkrDepartment okrDepartment = departmentService.findById(departmentId);
    OkrTopicDescription oldOkrTopicDescription = okrDepartment.getOkrTopicDescription();
    OkrTopicDescription updatedOkrTopicDescription =
        okrTopicDescriptionMapper.mapDtoToEntity(okrTopicDescriptionDto);

    updatedOkrTopicDescription.setId(oldOkrTopicDescription.getId());

    updatedOkrTopicDescription =
        okrTopicDescriptionService.updateOkrTopicDescription(updatedOkrTopicDescription, user);

    return ResponseEntity.ok(okrTopicDescriptionMapper.mapEntityToDto(updatedOkrTopicDescription));
  }

  /**
   * API Endpoint to add an Objective to a OkrDepartment.
   *
   * @param departmentId a long value
   * @param objectiveDto an {@link ObjectiveDto} object
   * @param user an {@link User} object
   * @return a {@link ResponseEntity} ok with the added objective
   */
  @PostMapping("/departments/{departmentId}/objectives")
  @PreAuthorize("@authorizationService.hasManagerPrivilegeForDepartment(#departmentId)")
  public ResponseEntity<ObjectiveDto> addObjectiveToDepartment(
      @PathVariable long departmentId, @Valid @RequestBody ObjectiveDto objectiveDto, User user) {
    OkrUnitService departmentService =
        departmentServicePicker.getRoleServiceForDepartment(departmentId);
    Objective objective = objectiveMapper.mapDtoToEntity(objectiveDto);
    objective.setId(null);
    objective = departmentService.createObjective(departmentId, objective, user);
    return ResponseEntity.ok(objectiveMapper.mapEntityToDto(objective));
  }

  /**
   * API Endpoint to delete a OkrDepartment.
   *
   * @param departmentId a long value
   * @param user an {@link User} object
   * @return a {@link ResponseEntity} ok
   */
  @DeleteMapping("/departments/{departmentId}")
  @PreAuthorize("@authorizationService.isAdmin()")
  public ResponseEntity deleteDepartment(@PathVariable Long departmentId, User user) {
    OkrUnitService<OkrDepartment> departmentService =
        departmentServicePicker.getRoleServiceForDepartment(departmentId);
    departmentService.deleteUnit(departmentId, user);
    return ResponseEntity.ok().build();
  }
}
