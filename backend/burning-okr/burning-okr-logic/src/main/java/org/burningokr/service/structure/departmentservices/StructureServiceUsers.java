package org.burningokr.service.structure.departmentservices;

import java.util.Collection;
import org.burningokr.model.cycles.CycleState;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.structures.Structure;
import org.burningokr.model.structures.SubStructure;
import org.burningokr.model.users.User;
import org.burningokr.repositories.okr.ObjectiveRepository;
import org.burningokr.repositories.structre.StructureRepository;
import org.burningokr.service.activity.ActivityService;
import org.burningokr.service.exceptions.ForbiddenException;
import org.burningokr.service.structure.StructureService;
import org.burningokr.service.structureutil.EntityCrawlerService;
import org.burningokr.service.structureutil.ParentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
import org.springframework.stereotype.Service;

@Service("structureServiceUsers")
public class StructureServiceUsers<T extends SubStructure> implements StructureService<T> {

  protected final Logger logger = LoggerFactory.getLogger(StructureServiceUsers.class);
  protected StructureRepository<T> structureRepository;
  protected ObjectiveRepository objectiveRepository;
  protected ActivityService activityService;
  ParentService parentService;
  private EntityCrawlerService entityCrawlerService;

  @Autowired
  StructureServiceUsers(
      ParentService parentService,
      StructureRepository<T> structureRepository,
      ObjectiveRepository objectiveRepository,
      ActivityService activityService,
      EntityCrawlerService entityCrawlerService) {
    this.parentService = parentService;
    this.structureRepository = structureRepository;
    this.objectiveRepository = objectiveRepository;
    this.activityService = activityService;
    this.entityCrawlerService = entityCrawlerService;
  }

  @Override
  public T findById(long structureId) {
    return structureRepository.findByIdOrThrow(structureId);
  }

  @Override
  public Collection<Objective> findObjectivesOfStructure(long departmentId) {
    T department = findById(departmentId);
    return objectiveRepository.findByStructureAndOrderBySequence(department);
  }

  @Override
  public T updateStructure(T updatedDepartment, User user) {
    throw new UnauthorizedUserException("Service method not supported for current user role.");
  }

  @Override
  public void deleteStructure(Long structureId, User user) {
    throw new UnauthorizedUserException("Service method not supported for current user role.");
  }

  @Override
  public T createSubstructure(Long parentStructureId, T subDepartment, User user) {
    throw new UnauthorizedUserException("Service method not supported for current user role.");
  }

  @Override
  public Objective createObjective(Long structureId, Objective objective, User user) {
    throw new UnauthorizedUserException("Service method not supported for current user role.");
  }

  void throwIfCycleForDepartmentIsClosed(Structure structureToCheck) {
    if (entityCrawlerService.getCycleOfStructure(structureToCheck).getCycleState()
        == CycleState.CLOSED) {
      throw new ForbiddenException(
          "Cannot modify this resource on a Department in a closed cycle.");
    }
  }
}
