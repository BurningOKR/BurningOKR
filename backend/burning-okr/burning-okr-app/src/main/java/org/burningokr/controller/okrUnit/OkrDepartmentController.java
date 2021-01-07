package org.burningokr.controller.okrUnit;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;
import javax.validation.Valid;
import org.burningokr.annotation.RestApiController;
import org.burningokr.dto.okr.ObjectiveDto;
import org.burningokr.dto.okr.OkrTopicDescriptionDto;
import org.burningokr.dto.okrUnit.OkrCompanyDto;
import org.burningokr.dto.okrUnit.OkrDepartmentDto;
import org.burningokr.dto.okrUnit.OkrUnitSchemaDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.mapper.okrUnit.OkrBranchSchemaMapper;
import org.burningokr.mapper.okrUnit.OkrCompanyMapper;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.okrUnits.OkrCompany;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.burningokr.model.users.User;
import org.burningokr.service.exceptions.DuplicateTeamMemberException;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestApiController
public class OkrDepartmentController {

  private OkrUnitServiceFactory<OkrDepartment> departmentServicePicker;
  private DataMapper<OkrDepartment, OkrDepartmentDto> departmentMapper;
  private DataMapper<Objective, ObjectiveDto> objectiveMapper;
  private OkrBranchSchemaMapper okrBranchSchemaMapper;
  private OkrCompanyMapper okrCompanyMapper;
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

  @GetMapping("/departments/{departmentId}/topicdescription")
  public ResponseEntity<OkrTopicDescriptionDto> getTopicDescriptionOfDepartment(
      @PathVariable long departmentId) {
    OkrTopicDescriptionDto dto = new OkrTopicDescriptionDto();
    dto.setAcceptanceCriteria(
        "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.");
    dto.setBeginning(LocalDate.now());
    dto.setContributesTo(
        "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.");
    dto.setDelimitation(
        "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.");
    dto.setDependencies(
        "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.");
    dto.setHandoverPlan(
        "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.");
    dto.setId(1337l);
    dto.setInitiatorId(userService.getCurrentUser().getId());
    dto.setName("Test Beschreibung");
    dto.setResources(
        "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.");
    dto.setStakeholders(Collections.singletonList(userService.getCurrentUser().getId()));
    dto.setStartTeam(dto.getStakeholders());

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
   * @return
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
