package org.burningokr.service.structure.departmentservices;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;
import lombok.SneakyThrows;
import org.burningokr.model.cycles.CycleState;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.structures.Department;
import org.burningokr.model.users.User;
import org.burningokr.repositories.okr.ObjectiveRepository;
import org.burningokr.repositories.structre.DepartmentRepository;
import org.burningokr.service.activity.ActivityService;
import org.burningokr.service.exceptions.DuplicateTeamMemberException;
import org.burningokr.service.exceptions.ForbiddenException;
import org.burningokr.service.structureutil.EntityCrawlerService;
import org.burningokr.service.structureutil.ParentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
import org.springframework.stereotype.Service;

@Service("departmentServiceUsers")
public class DepartmentServiceUsers implements DepartmentService {

  protected final Logger logger = LoggerFactory.getLogger(DepartmentServiceUsers.class);
  protected DepartmentRepository departmentRepository;
  protected ObjectiveRepository objectiveRepository;
  protected ActivityService activityService;
  ParentService parentService;
  private EntityCrawlerService entityCrawlerService;

  @Autowired
  DepartmentServiceUsers(
      ParentService parentService,
      DepartmentRepository departmentRepository,
      ObjectiveRepository objectiveRepository,
      ActivityService activityService,
      EntityCrawlerService entityCrawlerService) {
    this.parentService = parentService;
    this.departmentRepository = departmentRepository;
    this.objectiveRepository = objectiveRepository;
    this.activityService = activityService;
    this.entityCrawlerService = entityCrawlerService;
  }

  @Override
  public Department findById(long departmentId) {
    return departmentRepository.findByIdOrThrow(departmentId);
  }

  @Override
  public Collection<Department> findSubdepartmentsOfDepartment(long departmentId) {
    Department department = findById(departmentId);
    return department.getDepartments();
  }

  @Override
  public Collection<Objective> findObjectivesOfDepartment(long departmentId) {
    Department department = findById(departmentId);
    return objectiveRepository.findByDepartmentAndOrderBySequence(department);
  }

  @Override
  public Department updateDepartment(Department updatedDepartment, User user) {
    throw new UnauthorizedUserException("Service method not supported for current user role.");
  }

  @Override
  public void deleteDepartment(Long departmentId, User user) {
    throw new UnauthorizedUserException("Service method not supported for current user role.");
  }

  @Override
  public Department createSubdepartment(
      Long parentDepartmentId, Department subDepartment, User user) {
    throw new UnauthorizedUserException("Service method not supported for current user role.");
  }

  @Override
  public Objective createObjective(Long departmentId, Objective objective, User user) {
    throw new UnauthorizedUserException("Service method not supported for current user role.");
  }

  void throwIfCycleForDepartmentIsClosed(Department departmentToCheck) {
    if (entityCrawlerService.getCycleOfDepartment(departmentToCheck).getCycleState()
        == CycleState.CLOSED) {
      throw new ForbiddenException(
          "Cannot modify this resource on a Department in a closed cycle.");
    }
  }

  @SneakyThrows
  void throwIfDepartmentHasDuplicateTeamMembers(Department departmentToCheck) {
    if (hasDuplicateTeamMembers(departmentToCheck)) {
      throw new DuplicateTeamMemberException("Duplicate Team Members");
    }
  }

  boolean hasDuplicateTeamMembers(Department department) {
    UUID okrMaster = department.getOkrMasterId();
    UUID okrTopicSponsor = department.getOkrTopicSponsorId();
    Collection<UUID> okrMembers = department.getOkrMemberIds();
    boolean duplicateTeamMember =
        okrMembers.stream()
            .anyMatch(
                member ->
                    Collections.frequency(okrMembers, member) > 1
                        || member.equals(okrMaster)
                        || member.equals(okrTopicSponsor));
    boolean sameOkrMasterAndSponsor = okrMaster != null && okrMaster.equals(okrTopicSponsor);
    return duplicateTeamMember || sameOkrMasterAndSponsor;
  }
}
