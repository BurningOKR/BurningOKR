package org.burningokr.mapper.structure;

import java.util.ArrayList;
import java.util.Collection;
import org.burningokr.dto.structure.DepartmentDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.structures.Department;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DepartmentMapper implements DataMapper<Department, DepartmentDto> {

  private final Logger logger = LoggerFactory.getLogger(DepartmentMapper.class);

  @Override
  public Department mapDtoToEntity(DepartmentDto departmentDto) {
    Department department = new Department();

    department.setId(departmentDto.getStructureId());
    department.setName(departmentDto.getStructureName());
    department.setLabel(departmentDto.getLabel());
    department.setParentStructure(null);
    department.setOkrMasterId(departmentDto.getOkrMasterId());
    department.setOkrTopicSponsorId(departmentDto.getOkrTopicSponsorId());
    department.setOkrMemberIds(departmentDto.getOkrMemberIds());
    department.setActive(departmentDto.getIsActive());

    department.setObjectives(new ArrayList<>());

    logger.info("Mapped DepartmentDto (id:" + departmentDto.getStructureId() + ") to Department.");
    return department;
  }

  @Override
  public DepartmentDto mapEntityToDto(Department department) {
    DepartmentDto departmentDto = new DepartmentDto();

    departmentDto.setStructureId(department.getId());
    departmentDto.setStructureName(department.getName());
    departmentDto.setLabel(department.getLabel());
    departmentDto.setParentStructureId(department.getParentStructure().getId());
    departmentDto.setIsParentStructureACorporateObjectiveStructure(
        department.getParentStructure() instanceof Department);
    departmentDto.setIsActive(department.isActive());

    Collection<Long> objectiveIds = new ArrayList<>();
    for (Objective objective : department.getObjectives()) {
      objectiveIds.add(objective.getId());
    }
    departmentDto.setObjectiveIds(objectiveIds);

    departmentDto.setOkrMasterId(department.getOkrMasterId());
    departmentDto.setOkrTopicSponsorId(department.getOkrTopicSponsorId());
    departmentDto.setOkrMemberIds(department.getOkrMemberIds());

    logger.info("Mapped Department (id:" + department.getId() + ") to DepartmentDto.");
    return departmentDto;
  }

  @Override
  public Collection<Department> mapDtosToEntities(Collection<DepartmentDto> input) {
    Collection<Department> departments = new ArrayList<>();
    input.forEach(departmentDto -> departments.add(mapDtoToEntity(departmentDto)));
    return departments;
  }

  @Override
  public Collection<DepartmentDto> mapEntitiesToDtos(Collection<Department> departments) {
    Collection<DepartmentDto> departmentDtos = new ArrayList<>();
    for (Department department : departments) {
      DepartmentDto departmentDto = mapEntityToDto(department);
      departmentDtos.add(departmentDto);
    }
    return departmentDtos;
  }
}
