package org.burningokr.mapper.okrUnit;

import lombok.extern.slf4j.Slf4j;
import org.burningokr.dto.okrUnit.OkrDepartmentDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.okrUnits.OkrBranch;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Slf4j
@Service
public class OkrDepartmentMapper implements DataMapper<OkrDepartment, OkrDepartmentDto> {


  @Override
  public OkrDepartment mapDtoToEntity(OkrDepartmentDto okrDepartmentDto) {
    OkrDepartment okrDepartment = new OkrDepartment();

    okrDepartment.setId(okrDepartmentDto.getOkrUnitId());
    okrDepartment.setName(okrDepartmentDto.getUnitName());
    okrDepartment.setLabel(okrDepartmentDto.getLabel());
    okrDepartment.setParentOkrUnit(null);
    okrDepartment.setOkrMasterId(okrDepartmentDto.getOkrMasterId());
    okrDepartment.setOkrTopicSponsorId(okrDepartmentDto.getOkrTopicSponsorId());
    okrDepartment.setOkrMemberIds(okrDepartmentDto.getOkrMemberIds());
    okrDepartment.setActive(okrDepartmentDto.getIsActive());

    okrDepartment.setObjectives(new ArrayList<>());

    log.debug("Mapped OkrDepartmentDto (id: %d) to OkrDepartment.".formatted(okrDepartmentDto.getOkrUnitId()));
    return okrDepartment;
  }

  @Override
  public OkrDepartmentDto mapEntityToDto(OkrDepartment okrDepartment) {
    OkrDepartmentDto okrDepartmentDto = new OkrDepartmentDto();

    okrDepartmentDto.setOkrUnitId(okrDepartment.getId());
    okrDepartmentDto.setUnitName(okrDepartment.getName());
    okrDepartmentDto.setLabel(okrDepartment.getLabel());
    okrDepartmentDto.setParentUnitId(okrDepartment.getParentOkrUnit().getId());
    okrDepartmentDto.setIsParentUnitABranch(okrDepartment.getParentOkrUnit() instanceof OkrBranch);
    okrDepartmentDto.setIsActive(okrDepartment.isActive());

    Collection<Long> objectiveIds = new ArrayList<>();
    for (Objective objective : okrDepartment.getObjectives()) {
      objectiveIds.add(objective.getId());
    }
    okrDepartmentDto.setObjectiveIds(objectiveIds);

    okrDepartmentDto.setOkrMasterId(okrDepartment.getOkrMasterId());
    okrDepartmentDto.setOkrTopicSponsorId(okrDepartment.getOkrTopicSponsorId());
    okrDepartmentDto.setOkrMemberIds(okrDepartment.getOkrMemberIds());

    log.debug("Mapped OkrDepartment (id: %d) to OkrDepartmentDto.".formatted(okrDepartment.getId()));
    return okrDepartmentDto;
  }

  @Override
  public Collection<OkrDepartment> mapDtosToEntities(Collection<OkrDepartmentDto> input) {
    Collection<OkrDepartment> okrDepartments = new ArrayList<>();
    input.forEach(departmentDto -> okrDepartments.add(mapDtoToEntity(departmentDto)));
    return okrDepartments;
  }

  @Override
  public Collection<OkrDepartmentDto> mapEntitiesToDtos(Collection<OkrDepartment> okrDepartments) {
    Collection<OkrDepartmentDto> okrDepartmentDtos = new ArrayList<>();
    for (OkrDepartment okrDepartment : okrDepartments) {
      OkrDepartmentDto okrDepartmentDto = mapEntityToDto(okrDepartment);
      okrDepartmentDtos.add(okrDepartmentDto);
    }
    return okrDepartmentDtos;
  }
}
