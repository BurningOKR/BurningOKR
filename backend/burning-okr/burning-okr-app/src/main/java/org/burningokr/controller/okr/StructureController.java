package org.burningokr.controller.okr;

import lombok.RequiredArgsConstructor;
import org.burningokr.annotation.RestApiController;
import org.burningokr.dto.okr.StructureDto;
import org.burningokr.mapper.okr.StructureMapper;
import org.burningokr.model.okrUnits.OkrCompany;
import org.burningokr.service.okrUnit.CompanyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;

@RequiredArgsConstructor
@RestApiController
public class StructureController {

  private final StructureMapper structureMapper;
  private final CompanyService companyService;

  /**
   * API Endpoint to get a Tree of all active Structures with ChildUnits.
   *
   * @return a {@link ResponseEntity} ok with a {@link Collection} of Companies
   */
  @GetMapping("/structure")
  public ResponseEntity<Collection<StructureDto>> getAllCompanyStructures(
      @RequestParam(name = "active", required = false) Boolean activeFilter,
      @RequestParam(name = "attachCycleName", defaultValue = "false") boolean attachCycleName
  ) {
    Collection<OkrCompany> okrCompanies;

    if (activeFilter != null) {
      okrCompanies = this.companyService.getCompaniesByActiveStatus(activeFilter);
    } else {
      okrCompanies = this.companyService.getAllCompanies();
    }

    if (attachCycleName) {
      companyService.attachCycleNameToCompanyName(okrCompanies);
    }

    return ResponseEntity.ok(structureMapper.mapCompaniesToStructureDtos(okrCompanies));
  }
}
