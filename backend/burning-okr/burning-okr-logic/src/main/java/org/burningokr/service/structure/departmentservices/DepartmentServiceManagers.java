package org.burningokr.service.structure.departmentservices;

import org.burningokr.model.activity.Action;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.structures.Department;
import org.burningokr.model.users.User;
import org.burningokr.repositories.okr.ObjectiveRepository;
import org.burningokr.repositories.structre.DepartmentRepository;
import org.burningokr.service.activity.ActivityService;
import org.burningokr.service.exceptions.DuplicateTeamMemberException;
import org.burningokr.service.structureutil.EntityCrawlerService;
import org.burningokr.service.structureutil.ParentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DepartmentServiceManagers extends DepartmentServiceUsers {

  @Autowired
  DepartmentServiceManagers(
      ParentService parentService,
      DepartmentRepository departmentRepository,
      ObjectiveRepository objectiveRepository,
      ActivityService activityService,
      EntityCrawlerService entityCrawlerService) {
    super(
        parentService,
        departmentRepository,
        objectiveRepository,
        activityService,
        entityCrawlerService);
  }

  @Override
  @Transactional
  public Department updateDepartment(Department updatedDepartment, User user) throws DuplicateTeamMemberException {
    Department referencedDepartment =
        departmentRepository.findByIdOrThrow(updatedDepartment.getId());

    throwIfCycleForDepartmentIsClosed(referencedDepartment);
    throwIfDepartmentHasDuplicateTeamMembers(updatedDepartment);

    referencedDepartment.setOkrMemberIds(updatedDepartment.getOkrMemberIds());

    referencedDepartment = departmentRepository.save(referencedDepartment);
    logger.info(
        "Updated Department "
            + referencedDepartment.getName()
            + "(id:"
            + referencedDepartment.getId()
            + ")");
    activityService.createActivity(user, referencedDepartment, Action.EDITED);

    return referencedDepartment;
  }

  @Override
  @Transactional
  public Objective createObjective(Long departmentId, Objective objective, User user) {
    Department department = departmentRepository.findByIdOrThrow(departmentId);

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
            + departmentId
            + ")");
    activityService.createActivity(user, objective, Action.CREATED);

    return objective;
  }
}
