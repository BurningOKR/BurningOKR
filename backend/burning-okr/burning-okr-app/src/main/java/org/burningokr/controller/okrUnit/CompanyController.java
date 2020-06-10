package org.burningokr.controller.okrUnit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import javax.validation.Valid;
import org.burningokr.annotation.RestApiController;
import org.burningokr.dto.cycle.CycleDto;
import org.burningokr.dto.okr.ObjectiveDto;
import org.burningokr.dto.okrUnit.OkrBranchDTO;
import org.burningokr.dto.okrUnit.OkrCompanyDto;
import org.burningokr.dto.okrUnit.OkrDepartmentDto;
import org.burningokr.dto.okrUnit.OkrUnitSchemaDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.mapper.okrUnit.OkrBranchSchemaMapper;
import org.burningokr.mapper.okrUnit.OkrDepartmentMapper;
import org.burningokr.model.cycles.Cycle;
import org.burningokr.model.cycles.CycleState;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.okrUnits.OkrBranch;
import org.burningokr.model.okrUnits.OkrCompany;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.burningokr.model.users.User;
import org.burningokr.service.okrUnit.CompanyService;
import org.burningokr.service.okrUnit.departmentservices.OkrUnitServiceAdmins;
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
public class CompanyController {

  private CompanyService companyService;
  private DataMapper<OkrCompany, OkrCompanyDto> companyMapper;
  private DataMapper<OkrDepartment, OkrDepartmentDto> departmentMapper;
  private DataMapper<Cycle, CycleDto> cycleMapper;
  private OkrBranchSchemaMapper okrUnitSchemaMapper;
  private DataMapper<Objective, ObjectiveDto> objectiveMapper;
  private DataMapper<OkrBranch, OkrBranchDTO> okrBranchMapper;
  private AuthorizationService authorizationService;
  private UserService userService;
  private OkrUnitServiceAdmins<OkrBranch> OkrBranchService;

  /**
   * Initialize CompanyController.
   *
   * @param companyService a {@link CompanyService} object
   * @param companyMapper a {@link DataMapper} object with {@link OkrCompany} and {@link
   *     OkrCompanyDto}
   * @param cycleMapper a {@link DataMapper} object with {@link Cycle} nad {@link CycleDto}
   * @param departmentMapper a {@link DataMapper} object with {@link OkrDepartment} and {@link
   *     OkrDepartmentDto}
   * @param objectiveMapper a {@link DataMapper} object with {@link Objective} and {@link
   *     ObjectiveDto}
   * @param authorizationService an {@link AuthorizationService} object
   * @param okrUnitSchemaMapper a {@link OkrBranchSchemaMapper} object
   * @param userService an {@link UserService} object
   */
  @Autowired
  public CompanyController(
      CompanyService companyService,
      DataMapper<OkrCompany, OkrCompanyDto> companyMapper,
      DataMapper<Cycle, CycleDto> cycleMapper,
      DataMapper<OkrDepartment, OkrDepartmentDto> departmentMapper,
      DataMapper<Objective, ObjectiveDto> objectiveMapper,
      DataMapper<OkrBranch, OkrBranchDTO> OkrBranchMapper,
      AuthorizationService authorizationService,
      OkrBranchSchemaMapper okrUnitSchemaMapper,
      UserService userService,
      OkrUnitServiceAdmins<OkrBranch> OkrBranchService) {
    this.companyService = companyService;
    this.companyMapper = companyMapper;
    this.cycleMapper = cycleMapper;
    this.departmentMapper = departmentMapper;
    this.objectiveMapper = objectiveMapper;
    this.authorizationService = authorizationService;
    this.okrUnitSchemaMapper = okrUnitSchemaMapper;
    this.okrBranchMapper = OkrBranchMapper;
    this.userService = userService;
    this.OkrBranchService = OkrBranchService;
  }

