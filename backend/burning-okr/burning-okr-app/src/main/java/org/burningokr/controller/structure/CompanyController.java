package org.burningokr.controller.structure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import javax.validation.Valid;
import org.burningokr.annotation.RestApiController;
import org.burningokr.dto.cycle.CycleDto;
import org.burningokr.dto.okr.ObjectiveDto;
import org.burningokr.dto.structure.CompanyDto;
import org.burningokr.dto.structure.CorporateObjectiveStructureDto;
import org.burningokr.dto.structure.DepartmentDto;
import org.burningokr.dto.structure.StructureSchemaDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.mapper.structure.CorporateObjectiveStructureMapper;
import org.burningokr.mapper.structure.DepartmentMapper;
import org.burningokr.mapper.structure.StructureSchemaMapper;
import org.burningokr.model.cycles.Cycle;
import org.burningokr.model.cycles.CycleState;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.structures.Company;
import org.burningokr.model.structures.CorporateObjectiveStructure;
import org.burningokr.model.structures.Department;
import org.burningokr.model.structures.Structure;
import org.burningokr.model.users.User;
import org.burningokr.service.security.AuthorizationService;
import org.burningokr.service.structure.CompanyService;
import org.burningokr.service.structure.StructureService;
import org.burningokr.service.structure.StructureServicePicker;
import org.burningokr.service.structure.departmentservices.StructureServiceAdmins;
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
public class CompanyController {

  private CompanyService companyService;
  private DataMapper<Company, CompanyDto> companyMapper;
  private DataMapper<Department, DepartmentDto> departmentMapper;
  private DataMapper<Cycle, CycleDto> cycleMapper;
  private StructureSchemaMapper structureSchemaMapper;
  private DataMapper<Objective, ObjectiveDto> objectiveMapper;
  private DataMapper<CorporateObjectiveStructure, CorporateObjectiveStructureDto> corporateObjectiveStructureMapper;
  private AuthorizationService authorizationService;
  private UserService userService;
  private StructureServiceAdmins<CorporateObjectiveStructure> corporateObjectiveStructureService;

  /**
   * Initialize CompanyController.
   *
   * @param companyService a {@link CompanyService} object
   * @param companyMapper a {@link DataMapper} object with {@link Company} and {@link CompanyDto}
   * @param cycleMapper a {@link DataMapper} object with {@link Cycle} nad {@link CycleDto}
   * @param departmentMapper a {@link DataMapper} object with {@link Department} and {@link
   *     DepartmentDto}
   * @param objectiveMapper a {@link DataMapper} object with {@link Objective} and {@link
   *     ObjectiveDto}
   * @param authorizationService an {@link AuthorizationService} object
   * @param structureSchemaMapper a {@link StructureSchemaMapper} object
   * @param userService an {@link UserService} object
   */
  @Autowired
  public CompanyController(
      CompanyService companyService,
      DataMapper<Company, CompanyDto> companyMapper,
      DataMapper<Cycle, CycleDto> cycleMapper,
      DataMapper<Department, DepartmentDto> departmentMapper,
      DataMapper<Objective, ObjectiveDto> objectiveMapper,
      DataMapper<CorporateObjectiveStructure, CorporateObjectiveStructureDto> corporateObjectiveStructureMapper,
      AuthorizationService authorizationService,
      StructureSchemaMapper structureSchemaMapper,
      UserService userService,
      StructureServiceAdmins<CorporateObjectiveStructure> corporateObjectiveStructureService) {
    this.companyService = companyService;
    this.companyMapper = companyMapper;
    this.cycleMapper = cycleMapper;
    this.departmentMapper = departmentMapper;
    this.objectiveMapper = objectiveMapper;
    this.authorizationService = authorizationService;
    this.structureSchemaMapper = structureSchemaMapper;
    this.corporateObjectiveStructureMapper = corporateObjectiveStructureMapper;
    this.userService = userService;
    this.corporateObjectiveStructureService = corporateObjectiveStructureService;
  }

  /**
   * API Endpoint to get active Companies.
   *
   * @return a {@link ResponseEntity} ok with a {@link Collection} of Companies
   */
  @GetMapping("/companies")
  public ResponseEntity<Collection<CompanyDto>> getActiveCompanies() {
    Collection<CompanyDto> companies = new ArrayList<>();
    for (Company company : this.companyService.getAllCompanies()) {
      if (company.getCycle().getCycleState().equals(CycleState.ACTIVE)) {
        companies.add(companyMapper.mapEntityToDto(company));
      }
    }
    return ResponseEntity.ok(companies);
  }

  /**
   * API Endpoint to get all Companies.
   *
   * @return a {@link ResponseEntity} ok with a {@link Collection} of Companies
   */
  @GetMapping("/companies/all")
  public ResponseEntity<Collection<CompanyDto>> getAllCompanies() {
    Collection<Company> companies = companyService.getAllCompanies();
    return ResponseEntity.ok(companyMapper.mapEntitiesToDtos(companies));
  }

  /**
   * API Endpoint to get a Company by ID.
   *
   * @param companyId a long value
   * @return a {@link ResponseEntity} ok with a Company
   */
  @GetMapping("/companies/{companyId}")
  public ResponseEntity<CompanyDto> getCompanyById(@PathVariable long companyId) {
    Company company = this.companyService.findById(companyId);
    return ResponseEntity.ok(companyMapper.mapEntityToDto(company));
  }

