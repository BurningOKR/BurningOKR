package org.burningokr.mapper.okr;

import lombok.RequiredArgsConstructor;
import org.burningokr.mapper.okrUnit.OkrBranchSchemaMapper;
import org.burningokr.model.okrUnits.OkrBranch;
import org.burningokr.model.okrUnits.OkrChildUnit;
import org.burningokr.service.okrUnit.departmentservices.BranchHelper;
import org.burningokr.service.userhandling.UserService;
import org.springframework.stereotype.Service;
import org.burningokr.dto.okr.StructureDto;
import org.burningokr.model.okrUnits.OkrCompany;


import java.util.ArrayList;
import java.util.Collection;

@RequiredArgsConstructor
@Service
public class StructureMapper {

    private final UserService userService;
    private final OkrBranchSchemaMapper branchSchemaMapper;

    public StructureDto mapCompnayToStructureDto(OkrCompany input) {
        StructureDto structureDto = new StructureDto();
        structureDto.setOkrUnitId(input.getId());
        structureDto.setUnitName(input.getName());
        structureDto.setSubstructure(
                mapChildUnitsToStructureDtos(BranchHelper.collectChildUnits(input))
        );
        return structureDto;
    }

    public Collection<StructureDto> mapCompaniesToStructureDtos(Collection<OkrCompany> input) {
        Collection<StructureDto> structureDtos = new ArrayList<>();
        input.forEach(okrCompany -> structureDtos.add(mapCompnayToStructureDto(okrCompany)));
        return structureDtos;
    }

    public Collection<StructureDto> mapChildUnitsToStructureDtos(Collection<OkrChildUnit> input) {
        Collection<StructureDto> structureDtos = new ArrayList<>();
        input.forEach(childUnit -> {
            if(childUnit instanceof OkrBranch){
                structureDtos.add(mapChildUnitToStructureDto(childUnit));
            }
        }
        );
        return structureDtos;
    }

    private StructureDto mapChildUnitToStructureDto(OkrChildUnit input) {
        StructureDto structureDto = new StructureDto();
        structureDto.setOkrUnitId(input.getId());
        structureDto.setUnitName(input.getName());
        Collection<OkrChildUnit> childUnits = BranchHelper.collectChildUnitsWithoutSelf(input);
        if(!childUnits.isEmpty()){
            structureDto.setSubstructure(
                    mapChildUnitsToStructureDtos(childUnits)
            );
        }
        return structureDto;
    }

}
