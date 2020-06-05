package org.burningokr.controller.structure;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.burningokr.annotation.RestApiController;
import org.burningokr.dto.structure.CorporateObjectiveStructureDto;
import org.burningokr.dto.structure.DepartmentDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.structures.CorporateObjectiveStructure;
import org.burningokr.model.structures.Department;
import org.burningokr.model.users.User;
import org.burningokr.service.exceptions.DuplicateTeamMemberException;
import org.burningokr.service.structure.StructureService;
import org.burningokr.service.structure.StructureServicePicker;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestApiController
@RequiredArgsConstructor
public class CorporateObjectiveStructureController {

  private final StructureServicePicker<CorporateObjectiveStructure>
      corporateObjectiveStructurePicker;
  private final DataMapper<Department, DepartmentDto> departmentMapper;
  private final DataMapper<CorporateObjectiveStructure, CorporateObjectiveStructureDto>
      corporateObjectiveStructureMapper;

  /**
   * API Endpoint to add a sub-Department to an existing CorporateObjectiveStructure.
   *
   * @param structureId a long value
   * @param departmentDto a {@link DepartmentDto} object
   * @param user an {@link User} object
   * @return a {@link ResponseEntity} ok with the added sub-Department
   */
  @PostMapping("/corporateobjectivestructure/{structureId}/subdepartments")
  @PreAuthorize("@authorizationService.isAdmin()")
  public ResponseEntity<DepartmentDto> addSubDepartmentToCorporateObjectiveStructure(
      @PathVariable long structureId, @Valid @RequestBody DepartmentDto departmentDto, User user)
      throws DuplicateTeamMemberException {
    StructureService<CorporateObjectiveStructure> corporateObjectiveStructureService =
        corporateObjectiveStructurePicker.getRoleServiceForDepartment(structureId);
    Department department = departmentMapper.mapDtoToEntity(departmentDto);
    department.setId(null);
    department =
        (Department)
            corporateObjectiveStructureService.createSubstructure(structureId, department, user);
    return ResponseEntity.ok(departmentMapper.mapEntityToDto(department));
  }

  /**
   * API Endpoint to add a sub-CorporateObjectiveStructure to an existing
   * CorporateObjectiveStructure.
   *
   * @param structureId a long value
   * @param corporateObjectiveStructureDto a {@link CorporateObjectiveStructureDto} object
   * @param user an {@link User} object
   * @return a {@link ResponseEntity} ok with the added sub-CorporateObjectiveStructure
   */
  @PostMapping("/corporateobjectivestructure/{structureId}/corporateobjectivestructures")
  @PreAuthorize("@authorizationService.isAdmin()")
  public ResponseEntity<CorporateObjectiveStructureDto>
      addSubCorporateObjectiveStructureToCorporateObjectiveStructure(
          @PathVariable long structureId,
          @Valid @RequestBody CorporateObjectiveStructureDto corporateObjectiveStructureDto,
          User user)
          throws DuplicateTeamMemberException {
    StructureService<CorporateObjectiveStructure> corporateObjectiveStructureService =
        corporateObjectiveStructurePicker.getRoleServiceForDepartment(structureId);
    CorporateObjectiveStructure corporateObjectiveStructure =
        corporateObjectiveStructureMapper.mapDtoToEntity(corporateObjectiveStructureDto);
    corporateObjectiveStructure.setId(null);
    corporateObjectiveStructure =
        (CorporateObjectiveStructure)
            corporateObjectiveStructureService.createSubstructure(
                structureId, corporateObjectiveStructure, user);
    return ResponseEntity.ok(
        corporateObjectiveStructureMapper.mapEntityToDto(corporateObjectiveStructure));
  }
}
