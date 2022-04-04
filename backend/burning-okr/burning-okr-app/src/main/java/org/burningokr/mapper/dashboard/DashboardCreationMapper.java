package org.burningokr.mapper.dashboard;

import org.burningokr.dto.dashboard.DashboardCreationDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.dashboard.DashboardCreation;

import java.util.Collection;

public class DashboardCreationMapper implements DataMapper<DashboardCreation, DashboardCreationDto> {
  @Override
  public DashboardCreation mapDtoToEntity(DashboardCreationDto input) {
    return null;
  }

  @Override
  public DashboardCreationDto mapEntityToDto(DashboardCreation input) {
    return null;
  }

  @Override
  public Collection<DashboardCreation> mapDtosToEntities(Collection<DashboardCreationDto> input) {
    return null;
  }

  @Override
  public Collection<DashboardCreationDto> mapEntitiesToDtos(Collection<DashboardCreation> input) {
    return null;
  }
}
