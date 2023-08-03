package org.burningokr.mapper.okrUnit;

import org.burningokr.dto.okrUnit.OkrBranchDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.okrUnits.OkrBranch;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class OkrBranchMapper implements DataMapper<OkrBranch, OkrBranchDto> {
  @Override
  public OkrBranch mapDtoToEntity(OkrBranchDto dto) {
    OkrBranch entity = new OkrBranch();
    entity.setId(dto.getOkrUnitId());
    entity.setName(dto.getUnitName());
    entity.setPhoto(dto.getPhoto());
    entity.setLabel(dto.getLabel());
    entity.setActive(dto.getIsActive());
    return entity;
  }

  @Override
  public OkrBranchDto mapEntityToDto(OkrBranch entity) {
    OkrBranchDto dto = new OkrBranchDto();
    dto.setOkrUnitId(entity.getId());
    dto.setUnitName(entity.getName());
    dto.setPhoto(entity.getPhoto());
    dto.setIsActive(entity.isActive());
    dto.setLabel(entity.getLabel());
    dto.setParentUnitId(
      entity.getParentOkrUnit() != null ? entity.getParentOkrUnit().getId() : null);
    entity
      .getOkrChildUnits()
      .forEach(department -> dto.getOkrChildUnitIds().add(department.getId()));
    entity.getObjectives().forEach(objective -> dto.getObjectiveIds().add(objective.getId()));
    dto.setIsParentUnitABranch(entity.getParentOkrUnit() instanceof OkrBranch);
    return dto;
  }

  @Override
  public Collection<OkrBranch> mapDtosToEntities(Collection<OkrBranchDto> input) {
    Collection<OkrBranch> entities = new ArrayList<>();
    input.forEach(okrBranchDto -> entities.add(mapDtoToEntity(okrBranchDto)));
    return entities;
  }

  @Override
  public Collection<OkrBranchDto> mapEntitiesToDtos(Collection<OkrBranch> entities) {
    Collection<OkrBranchDto> dtos = new ArrayList<>();
    entities.forEach(entity -> dtos.add(mapEntityToDto(entity)));
    return dtos;
  }
}
