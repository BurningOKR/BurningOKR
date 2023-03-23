package org.burningokr.controller.okrUnit;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.burningokr.annotation.RestApiController;
import org.burningokr.dto.okrUnit.OkrBranchDto;
import org.burningokr.dto.okrUnit.OkrDepartmentDto;
import org.burningokr.mapper.okrUnit.OkrBranchMapper;
import org.burningokr.mapper.okrUnit.OkrDepartmentMapper;
import org.burningokr.model.okrUnits.OkrBranch;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.burningokr.service.exceptions.DuplicateTeamMemberException;
import org.burningokr.service.okrUnit.OkrChildUnitService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestApiController
@RequiredArgsConstructor
public class OkrBranchController {
  private final OkrChildUnitService<OkrBranch> okrBranchService;
  private final OkrDepartmentMapper departmentMapper;
  private final OkrBranchMapper okrBranchMapper;

  @PostMapping("/branch/{unitId}/department")
  @PreAuthorize("@authorizationService.isAdmin()")
  public ResponseEntity<OkrDepartmentDto> addSubDepartmentToBranch(
    @PathVariable long unitId,
    @Valid
    @RequestBody
    OkrDepartmentDto okrDepartmentDto
  )
    throws DuplicateTeamMemberException {

    OkrDepartment okrDepartment = departmentMapper.mapDtoToEntity(okrDepartmentDto);
    okrDepartment.setId(null);
    okrDepartment = (OkrDepartment) okrBranchService.createChildUnit(unitId, okrDepartment);
    return ResponseEntity.ok(departmentMapper.mapEntityToDto(okrDepartment));
  }

  @PostMapping("/branch/{unitId}/branch")
  @PreAuthorize("@authorizationService.isAdmin()")
  public ResponseEntity<OkrBranchDto> addSubBranchToBranch(
    @PathVariable long unitId,
    @Valid
    @RequestBody
    OkrBranchDto okrBranchDTO
  )
    throws DuplicateTeamMemberException {
    OkrBranch okrBranch = okrBranchMapper.mapDtoToEntity(okrBranchDTO);
    okrBranch.setId(null);
    okrBranch = (OkrBranch) okrBranchService.createChildUnit(unitId, okrBranch);
    return ResponseEntity.ok(okrBranchMapper.mapEntityToDto(okrBranch));
  }
}
