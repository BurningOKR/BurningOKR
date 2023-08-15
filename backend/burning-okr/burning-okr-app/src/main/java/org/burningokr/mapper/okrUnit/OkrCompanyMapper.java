package org.burningokr.mapper.okrUnit;

import lombok.extern.slf4j.Slf4j;
import org.burningokr.dto.okrUnit.OkrCompanyDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.cycles.Cycle;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.okrUnits.OkrChildUnit;
import org.burningokr.model.okrUnits.OkrCompany;
import org.burningokr.model.okrUnits.okrUnitHistories.OkrCompanyHistory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Slf4j
@Service
public class OkrCompanyMapper implements DataMapper<OkrCompany, OkrCompanyDto> {

  @Override
  public OkrCompany mapDtoToEntity(OkrCompanyDto okrCompanyDto) {
    OkrCompany okrCompany = new OkrCompany();

    okrCompany.setId(okrCompanyDto.getOkrUnitId());
    okrCompany.setName(okrCompanyDto.getUnitName());
    okrCompany.setPhoto(okrCompanyDto.getPhoto());
    okrCompany.setLabel(okrCompanyDto.getLabel());

    okrCompany.setOkrChildUnits(new ArrayList<>());
    okrCompany.setObjectives(new ArrayList<>());

    Cycle cycle = null;
    if (okrCompanyDto.getCycleId() != null) {
      cycle = new Cycle();
      cycle.setId(okrCompanyDto.getCycleId());
    }
    okrCompany.setCycle(cycle);

    OkrCompanyHistory history = null;
    if (okrCompanyDto.getHistoryId() != null) {
      history = new OkrCompanyHistory();
      history.setId(okrCompanyDto.getHistoryId());
    }
    okrCompany.setCompanyHistory(history);

    log.debug("Mapped OkrCompanyDto (id: %d) to OkrCompany.".formatted(okrCompanyDto.getOkrUnitId()));
    return okrCompany;
  }

  @Override
  public OkrCompanyDto mapEntityToDto(OkrCompany okrCompany) {
    OkrCompanyDto okrCompanyDto = new OkrCompanyDto();

    okrCompanyDto.setOkrUnitId(okrCompany.getId());
    okrCompanyDto.setUnitName(okrCompany.getName());
    okrCompanyDto.setPhoto(okrCompany.getPhoto());
    okrCompanyDto.setLabel(okrCompany.getLabel());
    okrCompanyDto.setCycleId(okrCompany.getCycle().getId());
    okrCompanyDto.setHistoryId(okrCompany.getCompanyHistory().getId());

    Collection<Long> departmentIds = new ArrayList<>();
    for (OkrChildUnit okrChildUnit : okrCompany.getOkrChildUnits()) {
      departmentIds.add(okrChildUnit.getId());
    }
    okrCompanyDto.setOkrChildUnitIds(departmentIds);

    Collection<Long> objectiveIds = new ArrayList<>();
    for (Objective objective : okrCompany.getObjectives()) {
      objectiveIds.add(objective.getId());
    }
    okrCompanyDto.setObjectiveIds(objectiveIds);

    log.debug("Mapped OkrCompany (id: %d) to OkrCompanyDto.".formatted(okrCompany.getId()));
    return okrCompanyDto;
  }

  @Override
  public Collection<OkrCompany> mapDtosToEntities(Collection<OkrCompanyDto> input) {
    Collection<OkrCompany> entities = new ArrayList<>();
    input.forEach(companyDto -> entities.add(mapDtoToEntity(companyDto)));
    return entities;
  }

  @Override
  public Collection<OkrCompanyDto> mapEntitiesToDtos(Collection<OkrCompany> companies) {
    Collection<OkrCompanyDto> okrCompanyDtos = new ArrayList<>();
    for (OkrCompany okrCompany : companies) {
      OkrCompanyDto okrCompanyDto = mapEntityToDto(okrCompany);
      okrCompanyDtos.add(okrCompanyDto);
    }
    return okrCompanyDtos;
  }
}