  /**
   * API Endpoint to get active Companies.
   *
   * @return a {@link ResponseEntity} ok with a {@link Collection} of Companies
   */
  @GetMapping("/companies")
  public ResponseEntity<Collection<OkrCompanyDto>> getActiveCompanies() {
    Collection<OkrCompanyDto> companies = new ArrayList<>();
    for (OkrCompany okrCompany : this.companyService.getAllCompanies()) {
      if (okrCompany.getCycle().getCycleState().equals(CycleState.ACTIVE)) {
        companies.add(companyMapper.mapEntityToDto(okrCompany));
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
  public ResponseEntity<Collection<OkrCompanyDto>> getAllCompanies() {
    Collection<OkrCompany> companies = companyService.getAllCompanies();
    return ResponseEntity.ok(companyMapper.mapEntitiesToDtos(companies));
  }

  /**
   * API Endpoint to get a OkrCompany by ID.
   *
   * @param companyId a long value
   * @return a {@link ResponseEntity} ok with a OkrCompany
   */
  @GetMapping("/companies/{companyId}")
  public ResponseEntity<OkrCompanyDto> getCompanyById(@PathVariable long companyId) {
    OkrCompany okrCompany = this.companyService.findById(companyId);
    return ResponseEntity.ok(companyMapper.mapEntityToDto(okrCompany));
  }

  /**
   * API Endpoint to get the History of a OkrCompany.
   *
   * @param companyId a long value
   * @return a {@link ResponseEntity} ok with a {@link Collection} of OkrCompany
   */
  @GetMapping("/companies/{companyId}/history")
  public ResponseEntity<Collection<OkrCompanyDto>> getCompanyHistoryByCompanyId(
      @PathVariable long companyId) {
    Collection<OkrCompany> companies = companyService.findCompanyHistoryByCompanyId(companyId);
    return ResponseEntity.ok(companyMapper.mapEntitiesToDtos(companies));
  }

  /**
   * API Endpoint to get the Cycles of a OkrCompany.
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
   * API Endpoint to get the OkrDepartment okrUnit of a OkrCompany.
   *
   * @param companyId a long value
   * @return a {@link ResponseEntity} ok with a {@link Collection} of OkrDepartment okrUnit
   */
  @GetMapping("/companies/{companyId}/unit")
  public ResponseEntity<Collection<OkrUnitSchemaDto>> getDepartmentSchemaOfCompany(
      @PathVariable long companyId) {
    OkrCompany okrCompany = this.companyService.findById(companyId);
    UUID currentUserId = userService.getCurrentUser().getId();
    return ResponseEntity.ok(
        okrUnitSchemaMapper.mapOkrChildUnitListToOkrChildUnitSchemaList(
            okrCompany.getOkrChildUnits(), currentUserId));
  }

  /**
   * API Endpoint to add a OkrCompany.
   *
   * @param okrCompanyDto a {@link OkrCompanyDto} object
   * @param user an {@link User} object
   * @return a {@link ResponseEntity} ok with a OkrCompany
   */
  @PostMapping("/companies")
  @PreAuthorize("@authorizationService.isAdmin()")
  public ResponseEntity<OkrCompanyDto> addCompany(
      @Valid @RequestBody OkrCompanyDto okrCompanyDto, User user) {
    OkrCompany okrCompany = companyMapper.mapDtoToEntity(okrCompanyDto);
    okrCompany = this.companyService.createCompany(okrCompany, user);
    return ResponseEntity.ok(companyMapper.mapEntityToDto(okrCompany));
  }

  /**
   * API Endpoint to add an Objective to a OkrDepartment.
   *
   * @param companyId a ong value
   * @param okrDepartmentDto a {@link OkrDepartmentDto} object
   * @param user an {@link User} object
   * @return a {@link ResponseEntity} ok with a OkrDepartment
   */
  @PostMapping("/companies/{companyId}/departments")
  @PreAuthorize("@authorizationService.isAdmin()")
  public ResponseEntity<OkrDepartmentDto> addDepartmentToCompanyById(
      @PathVariable long companyId,
      @Valid @RequestBody OkrDepartmentDto okrDepartmentDto,
      User user) {
    DataMapper<OkrDepartment, OkrDepartmentDto> departmentMapper = new OkrDepartmentMapper();
    OkrDepartment okrDepartment = departmentMapper.mapDtoToEntity(okrDepartmentDto);
    okrDepartment.setId(null);
    okrDepartment = this.companyService.createDepartment(companyId, okrDepartment, user);
    return ResponseEntity.ok(departmentMapper.mapEntityToDto(okrDepartment));
  }

  @PostMapping("/companies/{companyId}/okrbranch")
  @PreAuthorize("@authorizationService.isAdmin()")
  public ResponseEntity<OkrBranchDTO> addBranchToCompanyById(
      @PathVariable long companyId, @Valid @RequestBody OkrBranchDTO okrBranchDTO, User user) {

    OkrBranch okrBranch = okrBranchMapper.mapDtoToEntity(okrBranchDTO);
    okrBranch.setId(null);
    OkrBranch newBranch = companyService.createOkrBranch(companyId, okrBranch, user);

    return ResponseEntity.ok(okrBranchMapper.mapEntityToDto(newBranch));
  }

  /**
   * API Endpoint to update a OkrCompany by ID.
   *
   * @param companyId a long value
   * @param okrCompanyDto a {@link OkrCompanyDto} object
   * @param user an {@link User} object
   * @return a {@link ResponseEntity} ok with a {@link Collection} of Companies
   */
  @PutMapping("/companies/{companyId}")
  @PreAuthorize("@authorizationService.isAdmin()")
  public ResponseEntity<OkrCompanyDto> updateCompanyById(
      @PathVariable long companyId, @RequestBody OkrCompanyDto okrCompanyDto, User user) {
    OkrCompany okrCompany = companyMapper.mapDtoToEntity(okrCompanyDto);
    okrCompany = this.companyService.updateCompany(okrCompany, user);
    return ResponseEntity.ok(companyMapper.mapEntityToDto(okrCompany));
  }

  /**
   * API Endpoint to delete a OkrCompany and it's history.
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
