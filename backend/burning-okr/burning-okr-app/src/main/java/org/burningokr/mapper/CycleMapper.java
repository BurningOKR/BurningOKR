package org.burningokr.mapper;

import org.burningokr.dto.cycle.CycleDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.cycles.Cycle;
import org.burningokr.model.okrUnits.OkrCompany;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class CycleMapper implements DataMapper<Cycle, CycleDto> {

  private final Logger logger = LoggerFactory.getLogger(CycleMapper.class);

  @Override
  public Cycle mapDtoToEntity(CycleDto cycleDto) {
    Cycle cycle = new Cycle();
    cycle.setId(cycleDto.getId());
    cycle.setName(cycleDto.getName());
    cycle.setPlannedStartDate(cycleDto.getPlannedStartDate());
    cycle.setPlannedEndDate(cycleDto.getPlannedEndDate());
    cycle.setCycleState(cycleDto.getCycleState());
    cycle.setVisible(cycleDto.getIsVisible());

    logger.info("Mapped CycleDto (id:" + cycleDto.getId() + ") to Cycle.");
    return cycle;
  }

  @Override
  public CycleDto mapEntityToDto(Cycle cycle) {
    CycleDto cycleDto = new CycleDto();

    cycleDto.setId(cycle.getId());
    cycleDto.setName(cycle.getName());
    cycleDto.setPlannedStartDate(cycle.getPlannedStartDate());
    cycleDto.setPlannedEndDate(cycle.getPlannedEndDate());
    cycleDto.setCycleState(cycle.getCycleState());
    cycleDto.setIsVisible(cycle.isVisible());

    Collection<Long> companyIds = new ArrayList<>();
    for (OkrCompany okrCompany : cycle.getCompanies()) {
      companyIds.add(okrCompany.getId());
    }
    cycleDto.setCompanyIds(companyIds);

    logger.info("Mapped Cycle (id:" + cycle.getId() + ") to CycleDto.");
    return cycleDto;
  }

  @Override
  public Collection<Cycle> mapDtosToEntities(Collection<CycleDto> input) {
    Collection<Cycle> entities = new ArrayList<>();
    input.forEach(cycleDto -> entities.add(mapDtoToEntity(cycleDto)));
    return entities;
  }

  @Override
  public Collection<CycleDto> mapEntitiesToDtos(Collection<Cycle> cycles) {
    Collection<CycleDto> cycleDtos = new ArrayList<>();
    for (Cycle cycle : cycles) {
      CycleDto cycleDto = mapEntityToDto(cycle);
      cycleDtos.add(cycleDto);
    }
    return cycleDtos;
  }
}
