package org.burningokr.controller.okr;

import org.burningokr.annotation.RestApiController;
import org.burningokr.dto.okr.CompanyStructureDto;
import org.burningokr.mapper.okr.CompanyStructureMapper;
import org.burningokr.model.okrUnits.OkrCompany;
import org.burningokr.service.okrUnit.CompanyService;
import org.burningokr.service.okrUnit.departmentservices.BranchHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;

@RestApiController
public class StructureController {

    private final CompanyStructureMapper companyStructureMapper;
    private CompanyService companyService;

    @Autowired
    public StructureController(
          CompanyService companyService,
          CompanyStructureMapper companyStructureMapper
    ){
        this.companyService = companyService;
        this.companyStructureMapper= companyStructureMapper;
    }


    /**
     * API Endpoint to get a Tree of allactive Structures with ChildUnits.
     *
     * @return a {@link ResponseEntity} ok with a {@link Collection} of Companies
     */
    @GetMapping("/CopmanyStructure")
    public ResponseEntity<Collection<CompanyStructureDto>> getAllCompanyStructures(){
        Collection<OkrCompany> okrCompanies = this.companyService.getAllCompanies();
        Collection<CompanyStructureDto> okrCompanyStructures = companyStructureMapper.mapEntitiesToDtos(okrCompanies);
        return ResponseEntity.ok(okrCompanyStructures);
    }

}
