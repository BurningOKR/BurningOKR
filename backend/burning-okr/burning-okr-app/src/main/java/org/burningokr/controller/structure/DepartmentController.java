package org.burningokr.controller.structure;

import java.util.Collection;
import java.util.UUID;
import javax.validation.Valid;
import org.burningokr.annotation.RestApiController;
import org.burningokr.dto.okr.ObjectiveDto;
import org.burningokr.dto.structure.CompanyDto;
import org.burningokr.dto.structure.DepartmentDto;
import org.burningokr.dto.structure.DepartmentStructureDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.mapper.structure.CompanyMapper;
import org.burningokr.mapper.structure.DepartmentStructureMapper;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.structures.Company;
import org.burningokr.model.structures.Department;
import org.burningokr.model.users.User;
import org.burningokr.service.exceptions.DuplicateTeamMemberException;
import org.burningokr.service.security.AuthorizationService;
import org.burningokr.service.structure.CompanyService;
import org.burningokr.service.structure.DepartmentServicePicker;
import org.burningokr.service.structure.departmentservices.DepartmentHelper;
import org.burningokr.service.structure.StructureService;
import org.burningokr.service.structureutil.EntityCrawlerService;
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
public class DepartmentController {

  private DepartmentServicePicker departmentServicePicker;
  private DataMapper<Department, DepartmentDto> departmentMapper;
  private DataMapper<Objective, ObjectiveDto> objectiveMapper;
  private DepartmentStructureMapper departmentStructureMapper;
  private CompanyMapper companyMapper;
  private CompanyService companyService;
  private AuthorizationService authorizationService;
  private EntityCrawlerService entityCrawlerService;
  private UserService userService;

  /**
   * Initialize DepartmentController.
   *
   * @param departmentServicePicker {@link DepartmentServicePicker}
   * @param departmentMapper {@link DataMapper} with Department and DepartmentDto
   * @param objectiveMapper {@link DataMapper} with Objective and ObjectiveDto
   * @param authorizationService {@link AuthorizationService}
   * @param entityCrawlerService {@link EntityCrawlerService}
   * @param departmentStructureMapper {@link DepartmentStructureMapper}
   * @param userService {@link UserService}
   * @param companyMapper {@link CompanyMapper}
   * @param companyService {@link CompanyService}
   */
  @Autowired
  public DepartmentController(
      DepartmentServicePicker departmentServicePicker,
      DataMapper<Department, DepartmentDto> departmentMapper,
      DataMapper<Objective, ObjectiveDto> objectiveMapper,
      AuthorizationService authorizationService,
      EntityCrawlerService entityCrawlerService,
      DepartmentStructureMapper departmentStructureMapper,
      UserService userService,
      CompanyMapper companyMapper,
      CompanyService companyService) {
    this.departmentServicePicker = departmentServicePicker;
    this.departmentMapper = departmentMapper;
    this.objectiveMapper = objectiveMapper;
    this.authorizationService = authorizationService;
    this.entityCrawlerService = entityCrawlerService;
    this.departmentStructureMapper = departmentStructureMapper;
    this.userService = userService;
    this.companyMapper = companyMapper;
    this.companyService = companyService;
  }

  /**
   * API Endpoint to get a Department by it's ID.
   *
   * @param departmentId a long value
   * @return a {@link ResponseEntity} ok the Department
   */
  @GetMapping("/departments/{departmentId}")
  public ResponseEntity<DepartmentDto> getDepartmentByDepartmentId(
      @PathVariable long departmentId) {
    StructureService<Department> departmentService =
        departmentServicePicker.getRoleServiceForDepartment(departmentId);
    Department department = departmentService.findById(departmentId);
    return ResponseEntity.ok(departmentMapper.mapEntityToDto(department));
  }

  /**
   * API Endpoint to get the structure of all the related Departments.
   *
   * @param departmentId a long value
   * @return a {@link ResponseEntity} ok with a {@link Collection} of DepartmentStructure
   */
  @GetMapping("/departments/{departmentId}/structure")
  public ResponseEntity<Collection<DepartmentStructureDto>> getDepartmentStructureOfDepartment(
      @PathVariable long departmentId) {
    StructureService<Department> departmentService =
        departmentServicePicker.getRoleServiceForDepartment(departmentId);
    Department department = departmentService.findById(departmentId);
    Company parentCompany = entityCrawlerService.getCompanyOfDepartment(department);
    UUID currentUserId = userService.getCurrentUser().getId();
    return ResponseEntity.ok(
        departmentStructureMapper.mapDepartmentListToDepartmentStructureList(
            parentCompany.getDepartments(), currentUserId));
  }

  /**
   * API Endpoint to get the parent-Company of a Department.
   *
   * @param departmentId a long value
   * @return a {@link ResponseEntity} ok with the parent-Company
   */
  @GetMapping("/departments/{departmentId}/company")
  public ResponseEntity<CompanyDto> getParentCompanyOfDepartment(@PathVariable long departmentId) {
    StructureService<Department> departmentService =
        departmentServicePicker.getRoleServiceForDepartment(departmentId);
    Department department = departmentService.findById(departmentId);
    Company parentCompany = entityCrawlerService.getCompanyOfDepartment(department);
    return ResponseEntity.ok(companyMapper.mapEntityToDto(parentCompany));
  }

