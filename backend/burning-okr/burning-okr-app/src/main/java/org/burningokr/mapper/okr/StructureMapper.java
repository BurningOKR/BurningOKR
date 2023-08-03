package org.burningokr.mapper.okr;

import lombok.RequiredArgsConstructor;
import org.burningokr.dto.okr.StructureDto;
import org.burningokr.model.okrUnits.OkrBranch;
import org.burningokr.model.okrUnits.OkrChildUnit;
import org.burningokr.model.okrUnits.OkrCompany;
import org.burningokr.service.okrUnit.departmentservices.BranchHelper;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class StructureMapper {

  public StructureDto mapCompanyToStructureDto(OkrCompany input) {
    StructureDto structureDto = new StructureDto();
    structureDto.setOkrUnitId(input.getId());
    structureDto.setUnitName(input.getName());
    structureDto.setPhoto(input.getPhoto());
    structureDto.setSubstructure(
      mapChildUnitsToStructureDtos(BranchHelper.collectDirectChildUnits(input)));
    return structureDto;
  }

  public Collection<StructureDto> mapCompaniesToStructureDtos(Collection<OkrCompany> input) {
    return input.stream().map(this::mapCompanyToStructureDto).collect(Collectors.toList());
  }

  private Collection<StructureDto> mapChildUnitsToStructureDtos(Collection<OkrChildUnit> input) {
    return input.stream()
      .filter(OkrBranch.class::isInstance)
      .map(this::mapChildUnitToStructureDto)
      .collect(Collectors.toList());
  }

  private StructureDto mapChildUnitToStructureDto(OkrChildUnit input) {
    StructureDto structureDto = new StructureDto();
    structureDto.setOkrUnitId(input.getId());
    structureDto.setUnitName(input.getName());
    structureDto.setPhoto(input.getPhoto());
    Collection<OkrChildUnit> childUnits = BranchHelper.collectChildUnitsWithoutSelf(input);
    if (!childUnits.isEmpty()) {
      structureDto.setSubstructure(mapChildUnitsToStructureDtos(childUnits));
    }
    return structureDto;
  }
}
