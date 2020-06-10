package org.burningokr.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {

  private UserRoleFromContextService userRoleFromContextService;

  @Autowired
  public AuthorizationService(UserRoleFromContextService userRoleFromContextService) {
    this.userRoleFromContextService = userRoleFromContextService;
  }

  public boolean isAdmin() {
    UserContextRole userRole = userRoleFromContextService.getUserRoleWithoutContext();
    return userRole == UserContextRole.ADMIN;
  }

  public boolean hasManagerPrivilegeForDepartment(Long departmentId) {
    UserContextRole userRole = userRoleFromContextService.getUserRoleInUnitId(departmentId);
    return userRole.isHigherAuthorityTypeThan(UserContextRole.OKRMEMBER);
  }

  public boolean hasManagerPrivilegeForObjective(Long objectiveId) {
    UserContextRole userRole = userRoleFromContextService.getUserRoleInObjectiveId(objectiveId);
    return userRole.isHigherAuthorityTypeThan(UserContextRole.OKRMEMBER);
  }

  public boolean hasManagerPrivilegeForKeyResult(Long keyResultId) {
    UserContextRole userRole = userRoleFromContextService.getUserRoleInKeyResultId(keyResultId);
    return userRole.isHigherAuthorityTypeThan(UserContextRole.OKRMEMBER);
  }

  public boolean hasMemberPrivilegeForDepartment(Long departmentId) {
    UserContextRole userRole = userRoleFromContextService.getUserRoleInUnitId(departmentId);
    return userRole.isHigherAuthorityTypeThan(UserContextRole.USER);
  }

  public boolean hasMemberPrivilegeForObjective(Long objectiveId) {
    UserContextRole userRole = userRoleFromContextService.getUserRoleInObjectiveId(objectiveId);
    return userRole.isHigherAuthorityTypeThan(UserContextRole.USER);
  }

  public boolean hasMemberPrivilegeForKeyResult(Long keyResultId) {
    UserContextRole userRole = userRoleFromContextService.getUserRoleInKeyResultId(keyResultId);
    return userRole.isHigherAuthorityTypeThan(UserContextRole.USER);
  }

  public boolean isNoteOwner(Long noteId) {
    UserContextRole userRole = userRoleFromContextService.getUserRoleInNoteId(noteId);
    return userRole == UserContextRole.ENTITYOWNER;
  }
}
