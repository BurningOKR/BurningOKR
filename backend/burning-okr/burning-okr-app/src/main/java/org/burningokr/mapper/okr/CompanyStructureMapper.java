package org.burningokr.mapper.okr;

import org.burningokr.service.okrUnit.departmentservices.BranchHelper;
import org.springframework.stereotype.Service;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.dto.okr.CompanyStructureDto;
import org.burningokr.model.okrUnits.OkrCompany;

import java.util.Collection;
import java.util.Collections;

@Service
public class CompanyStructureMapper implements DataMapper< OkrCompany, CompanyStructureDto > {

    @Override
    public OkrCompany mapDtoToEntity(CompanyStructureDto input) {
        return null;
    }

    @Override
    public CompanyStructureDto mapEntityToDto(OkrCompany input) {
        CompanyStructureDto output = new CompanyStructureDto();
        output.setId(input.getId());
        output.setName(input.getName());
        output.setChildUnits(BranchHelper.collectChildUnits(input));
        return output;
    }

    @Override
    public Collection<OkrCompany> mapDtosToEntities(Collection<CompanyStructureDto> input) {
        return null;
    }

    @Override
    public Collection<CompanyStructureDto> mapEntitiesToDtos(Collection<OkrCompany> input) {
        Collection<CompanyStructureDto> companyStructureDtos = Collections.emptyList();
        for (OkrCompany okrCompany : input) {
            companyStructureDtos.add(mapEntityToDto(okrCompany));
        }
        return null;
    }
}
