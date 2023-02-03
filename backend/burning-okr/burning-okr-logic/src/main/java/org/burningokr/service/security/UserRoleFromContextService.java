package org.burningokr.service.security;

import org.burningokr.model.okr.Note;
import org.burningokr.model.okr.okrTopicDraft.OkrTopicDraft;
import org.burningokr.model.okrUnits.OkrChildUnit;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.burningokr.model.okrUnits.OkrUnit;
import org.burningokr.model.users.AdminUser;
import org.burningokr.model.users.AuditorUser;
import org.burningokr.repositories.okr.KeyResultRepository;
import org.burningokr.repositories.okr.NoteRepository;
import org.burningokr.repositories.okr.ObjectiveRepository;
import org.burningokr.repositories.okr.OkrTopicDraftRepository;
import org.burningokr.repositories.okrUnit.OkrDepartmentRepository;
import org.burningokr.repositories.okrUnit.UnitRepository;
import org.burningokr.repositories.users.AdminUserRepository;
import org.burningokr.repositories.users.AuditorUserRepository;
import org.burningokr.service.userhandling.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserRoleFromContextService {

  private UnitRepository<OkrChildUnit> unitRepository;
  private ObjectiveRepository objectiveRepository;
  private KeyResultRepository keyResultRepository;
  private NoteRepository noteRepository;
  private OkrTopicDraftRepository topicDraftRepository;
  private UserService userService;
  private AdminUserRepository adminUserRepository;
  private AuditorUserRepository auditorUserRepository;

  /**
   * Initialize UserRoleFromContextService.
   *
   * @param unitRepository a {@link OkrDepartmentRepository} object
   * @param objectiveRepository an {@link ObjectiveRepository} object
   * @param keyResultRepository a {@link KeyResultRepository} object
   * @param noteRepository a {@link NoteRepository} object
   * @param adminUserRepository an {@link AdminUserRepository} object
   * @param auditorUserRepository an {@link AuditorUserRepository} object
   * @param userService an {@link UserService} object
   */
  @Autowired
  public UserRoleFromContextService(
      UnitRepository<OkrChildUnit> unitRepository,
      ObjectiveRepository objectiveRepository,
      KeyResultRepository keyResultRepository,
      NoteRepository noteRepository,
      OkrTopicDraftRepository topicDraftRepository,
      AdminUserRepository adminUserRepository,
      AuditorUserRepository auditorUserRepository,
      UserService userService) {
    this.unitRepository = unitRepository;
    this.objectiveRepository = objectiveRepository;
    this.keyResultRepository = keyResultRepository;
    this.noteRepository = noteRepository;
    this.topicDraftRepository = topicDraftRepository;
    this.adminUserRepository = adminUserRepository;
    this.auditorUserRepository = auditorUserRepository;
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

  public UserContextRole getUserRoleInUnitId(Long unitId) {
    OkrChildUnit contextDepartment = getUnitOfId(unitId);
    return getUserRoleInUnit(contextDepartment);
  }

  public UserContextRole getUserRoleInObjectiveId(Long objectiveId) {
    OkrUnit contextDepartment = getUnitOfObjective(objectiveId);
    return getUserRoleInUnit(contextDepartment);
  }

  public UserContextRole getUserRoleInKeyResultId(Long keyResultId) {
    OkrUnit contextDepartment = getUnitOfKeyResult(keyResultId);
    return getUserRoleInUnit(contextDepartment);
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
   * @param topicDraftId a long value
   * @return an {@link UserContextRole} object
   */
  public UserContextRole getUserRoleTopicDraft(Long topicDraftId) {
    OkrTopicDraft topicDraft = topicDraftRepository.findByIdOrThrow(topicDraftId);
    UUID currentUserId = userService.getCurrentUser().getId();

    if (currentUserId.equals(topicDraft.getInitiatorId())) {
      return UserContextRole.ENTITYOWNER;
    } else {
      return UserContextRole.USER;
    }
  }

  /**
   * Gets the UserContextRole of a okrUnit.
   *
   * @param okrUnit an {@link OkrChildUnit} object
   * @return an {@link UserContextRole} object
   */
  public UserContextRole getUserRoleInUnit(OkrUnit okrUnit) {
    if (okrUnit instanceof OkrDepartment) {
      return getUserRoleInDepartment((OkrDepartment) okrUnit);
    } else {
      return getUserRoleWithoutContext();
    }
  }

  private UserContextRole getUserRoleInDepartment(OkrDepartment contextOkrDepartment) {
    UUID currentUserId = userService.getCurrentUser().getId();
    if (isCurrentUserAdmin(currentUserId)) {
      return UserContextRole.ADMIN;
    } else if (isOkrManagerOfDepartment(contextOkrDepartment, currentUserId)) {
      return UserContextRole.OKRMANAGER;
    } else if (isOkrMemberOfDepartment(contextOkrDepartment, currentUserId)) {
      return UserContextRole.OKRMEMBER;
    }
    return UserContextRole.USER;
  }

  private boolean isCurrentUserAdmin(UUID currentUserId) {
    Optional<AdminUser> optional = adminUserRepository.findById(currentUserId);

    return optional.isPresent();
  }

  public boolean isCurrentUserAuditor() {
    Optional<AuditorUser> optional =
        auditorUserRepository.findById(userService.getCurrentUser().getId());

    return optional.isPresent();
  }

  private boolean isOkrManagerOfDepartment(OkrDepartment okrDepartment, UUID targetUserId) {
    UUID okrMaster = okrDepartment.getOkrMasterId();
    UUID okrTopicSponsor = okrDepartment.getOkrTopicSponsorId();
    return (isIdDefinedAndEquals(okrMaster, targetUserId)
        || isIdDefinedAndEquals(okrTopicSponsor, targetUserId));
  }

  private boolean isIdDefinedAndEquals(UUID uuidToTest, UUID uuidToMatch) {
    if (uuidToTest != null) {
      return uuidToTest.equals(uuidToMatch);
    }
    return false;
  }

  private boolean isOkrMemberOfDepartment(OkrDepartment okrDepartment, UUID targetUserId) {
    for (UUID currentUserId : okrDepartment.getOkrMemberIds()) {
      if (currentUserId.equals(targetUserId)) {
        return true;
      }
    }
    return false;
  }

  private OkrChildUnit getUnitOfId(long unitId) {
    return unitRepository.findByIdOrThrow(unitId);
  }

  private OkrUnit getUnitOfObjective(long objectiveId) {
    return objectiveRepository.findByIdOrThrow(objectiveId).getParentOkrUnit();
  }

  private OkrUnit getUnitOfKeyResult(long keyResultId) {
    return keyResultRepository.findByIdOrThrow(keyResultId).getParentObjective().getParentOkrUnit();
  }
}
