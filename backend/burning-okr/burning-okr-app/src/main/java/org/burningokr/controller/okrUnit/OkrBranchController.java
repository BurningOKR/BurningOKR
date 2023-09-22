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
import org.springframework.http.MediaType;
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

  @PostMapping(
      value = "/branch/{unitId}/department",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  @PreAuthorize("@authorizationService.isAdmin()")
  public ResponseEntity<OkrDepartmentDto> addSubDepartmentToBranch(
      @PathVariable long unitId,
      @Valid
      @RequestBody
      OkrDepartmentDto okrDepartmentDto
  )
      throws DuplicateTeamMemberException {

    OkrDepartment okrDepartment = departmentMapper.mapDtoToEntity(okrDepartmentDto);
    okrDepartment = (OkrDepartment) okrBranchService.createChildUnit(unitId, okrDepartment);
    return ResponseEntity.ok(departmentMapper.mapEntityToDto(okrDepartment));
  }

  @PostMapping(
      value = "/branch/{unitId}/branch",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  @PreAuthorize("@authorizationService.isAdmin()")
  public ResponseEntity<OkrBranchDto> addSubBranchToBranch(
      @PathVariable long unitId,
      @Valid
      @RequestBody
      OkrBranchDto okrBranchDTO
  )
      throws DuplicateTeamMemberException {
    OkrBranch okrBranch = okrBranchMapper.mapDtoToEntity(okrBranchDTO);
    okrBranch = (OkrBranch) okrBranchService.createChildUnit(unitId, okrBranch);
    return ResponseEntity.ok(okrBranchMapper.mapEntityToDto(okrBranch));
  }
}
