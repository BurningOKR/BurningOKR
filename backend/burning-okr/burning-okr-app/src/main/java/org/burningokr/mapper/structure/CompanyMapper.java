package org.burningokr.mapper.structure;

import java.util.ArrayList;
import java.util.Collection;
import org.burningokr.dto.structure.CompanyDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.cycles.CompanyHistory;
import org.burningokr.model.cycles.Cycle;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.structures.Company;
import org.burningokr.model.structures.SubStructure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CompanyMapper implements DataMapper<Company, CompanyDto> {

  private final Logger logger = LoggerFactory.getLogger(CompanyMapper.class);

  @Override
  public Company mapDtoToEntity(CompanyDto companyDto) {
    Company company = new Company();

    company.setId(companyDto.getStructureId());
    company.setName(companyDto.getStructureName());
    company.setLabel(companyDto.getLabel());

    company.setSubStructures(new ArrayList<>());
    company.setObjectives(new ArrayList<>());

    Cycle cycle = null;
    if (companyDto.getCycleId() != null) {
      cycle = new Cycle();
      cycle.setId(companyDto.getCycleId());
    }
    company.setCycle(cycle);

    CompanyHistory history = null;
    if (companyDto.getHistoryId() != null) {
      history = new CompanyHistory();
      history.setId(companyDto.getHistoryId());
    }
    company.setHistory(history);

    logger.info("Mapped CompanyDto (id:" + companyDto.getStructureId() + ") to Company.");
    return company;
  }

  @Override
  public CompanyDto mapEntityToDto(Company company) {
    CompanyDto companyDto = new CompanyDto();

    companyDto.setStructureId(company.getId());
    companyDto.setStructureName(company.getName());
    companyDto.setLabel(company.getLabel());
    companyDto.setCycleId(company.getCycle().getId());
    companyDto.setHistoryId(company.getHistory().getId());

    Collection<Long> departmentIds = new ArrayList<>();
    for (SubStructure subStructure : company.getSubStructures()) {
      departmentIds.add(subStructure.getId());
    }
    companyDto.setDepartmentIds(departmentIds);

    Collection<Long> objectiveIds = new ArrayList<>();
    for (Objective objective : company.getObjectives()) {
      objectiveIds.add(objective.getId());
    }
    companyDto.setObjectiveIds(objectiveIds);

    logger.info("Mapped Company (id:" + company.getId() + ") to CompanyDto.");
    return companyDto;
  }

  @Override
  public Collection<Company> mapDtosToEntities(Collection<CompanyDto> input) {
    Collection<Company> entities = new ArrayList<>();
    input.forEach(companyDto -> entities.add(mapDtoToEntity(companyDto)));
    return entities;
  }

  @Override
  public Collection<CompanyDto> mapEntitiesToDtos(Collection<Company> companies) {
    Collection<CompanyDto> companyDtos = new ArrayList<>();
    for (Company company : companies) {
      CompanyDto companyDto = mapEntityToDto(company);
      companyDtos.add(companyDto);
    }
    return companyDtos;
  }
}
