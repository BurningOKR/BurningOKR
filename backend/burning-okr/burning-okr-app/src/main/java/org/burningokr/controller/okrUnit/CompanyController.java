package org.burningokr.controller.okrUnit;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.formula.eval.NotImplementedException;
import org.burningokr.annotation.RestApiController;
import org.burningokr.dto.cycle.CycleDto;
import org.burningokr.dto.okrUnit.OkrBranchDto;
import org.burningokr.dto.okrUnit.OkrCompanyDto;
import org.burningokr.dto.okrUnit.OkrDepartmentDto;
import org.burningokr.dto.okrUnit.OkrUnitSchemaDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.mapper.okrUnit.OkrBranchSchemaMapper;
import org.burningokr.mapper.okrUnit.OkrChildUnitMapper;
import org.burningokr.mapper.okrUnit.OkrDepartmentMapper;
import org.burningokr.model.cycles.Cycle;
import org.burningokr.model.cycles.CycleState;
import org.burningokr.model.okrUnits.OkrBranch;
import org.burningokr.model.okrUnits.OkrChildUnitSchema;
import org.burningokr.model.okrUnits.OkrCompany;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.burningokr.model.users.IUser;
import org.burningokr.service.okrUnit.CompanyService;
import org.burningokr.service.security.AuthorizationUserContextService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;

@RestApiController
@RequiredArgsConstructor
public class CompanyController {

  private final CompanyService companyService;
  private final DataMapper<OkrCompany, OkrCompanyDto> companyMapper;
  private final DataMapper<Cycle, CycleDto> cycleMapper;
  private final DataMapper<OkrBranch, OkrBranchDto> okrBranchMapper;
  private final OkrBranchSchemaMapper okrUnitSchemaMapper;
  private final AuthorizationUserContextService authorizationUserContextService;

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
  public ResponseEntity<OkrCompanyDto> getCompanyById(
    @PathVariable long companyId
  ) {
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
    @PathVariable long companyId
  ) {
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
  public ResponseEntity<Collection<CycleDto>> getCompanyCycleList(
    @PathVariable long companyId
  ) {
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
    @PathVariable long companyId
  ) {
    OkrCompany okrCompany = this.companyService.findById(companyId);
    return ResponseEntity.ok(
      okrUnitSchemaMapper.mapOkrChildUnitListToOkrChildUnitSchemaList(
        okrCompany.getOkrChildUnits(), authorizationUserContextService.getAuthenticatedUser().getId()
      )
    );
  }

  /**
   * API Endpoint to add a OkrCompany.
   *
   * @param okrCompanyDto a {@link OkrCompanyDto} object
   * @param IUser         an {@link IUser} object
   * @return a {@link ResponseEntity} ok with a OkrCompany
   */
  @PostMapping("/companies")
  @PreAuthorize("@authorizationService.isAdmin()")
  public ResponseEntity<OkrCompanyDto> addCompany(
    @Valid
    @RequestBody
    OkrCompanyDto okrCompanyDto, IUser IUser
  ) {
    OkrCompany okrCompany = companyMapper.mapDtoToEntity(okrCompanyDto);
    okrCompany = this.companyService.createCompany(okrCompany, IUser);
    return ResponseEntity.ok(companyMapper.mapEntityToDto(okrCompany));
  }

  /**
   * API Endpoint to add an Objective to a OkrDepartment.
   *
   * @param companyId        a ong value
   * @param okrDepartmentDto a {@link OkrDepartmentDto} object
   * @param IUser            an {@link IUser} object
   * @return a {@link ResponseEntity} ok with a OkrDepartment
   */
  @PostMapping("/companies/{companyId}/departments")
  @PreAuthorize("@authorizationService.isAdmin()")
  public ResponseEntity<OkrDepartmentDto> addDepartmentToCompanyById(
    @PathVariable long companyId,
    @Valid
    @RequestBody
    OkrDepartmentDto okrDepartmentDto,
    IUser IUser
  ) {
    DataMapper<OkrDepartment, OkrDepartmentDto> departmentMapper = new OkrDepartmentMapper();
    OkrDepartment okrDepartment = departmentMapper.mapDtoToEntity(okrDepartmentDto);
    okrDepartment.setId(null);
    okrDepartment = this.companyService.createDepartment(companyId, okrDepartment, IUser);
    return ResponseEntity.ok(departmentMapper.mapEntityToDto(okrDepartment));
  }

  @PostMapping("/companies/{companyId}/branch")
  @PreAuthorize("@authorizationService.isAdmin()")
  public ResponseEntity<OkrBranchDto> addBranchToCompanyById(
    @PathVariable long companyId,
    @Valid
    @RequestBody
    OkrBranchDto okrBranchDTO,
    IUser IUser
  ) {

    OkrBranch okrBranch = okrBranchMapper.mapDtoToEntity(okrBranchDTO);
    okrBranch.setId(null);
    OkrBranch newBranch = companyService.createOkrBranch(companyId, okrBranch, IUser);

    return ResponseEntity.ok(okrBranchMapper.mapEntityToDto(newBranch));
  }

  /**
   * API Endpoint to update a OkrCompany by ID.
   *
   * @param companyId     a long value
   * @param okrCompanyDto a {@link OkrCompanyDto} object
   * @param IUser         an {@link IUser} object
   * @return a {@link ResponseEntity} ok with a {@link Collection} of Companies
   */
  @PutMapping("/companies/{companyId}")
  @PreAuthorize("@authorizationService.isAdmin()")
  public ResponseEntity<OkrCompanyDto> updateCompanyById(
    @PathVariable long companyId,
    @RequestBody OkrCompanyDto okrCompanyDto, IUser IUser
  ) {
    OkrCompany okrCompany = companyMapper.mapDtoToEntity(okrCompanyDto);
    okrCompany = this.companyService.updateCompany(okrCompany, IUser);
    return ResponseEntity.ok(companyMapper.mapEntityToDto(okrCompany));
  }

  /**
   * API Endpoint to delete a OkrCompany and it's history.
   *
   * @param companyId a long value
   * @param IUser     an {@link IUser} object
   * @return a {@link ResponseEntity} ok
   */
  @DeleteMapping("/companies/{companyId}")
  @PreAuthorize("@authorizationService.isAdmin()")
  public ResponseEntity deleteCompany(
    @PathVariable Long companyId, IUser IUser
  ) {
    companyService.deleteCompany(companyId, true, IUser);
    return ResponseEntity.ok().build();
  }
}