  /**
   * API Endpoint to get all sub-Departments of a Department.
   *
   * @param departmentId a long value
   * @return a {@link ResponseEntity} ok with a {@link Collection} of Departments
   */
  @GetMapping("/departments/{departmentId}/departments")
  public ResponseEntity<Collection<DepartmentDto>> getSubDepartmentsOfDepartment(
      @PathVariable long departmentId) {
    StructureService<Department> departmentService =
        departmentServicePicker.getRoleServiceForDepartment(departmentId);
    Collection<Department> departments =
        departmentService.findSubStructuresOfStructure(departmentId);
    return ResponseEntity.ok(departmentMapper.mapEntitiesToDtos(departments));
  }

  /**
   * API Endpoint to get all Objectives of a Department.
   *
   * @param departmentId a long value
   * @return a {@link ResponseEntity} ok with a {@link Collection} of Objectives
   */
  @GetMapping("/departments/{departmentId}/objectives")
  public ResponseEntity<Collection<ObjectiveDto>> getObjectivesOfDepartment(
      @PathVariable long departmentId) {
    StructureService<Department> departmentService =
        departmentServicePicker.getRoleServiceForDepartment(departmentId);
    Collection<Objective> objectives = departmentService.findObjectivesOfStructure(departmentId);
    return ResponseEntity.ok(objectiveMapper.mapEntitiesToDtos(objectives));
  }

  /**
   * API Endpoint to get all Departments for a given Company.
   *
   * @param companyId a long value
   * @return a {@link ResponseEntity} ok with a {@link Collection} of Departments
   */
  @GetMapping("/departments/flatted/{companyId}")
  public ResponseEntity<Collection<DepartmentDto>> getAllDepartmentsForCompany(
      @PathVariable long companyId) {
    Company company = companyService.findById(companyId);
    Collection<Department> departments = DepartmentHelper.collectDepartments(company);
    return ResponseEntity.ok(departmentMapper.mapEntitiesToDtos(departments));
  }

  /**
   * API Endpoint to update an existing Department.
   *
   * @param departmentId a long value
   * @param departmentDto a {@link DepartmentDto} object
   * @param user an {@link User} object
   * @return
   */
  @PutMapping("/departments/{departmentId}")
  @PreAuthorize("@authorizationService.hasManagerPrivilegeForDepartment(#departmentId)")
  public ResponseEntity<DepartmentDto> updateDepartment(
      @PathVariable long departmentId, @Valid @RequestBody DepartmentDto departmentDto, User user)
      throws DuplicateTeamMemberException {
    StructureService<Department> departmentService =
        departmentServicePicker.getRoleServiceForDepartment(departmentId);
    Department department = departmentMapper.mapDtoToEntity(departmentDto);
    department.setId(departmentId);
    department = departmentService.updateStructure(department, user);
    return ResponseEntity.ok(departmentMapper.mapEntityToDto(department));
  }

  /**
   * API Endpoint to add a sub-Department to an existing Department.
   *
   * @param departmentId a long value
   * @param departmentDto a {@link DepartmentDto} object
   * @param user an {@link User} object
   * @return a {@link ResponseEntity} ok with the added sub-Department
   */
  @PostMapping("/departments/{departmentId}/subdepartments")
  @PreAuthorize("@authorizationService.isAdmin()")
  public ResponseEntity<DepartmentDto> addSubDepartmentToDepartment(
      @PathVariable long departmentId, @Valid @RequestBody DepartmentDto departmentDto, User user)
      throws DuplicateTeamMemberException {
    StructureService<Department> departmentService =
        departmentServicePicker.getRoleServiceForDepartment(departmentId);
    Department department = departmentMapper.mapDtoToEntity(departmentDto);
    department.setId(null);
    department = departmentService.createSubstructure(departmentId, department, user);
    return ResponseEntity.ok(departmentMapper.mapEntityToDto(department));
  }

  /**
   * API Endpoint to add an Objective to a Department.
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
    StructureService departmentService =
        departmentServicePicker.getRoleServiceForDepartment(departmentId);
    Objective objective = objectiveMapper.mapDtoToEntity(objectiveDto);
    objective.setId(null);
    objective = departmentService.createObjective(departmentId, objective, user);
    return ResponseEntity.ok(objectiveMapper.mapEntityToDto(objective));
  }

  /**
   * API Endpoint to delete a Department.
   *
   * @param departmentId a long value
   * @param user an {@link User} object
   * @return a {@link ResponseEntity} ok
   */
  @DeleteMapping("/departments/{departmentId}")
  @PreAuthorize("@authorizationService.isAdmin()")
  public ResponseEntity deleteDepartment(@PathVariable Long departmentId, User user) {
    StructureService<Department> departmentService =
        departmentServicePicker.getRoleServiceForDepartment(departmentId);
    departmentService.deleteDepartment(departmentId, user);
    return ResponseEntity.ok().build();
  }
}
