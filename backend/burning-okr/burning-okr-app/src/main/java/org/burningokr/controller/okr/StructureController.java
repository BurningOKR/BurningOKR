package org.burningokr.controller.okr;

import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.burningokr.annotation.RestApiController;
import org.burningokr.dto.okr.StructureDto;
import org.burningokr.mapper.okr.StructureMapper;
import org.burningokr.mapper.okrUnit.OkrBranchSchemaMapper;
import org.burningokr.mapper.okrUnit.UnitMapperFactory;
import org.burningokr.model.okrUnits.OkrCompany;
import org.burningokr.service.okrUnit.CompanyService;
import org.burningokr.service.userhandling.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@RestApiController
public class StructureController {

  private final StructureMapper structureMapper;
  private final UnitMapperFactory mapperPicker;
  private final CompanyService companyService;
  private final UserService userService;
  private final OkrBranchSchemaMapper branchSchemaMapper;

  /**
   * API Endpoint to get a Tree of all active Structures with ChildUnits.
   *
   * @return a {@link ResponseEntity} ok with a {@link Collection} of Companies
   */
  @GetMapping("/structure")
  public ResponseEntity<Collection<StructureDto>> getAllCompanyStructures() {

    Collection<OkrCompany> okrCompanies = this.companyService.getAllCompanies();
    Collection<StructureDto> structureDtos =
        structureMapper.mapCompaniesToStructureDtos(okrCompanies);
    return ResponseEntity.ok(structureDtos);
  }
}
