package org.burningokr.service.security;

import java.util.Optional;
import java.util.UUID;
import org.burningokr.model.okr.Note;
import org.burningokr.model.structures.Department;
import org.burningokr.model.structures.SubStructure;
import org.burningokr.model.users.AdminUser;
import org.burningokr.repositories.okr.KeyResultRepository;
import org.burningokr.repositories.okr.NoteRepository;
import org.burningokr.repositories.okr.ObjectiveRepository;
import org.burningokr.repositories.structre.DepartmentRepository;
import org.burningokr.repositories.users.AdminUserRepository;
import org.burningokr.service.userhandling.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRoleFromContextService {

  private DepartmentRepository departmentRepository;
  private ObjectiveRepository objectiveRepository;
  private KeyResultRepository keyResultRepository;
  private NoteRepository noteRepository;
  private UserService userService;
  private AdminUserRepository adminUserRepository;

  /**
   * Initialize UserRoleFromContextService.
   *
   * @param departmentRepository a {@link DepartmentRepository} object
   * @param objectiveRepository an {@link ObjectiveRepository} object
   * @param keyResultRepository a {@link KeyResultRepository} object
   * @param noteRepository a {@link NoteRepository} object
   * @param adminUserRepository an {@link AdminUserRepository} object
   * @param userService an {@link UserService} object
   */
  @Autowired
  public UserRoleFromContextService(
      DepartmentRepository departmentRepository,
      ObjectiveRepository objectiveRepository,
      KeyResultRepository keyResultRepository,
      NoteRepository noteRepository,
      AdminUserRepository adminUserRepository,
      UserService userService) {
    this.departmentRepository = departmentRepository;
    this.objectiveRepository = objectiveRepository;
    this.keyResultRepository = keyResultRepository;
    this.noteRepository = noteRepository;
    this.adminUserRepository = adminUserRepository;
    this.userService = userService;
  }

  /**
   * Gets the User Role without Context.
   *
   * @return a {@link UserContextRole} object
   */
  public UserContextRole getUserRoleWithoutContext() {
    UUID currentUserId = userService.getCurrentUser().getId();
    if (isCurrentUserAdmin(currentUserId)) {
      return UserContextRole.ADMIN;
    }
    return UserContextRole.USER;
  }

  public UserContextRole getUserRoleInDepartmentId(Long departmentId) {
    Department contextDepartment = getDepartmentOfId(departmentId);
    return getUserRoleInDepartment(contextDepartment);
  }

  public UserContextRole getUserRoleInObjectiveId(Long objectiveId) {
    Department contextDepartment = getDepartmentOfObjective(objectiveId);
    return getUserRoleInDepartment(contextDepartment);
  }

  public UserContextRole getUserRoleInKeyResultId(Long keyResultId) {
    Department contextDepartment = getDepartmentOfKeyResult(keyResultId);
    return getUserRoleInDepartment(contextDepartment);
  }

  /**
   * Gets the UserContextRole of a Note.
   *
   * @param noteId a long value
   * @return an {@link UserContextRole} object
   */
  public UserContextRole getUserRoleInNoteId(Long noteId) {
    Note contextNote = noteRepository.findByIdOrThrow(noteId);
    UUID currentUserId = userService.getCurrentUser().getId();

    if (contextNote.getUserId().equals(currentUserId)) {
      return UserContextRole.ENTITYOWNER;
    }
    return UserContextRole.USER;
  }

  /**
   * Gets the UserContextRole of a Structure.
   *
   * @param structure an {@link SubStructure} object
   * @return an {@link UserContextRole} object
   */
  public UserContextRole getUserRoleInStructure(SubStructure structure) {
    if (structure instanceof Department) {
      return getUserRoleInDepartment((Department) structure);
    } else {
      return getUserRoleWithoutContext();
    }
  }

  private UserContextRole getUserRoleInDepartment(Department contextDepartment) {
    UUID currentUserId = userService.getCurrentUser().getId();
    if (isCurrentUserAdmin(currentUserId)) {
      return UserContextRole.ADMIN;
    } else if (isOkrManagerOfDepartment(contextDepartment, currentUserId)) {
      return UserContextRole.OKRMANAGER;
    } else if (isOkrMemberOfDepartment(contextDepartment, currentUserId)) {
      return UserContextRole.OKRMEMBER;
    }
    return UserContextRole.USER;
  }

  private boolean isCurrentUserAdmin(UUID currentUserId) {
    Optional<AdminUser> optional = adminUserRepository.findById(currentUserId);

    return optional.isPresent();
  }

  private boolean isOkrManagerOfDepartment(Department department, UUID targetUserId) {
    UUID okrMaster = department.getOkrMasterId();
    UUID okrTopicSponsor = department.getOkrTopicSponsorId();
    return (isIdDefinedAndEquals(okrMaster, targetUserId)
        || isIdDefinedAndEquals(okrTopicSponsor, targetUserId));
  }

  private boolean isIdDefinedAndEquals(UUID uuidToTest, UUID uuidToMatch) {
    if (uuidToTest != null) {
      return uuidToTest.equals(uuidToMatch);
    }
    return false;
  }

  private boolean isOkrMemberOfDepartment(Department department, UUID targetUserId) {
    for (UUID currentUserId : department.getOkrMemberIds()) {
      if (currentUserId.equals(targetUserId)) {
        return true;
      }
    }
    return false;
  }

  private Department getDepartmentOfId(long departmentId) {
    return departmentRepository.findByIdOrThrow(departmentId);
  }

  private Department getDepartmentOfObjective(long objectiveId) {
    return (Department) objectiveRepository.findByIdOrThrow(objectiveId).getParentStructure();
  }

  private Department getDepartmentOfKeyResult(long keyResultId) {
    return (Department)
        keyResultRepository.findByIdOrThrow(keyResultId).getParentObjective().getParentStructure();
  }
}
