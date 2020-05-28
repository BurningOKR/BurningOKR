package org.burningokr.service.structure.departmentservices;

import org.burningokr.model.activity.Action;
import org.burningokr.model.structures.Department;
import org.burningokr.model.structures.SubStructure;
import org.burningokr.model.users.User;
import org.burningokr.repositories.okr.ObjectiveRepository;
import org.burningokr.repositories.structre.DepartmentRepository;
import org.burningokr.repositories.structre.StructureRepository;
import org.burningokr.service.activity.ActivityService;
import org.burningokr.service.exceptions.InvalidDeleteRequestException;
import org.burningokr.service.structureutil.EntityCrawlerService;
import org.burningokr.service.structureutil.ParentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("structureServiceAdmins")
public class StructureServiceAdmins<T extends SubStructure> extends StructureServiceManagers<T> {

  /**
   * Initialize DepartmentServiceAdmins.
   *
   * @param parentService a {@link ParentService} object
   * @param structureRepository a {@link DepartmentRepository} object
   * @param objectiveRepository an {@link ObjectiveRepository} object
   * @param activityService an {@link ActivityService} object
   * @param entityCrawlerService an {@link EntityCrawlerService} object
   */
  public StructureServiceAdmins(
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

    referencedStructure.setName(updatedStructure.getName());
    referencedStructure.setLabel(updatedStructure.getLabel());
    referencedStructure.setActive(updatedStructure.isActive());

    if (updatedStructure instanceof Department) {
      Department referencedDepartment = (Department) referencedStructure;
      Department updatedDepartment = (Department) updatedStructure;

      referencedDepartment.setOkrMasterId(updatedDepartment.getOkrMasterId());
      referencedDepartment.setOkrTopicSponsorId(updatedDepartment.getOkrTopicSponsorId());
      referencedDepartment.setOkrMemberIds(updatedDepartment.getOkrMemberIds());
    }

    referencedStructure = structureRepository.save(referencedStructure);
    logger.info(
        "Updated Department "
            + referencedStructure.getName()
            + "(id:"
            + referencedStructure.getId()
            + ")");
    activityService.createActivity(user, referencedStructure, Action.EDITED);

    return referencedStructure;
  }

  @Override
  public void deleteStructure(Long structureId, User user) {
    T referencedStructure = structureRepository.findByIdOrThrow(structureId);

    throwIfCycleForDepartmentIsClosed(referencedStructure);

    if (referencedStructure.getDepartments().isEmpty()) {
      activityService.createActivity(user, referencedStructure, Action.DELETED);
      structureRepository.deleteById(structureId);
      logger.info("Deleted Department with id: " + structureId);
      activityService.createActivity(user, referencedStructure, Action.DELETED);

    } else {
      logger.info(
          "Could not delete department with id: "
              + structureId
              + ". The department contains sub-departments.");
      throw new InvalidDeleteRequestException(
          "You can not delete departments which contains sub-departments.");
    }
  }

  @Override
  @Transactional
  public T createSubstructure(Long parentStructureId, T subDepartment, User user) {
    T parentStructure = structureRepository.findByIdOrThrow(parentStructureId);

    throwIfCycleForDepartmentIsClosed(parentStructure);

    subDepartment.setParentStructure(parentStructure);

    subDepartment = structureRepository.save(subDepartment);
    logger.info(
        "Created subdepartment: "
            + subDepartment.getName()
            + " into Department "
            + parentStructure.getName()
            + "(id:"
            + parentStructureId
            + ")");
    activityService.createActivity(user, subDepartment, Action.CREATED);
    return subDepartment;
  }
}
