package org.burningokr.mapper.okr;

import lombok.RequiredArgsConstructor;
import org.burningokr.mapper.okrUnit.OkrBranchSchemaMapper;
import org.burningokr.service.userhandling.UserService;
import org.springframework.stereotype.Service;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.dto.okr.CompanyStructureDto;
import org.burningokr.model.okrUnits.OkrCompany;


import java.util.ArrayList;
import java.util.Collection;

@RequiredArgsConstructor
@Service
public class CompanyStructureMapper implements DataMapper< OkrCompany, CompanyStructureDto > {

    private final UserService userService;
    private final OkrBranchSchemaMapper branchSchemaMapper;


    @Override
    public OkrCompany mapDtoToEntity(CompanyStructureDto input) {
        return null;
    }

    @Override
    public CompanyStructureDto mapEntityToDto(OkrCompany input) {
        CompanyStructureDto companyStructureDto = new CompanyStructureDto();
        companyStructureDto.setOkrUnitId(input.getId());
        companyStructureDto.setUnitName(input.getName());
        companyStructureDto.setUnitSchema(
                branchSchemaMapper.mapOkrChildUnitListToOkrChildUnitSchemaList(
                        input.getOkrChildUnits(),
                        userService.getCurrentUser().getId())
        );
        return companyStructureDto;
    }

    @Override
    public Collection<OkrCompany> mapDtosToEntities(Collection<CompanyStructureDto> input) {
        return null;
    }

    @Override
    public Collection<CompanyStructureDto> mapEntitiesToDtos(Collection<OkrCompany> input) {
        Collection<CompanyStructureDto> companyStructureDtos = new ArrayList<>();
        input.forEach(okrCompany -> companyStructureDtos.add(mapEntityToDto(okrCompany)));
        return companyStructureDtos;
    }
}
