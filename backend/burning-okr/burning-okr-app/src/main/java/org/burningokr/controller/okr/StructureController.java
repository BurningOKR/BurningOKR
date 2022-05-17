package org.burningokr.controller.okr;

import java.util.Collection;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.burningokr.annotation.RestApiController;
import org.burningokr.dto.okr.StructureDto;
import org.burningokr.mapper.okr.StructureMapper;
import org.burningokr.model.cycles.CycleState;
import org.burningokr.model.okrUnits.OkrCompany;
import org.burningokr.service.okrUnit.CompanyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
  public ResponseEntity<Collection<StructureDto>> getAllCompanyStructures(@RequestParam(name = "active", required = false) Boolean activeFilter) {

    Collection<OkrCompany> okrCompanies = this.companyService.getAllCompanies();
    Collection<StructureDto> structureDtos;

    if (activeFilter != null && activeFilter) {
      okrCompanies = okrCompanies.stream().filter(okrCompany -> okrCompany.getCycle().getCycleState() != CycleState.CLOSED).collect(Collectors.toList());
    } else if (activeFilter != null){
      okrCompanies = okrCompanies.stream().filter(okrCompany -> okrCompany.getCycle().getCycleState() == CycleState.CLOSED).collect(Collectors.toList());
    }

    structureDtos = structureMapper.mapCompaniesToStructureDtos(okrCompanies);
    return ResponseEntity.ok(structureDtos);
  }
}
