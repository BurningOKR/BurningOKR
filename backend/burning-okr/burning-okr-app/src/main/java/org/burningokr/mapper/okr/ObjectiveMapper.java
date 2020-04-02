package org.burningokr.mapper.okr;

import java.util.ArrayList;
import java.util.Collection;
import org.burningokr.dto.okr.ObjectiveDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.structures.CompanyStructure;
import org.burningokr.model.structures.Department;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ObjectiveMapper implements DataMapper<Objective, ObjectiveDto> {

  private final Logger logger = LoggerFactory.getLogger(ObjectiveMapper.class);

  @Override
  public Objective mapDtoToEntity(ObjectiveDto objectiveDto) {
    Objective objective = new Objective();

    objective.setId(objectiveDto.getId());
    objective.setName(objectiveDto.getTitle());
    objective.setDescription(objectiveDto.getDescription());
    objective.setRemark(objectiveDto.getRemark());
    objective.setReview(objectiveDto.getReview());
    objective.setContactPersonId(objectiveDto.getContactPersonId());
    objective.setActive(objectiveDto.getIsActive());
    objective.setSequence(objectiveDto.getSequence());

    CompanyStructure parentStructure = null;
    if (objectiveDto.getParentStructureId() != null) {
      parentStructure = new Department();
      parentStructure.setId(objectiveDto.getParentStructureId());
    }
    objective.setParentStructure(parentStructure);

    Objective parentObjective = null;
    if (objectiveDto.hasParentObjectiveId()) {
      parentObjective = new Objective();
      parentObjective.setId(objectiveDto.getParentObjectiveId());
    }
    objective.setParentObjective(parentObjective);

    objective.setSubObjectives(new ArrayList<>());
    objective.setKeyResults(new ArrayList<>());

    logger.info("Mapped ObjectiveDto (id:" + objectiveDto.getId() + ") successful into Objective.");
    return objective;
  }

  @Override
  public ObjectiveDto mapEntityToDto(Objective objective) {
    ObjectiveDto objectiveDto = new ObjectiveDto();

    objectiveDto.setId(objective.getId());
    objectiveDto.setTitle(objective.getName());
    objectiveDto.setDescription(objective.getDescription());
    objectiveDto.setRemark(objective.getRemark());
    objectiveDto.setReview(objective.getReview());
    objectiveDto.setContactPersonId(objective.getContactPersonId());
    objectiveDto.setIsActive(objective.isActive());
    objectiveDto.setParentStructureId(objective.getParentStructure().getId());
    objectiveDto.setSequence(objective.getSequence());

    if (objective.hasParentObjective()) {
      objectiveDto.setParentObjectiveId(objective.getParentObjective().getId());
    }

    Collection<Long> subObjectiveIds = new ArrayList<>();
    for (Objective subObjective : objective.getSubObjectives()) {
      subObjectiveIds.add(subObjective.getId());
    }
    objectiveDto.setSubObjectiveIds(subObjectiveIds);

    Collection<Long> keyResultIds = new ArrayList<>();
    for (KeyResult keyResult : objective.getKeyResults()) {
      keyResultIds.add(keyResult.getId());
    }
    objectiveDto.setKeyResultIds(keyResultIds);

    logger.info("Mapped Objective (id:" + objective.getId() + ") successful into ObjectiveDto.");
    return objectiveDto;
  }

  @Override
  public Collection<Objective> mapDtosToEntities(Collection<ObjectiveDto> input) {
    Collection<Objective> objectives = new ArrayList<>();
    input.forEach(objectiveDto -> objectives.add(mapDtoToEntity(objectiveDto)));
    return objectives;
  }

  @Override
  public Collection<ObjectiveDto> mapEntitiesToDtos(Collection<Objective> objectives) {
    Collection<ObjectiveDto> objectiveDtos = new ArrayList<>();
    for (Objective objective : objectives) {
      ObjectiveDto objectiveDto = mapEntityToDto(objective);
      objectiveDtos.add(objectiveDto);
    }
    return objectiveDtos;
  }
}