  /**
   * API Endpoint to get the History of a Company.
   *
   * @param companyId a long value
   * @return a {@link ResponseEntity} ok with a {@link Collection} of Company
   */
  @GetMapping("/companies/{companyId}/history")
  public ResponseEntity<Collection<CompanyDto>> getCompanyHistoryByCompanyId(
      @PathVariable long companyId) {
    Collection<Company> companies = companyService.findCompanyHistoryByCompanyId(companyId);
    return ResponseEntity.ok(companyMapper.mapEntitiesToDtos(companies));
  }

  /**
   * API Endpoint to get the Cycles of a Company.
   *
   * @param companyId a long value
   * @return a {@link ResponseEntity} ok with a {@link Collection} of Cycles
   */
  @GetMapping("/companies/{companyId}/cycles")
  public ResponseEntity<Collection<CycleDto>> getCompanyCycleList(@PathVariable long companyId) {
    Collection<Cycle> cycles = companyService.findCycleListByCompanyId(companyId);
    return ResponseEntity.ok(cycleMapper.mapEntitiesToDtos(cycles));
  }

  /**
   * API Endpoint to get the Department Structure of a Company.
   *
   * @param companyId a long value
   * @return a {@link ResponseEntity} ok with a {@link Collection} of Department Structure
   */
  @GetMapping("/companies/{companyId}/structure")
  public ResponseEntity<Collection<StructureSchemaDto>> getDepartmentStructureOfCompany(
      @PathVariable long companyId) {
    Company company = this.companyService.findById(companyId);
    UUID currentUserId = userService.getCurrentUser().getId();
    return ResponseEntity.ok(
        structureSchemaMapper.mapStructureListToStructureSchemaList(
            company.getSubStructures(), currentUserId));
  }

  /**
   * API Endpoint to add a Company.
   *
   * @param companyDto a {@link CompanyDto} object
   * @param user an {@link User} object
   * @return a {@link ResponseEntity} ok with a Company
   */
  @PostMapping("/companies")
  @PreAuthorize("@authorizationService.isAdmin()")
  public ResponseEntity<CompanyDto> addCompany(
      @Valid @RequestBody CompanyDto companyDto, User user) {
    Company company = companyMapper.mapDtoToEntity(companyDto);
    company = this.companyService.createCompany(company, user);
    return ResponseEntity.ok(companyMapper.mapEntityToDto(company));
  }

  /**
   * API Endpoint to add an Objective to a Department.
   *
   * @param companyId a ong value
   * @param departmentDto a {@link DepartmentDto} object
   * @param user an {@link User} object
   * @return a {@link ResponseEntity} ok with a Department
   */
  @PostMapping("/companies/{companyId}/departments")
  @PreAuthorize("@authorizationService.isAdmin()")
  public ResponseEntity<DepartmentDto> addDepartmentToCompanyById(
      @PathVariable long companyId, @Valid @RequestBody DepartmentDto departmentDto, User user) {
    DataMapper<Department, DepartmentDto> departmentMapper = new DepartmentMapper();
    Department department = departmentMapper.mapDtoToEntity(departmentDto);
    department.setId(null);
    department = this.companyService.createDepartment(companyId, department, user);
    return ResponseEntity.ok(departmentMapper.mapEntityToDto(department));
  }

  @PostMapping("/companies/{companyId}/corporateObjectiveStructures")
  @PreAuthorize("@authorizationService.isAdmin()")
  public ResponseEntity<CorporateObjectiveStructureDto> addCorporateObjectiveStructureToCompanyById(
      @PathVariable long companyId, @Valid @RequestBody CorporateObjectiveStructureDto corporateObjectiveStructureDto, User user) {

    CorporateObjectiveStructure corporateObjectiveStructure = corporateObjectiveStructureMapper.mapDtoToEntity(corporateObjectiveStructureDto);
    corporateObjectiveStructure.setId(null);
    CorporateObjectiveStructure createdStructure = companyService.createCorporateObjectiveStructure(companyId, corporateObjectiveStructure, user);

    return ResponseEntity.ok(corporateObjectiveStructureMapper.mapEntityToDto(createdStructure));
  }

  /**
   * API Endpoint to update a Company by ID.
   *
   * @param companyId a long value
   * @param companyDto a {@link CompanyDto} object
   * @param user an {@link User} object
   * @return a {@link ResponseEntity} ok with a {@link Collection} of Companies
   */
  @PutMapping("/companies/{companyId}")
  @PreAuthorize("@authorizationService.isAdmin()")
  public ResponseEntity<CompanyDto> updateCompanyById(
      @PathVariable long companyId, @RequestBody CompanyDto companyDto, User user) {
    Company company = companyMapper.mapDtoToEntity(companyDto);
    company = this.companyService.updateCompany(company, user);
    return ResponseEntity.ok(companyMapper.mapEntityToDto(company));
  }

  /**
   * API Endpoint to delete a Company and it's history.
   *
   * @param companyId a long value
   * @param user an {@link User} object
   * @return a {@link ResponseEntity} ok
   */
  @DeleteMapping("/companies/{companyId}")
  @PreAuthorize("@authorizationService.isAdmin()")
  public ResponseEntity deleteCompany(@PathVariable Long companyId, User user) {
    companyService.deleteCompany(companyId, true, user);
    return ResponseEntity.ok().build();
  }
}
