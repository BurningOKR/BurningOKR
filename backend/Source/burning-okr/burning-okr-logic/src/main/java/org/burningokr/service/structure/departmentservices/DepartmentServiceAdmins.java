package org.burningokr.service.structure.departmentservices;

import org.burningokr.model.activity.Action;
import org.burningokr.model.structures.Department;
import org.burningokr.model.users.User;
import org.burningokr.repositories.okr.ObjectiveRepository;
import org.burningokr.repositories.structre.DepartmentRepository;
import org.burningokr.service.activity.ActivityService;
import org.burningokr.service.exceptions.InvalidDeleteRequestException;
import org.burningokr.service.structureutil.EntityCrawlerService;
import org.burningokr.service.structureutil.ParentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DepartmentServiceAdmins extends DepartmentServiceManagers {

  /**
   * Initialize DepartmentServiceAdmins.
   *
   * @param parentService a {@link ParentService} object
   * @param departmentRepository a {@link DepartmentRepository} object
   * @param objectiveRepository an {@link ObjectiveRepository} object
   * @param activityService an {@link ActivityService} object
   * @param entityCrawlerService an {@link EntityCrawlerService} object
   */
  public DepartmentServiceAdmins(
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
  public Department updateDepartment(Department updatedDepartment, User user) {
    Department referencedDepartment =
        departmentRepository.findByIdOrThrow(updatedDepartment.getId());

    throwIfCycleForDepartmentIsClosed(referencedDepartment);

    referencedDepartment.setName(updatedDepartment.getName());
    referencedDepartment.setLabel(updatedDepartment.getLabel());
    referencedDepartment.setOkrMasterId(updatedDepartment.getOkrMasterId());
    referencedDepartment.setOkrTopicSponsorId(updatedDepartment.getOkrTopicSponsorId());
    referencedDepartment.setOkrMemberIds(updatedDepartment.getOkrMemberIds());
    referencedDepartment.setActive(updatedDepartment.isActive());

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
  public void deleteDepartment(Long departmentId, User user) {
    Department referencedDepartment = departmentRepository.findByIdOrThrow(departmentId);

    throwIfCycleForDepartmentIsClosed(referencedDepartment);

    if (referencedDepartment.getDepartments().isEmpty()) {
      activityService.createActivity(user, referencedDepartment, Action.DELETED);
      departmentRepository.deleteById(departmentId);
      logger.info("Deleted Department with id: " + departmentId);
      activityService.createActivity(user, referencedDepartment, Action.DELETED);

    } else {
      logger.info(
          "Could not delete department with id: "
              + departmentId
              + ". The department contains sub-departments.");
      throw new InvalidDeleteRequestException(
          "You can not delete departments which contains sub-departments.");
    }
  }

  @Override
  @Transactional
  public Department createSubdepartment(
      Long parentDepartmentId, Department subDepartment, User user) {
    Department parentDepartment = departmentRepository.findByIdOrThrow(parentDepartmentId);

    throwIfCycleForDepartmentIsClosed(parentDepartment);

    subDepartment.setParentStructure(parentDepartment);

    subDepartment = departmentRepository.save(subDepartment);
    logger.info(
        "Created subdepartment: "
            + subDepartment.getName()
            + " into Department "
            + parentDepartment.getName()
            + "(id:"
            + parentDepartmentId
            + ")");
    activityService.createActivity(user, subDepartment, Action.CREATED);
    return subDepartment;
  }
}
