package org.burningokr.controller.okr;

import lombok.RequiredArgsConstructor;
import org.burningokr.annotation.RestApiController;
import org.burningokr.dto.okr.CompanyStructureDto;
import org.burningokr.dto.okrUnit.OkrChildUnitDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.mapper.okr.CompanyStructureMapper;
import org.burningokr.mapper.okrUnit.OkrBranchMapper;
import org.burningokr.mapper.okrUnit.OkrBranchSchemaMapper;
import org.burningokr.mapper.okrUnit.UnitMapperFactory;
import org.burningokr.model.okrUnits.OkrBranch;
import org.burningokr.model.okrUnits.OkrChildUnit;
import org.burningokr.model.okrUnits.OkrCompany;
import org.burningokr.service.okrUnit.CompanyService;
import org.burningokr.service.okrUnit.departmentservices.BranchHelper;
import org.burningokr.service.userhandling.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;
import java.util.UUID;

@RequiredArgsConstructor
@RestApiController
public class StructureController {

    private final CompanyStructureMapper companyStructureMapper;
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
    public ResponseEntity<Collection<CompanyStructureDto>> getAllCompanyStructures(){

        Collection<OkrCompany> okrCompanies = this.companyService.getAllCompanies();
        Collection<CompanyStructureDto> CompanyStructureDtos = companyStructureMapper.mapEntitiesToDtos(okrCompanies);
        return ResponseEntity.ok(CompanyStructureDtos);
    }
}
