package org.burningokr.mapper.okrUnit;

import java.util.ArrayList;
import java.util.Collection;
import org.burningokr.dto.okrUnit.OkrBranchDTO;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.okrUnits.OkrBranch;
import org.springframework.stereotype.Service;

@Service
public class OkrBranchMapper implements DataMapper<OkrBranch, OkrBranchDTO> {
  @Override
  public OkrBranch mapDtoToEntity(OkrBranchDTO dto) {
    OkrBranch entity = new OkrBranch();
    entity.setId(dto.getOkrUnitId());
    entity.setName(dto.getUnitName());
    entity.setLabel(dto.getLabel());
    entity.setActive(dto.getIsActive());
    return entity;
  }

  @Override
  public OkrBranchDTO mapEntityToDto(OkrBranch entity) {
    OkrBranchDTO dto = new OkrBranchDTO();
    dto.setOkrUnitId(entity.getId());
    dto.setUnitName(entity.getName());
    dto.setIsActive(entity.isActive());
    dto.setLabel(entity.getLabel());
    dto.setParentUnitId(
        entity.getParentOkrUnit() != null ? entity.getParentOkrUnit().getId() : null);
    entity.getOkrChildUnits().forEach(department -> dto.getChildUnits().add(department.getId()));
    entity.getObjectives().forEach(objective -> dto.getObjectiveIds().add(objective.getId()));
    dto.setIsParentUnitAOkrBranch(entity.getParentOkrUnit() instanceof OkrBranch);
    return dto;
  }

  @Override
  public Collection<OkrBranch> mapDtosToEntities(Collection<OkrBranchDTO> input) {
    Collection<OkrBranch> entities = new ArrayList<>();
    input.forEach(okrBranchDto -> entities.add(mapDtoToEntity(okrBranchDto)));
    return entities;
  }

  @Override
  public Collection<OkrBranchDTO> mapEntitiesToDtos(Collection<OkrBranch> entities) {
    Collection<OkrBranchDTO> dtos = new ArrayList<>();
    entities.forEach(entity -> dtos.add(mapEntityToDto(entity)));
    return dtos;
  }
}
