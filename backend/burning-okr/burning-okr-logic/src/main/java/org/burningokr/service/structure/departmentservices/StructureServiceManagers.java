package org.burningokr.service.structure.departmentservices;

import org.burningokr.model.activity.Action;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.structures.Department;
import org.burningokr.model.structures.SubStructure;
import org.burningokr.model.users.User;
import org.burningokr.repositories.okr.ObjectiveRepository;
import org.burningokr.repositories.structre.StructureRepository;
import org.burningokr.service.activity.ActivityService;
import org.burningokr.service.structureutil.EntityCrawlerService;
import org.burningokr.service.structureutil.ParentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("structureServiceManagers")
public class StructureServiceManagers<T extends SubStructure> extends StructureServiceUsers<T> {

  @Autowired
  StructureServiceManagers(
      ParentService parentService,
      StructureRepository<T> structureRepository,
      ObjectiveRepository objectiveRepository,
      ActivityService activityService,
      EntityCrawlerService entityCrawlerService) {
    super(
        parentService,
        structureRepository,
        objectiveRepository,
        activityService,
        entityCrawlerService);
  }

  @Override
  @Transactional
  public T updateStructure(T updatedStructure, User user) {
    T referencedStructure = structureRepository.findByIdOrThrow(updatedStructure.getId());

    throwIfCycleForDepartmentIsClosed(referencedStructure);

    if (updatedStructure instanceof Department) {
      ((Department) referencedStructure)
          .setOkrMemberIds(((Department) updatedStructure).getOkrMemberIds());

      referencedStructure = structureRepository.save(referencedStructure);
      logger.info(
          "Updated Department "
              + referencedStructure.getName()
              + "(id:"
              + referencedStructure.getId()
              + ")");
      activityService.createActivity(user, referencedStructure, Action.EDITED);
    }

    return referencedStructure;
  }

  @Override
  @Transactional
  public Objective createObjective(Long structureId, Objective objective, User user) {
    T department = structureRepository.findByIdOrThrow(structureId);

    throwIfCycleForDepartmentIsClosed(department);

    for (Objective otherObjective : department.getObjectives()) {
      otherObjective.setSequence(otherObjective.getSequence() + 1);
      objectiveRepository.save(otherObjective);
    }

    objective.setParentStructure(department);

    Objective parentObjective = null;
    if (objective.hasParentObjective()) {
      parentObjective = objectiveRepository.findByIdOrThrow(objective.getParentObjective().getId());
      parentService.validateParentObjective(objective, parentObjective);
    }
    objective.setParentObjective(parentObjective);

    objective = objectiveRepository.save(objective);
    logger.info(
        "Created Objective: "
            + objective.getName()
            + " into department "
            + department.getName()
            + "(id:"
            + structureId
            + ")");
    activityService.createActivity(user, objective, Action.CREATED);

    return objective;
  }
}
