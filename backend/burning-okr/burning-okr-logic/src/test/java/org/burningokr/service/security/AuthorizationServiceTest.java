package org.burningokr.service.security;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AuthorizationServiceTest {

  @Mock
  UserRoleFromContextService userRoleFromContextService;

  @InjectMocks
  private AuthorizationService authorizationService;

  private Long departmentId = 100L;
  private Long objectiveId = 200L;
  private Long keyResultId = 300L;
  private Long noteId = 400L;

  private void mockCurrentUserRole(UserContextRole roleToMock) {
    when(userRoleFromContextService.getUserRoleWithoutContext()).thenReturn(roleToMock);
    when(userRoleFromContextService.getUserRoleInUnitId(departmentId)).thenReturn(roleToMock);
    when(userRoleFromContextService.getUserRoleInObjectiveId(objectiveId)).thenReturn(roleToMock);
    when(userRoleFromContextService.getUserRoleInKeyResultId(keyResultId)).thenReturn(roleToMock);
    when(userRoleFromContextService.getUserRoleInNoteId(noteId)).thenReturn(roleToMock);
  }

  @Test
  public void isAdmin_isAdmin_expectedTrue() {
    mockCurrentUserRole(UserContextRole.ADMIN);

    boolean actual = authorizationService.isAdmin();

    Assert.assertTrue(actual);
  }

  @Test
  public void isAdmin_isUser_expectedFalse() {
    mockCurrentUserRole(UserContextRole.USER);

    boolean actual = authorizationService.isAdmin();

    Assert.assertFalse(actual);
  }

  @Test
  public void hasManagerPrivilegeForDepartment_isManager_expectedTrue() {
    mockCurrentUserRole(UserContextRole.OKRMANAGER);

    boolean actual = authorizationService.hasManagerPrivilegeForDepartment(departmentId);

    Assert.assertTrue(actual);
  }

  @Test
  public void hasManagerPrivilegeForDepartment_isMember_expectedFalse() {
    mockCurrentUserRole(UserContextRole.OKRMEMBER);

    boolean actual = authorizationService.hasManagerPrivilegeForDepartment(departmentId);

    Assert.assertFalse(actual);
  }

  @Test
  public void hasManagerPrivilegeForObjective_isManager_expectedTrue() {
    mockCurrentUserRole(UserContextRole.OKRMANAGER);

    boolean actual = authorizationService.hasManagerPrivilegeForObjective(objectiveId);

    Assert.assertTrue(actual);
  }

  @Test
  public void hasManagerPrivilegeForObjective_isMember_expectedFalse() {
    mockCurrentUserRole(UserContextRole.OKRMEMBER);

    boolean actual = authorizationService.hasManagerPrivilegeForObjective(objectiveId);

    Assert.assertFalse(actual);
  }

  @Test
  public void hasManagerPrivilegeForKeyResult_isManager_expectedTrue() {
    mockCurrentUserRole(UserContextRole.OKRMANAGER);

    boolean actual = authorizationService.hasManagerPrivilegeForKeyResult(keyResultId);

    Assert.assertTrue(actual);
  }

  @Test
  public void hasManagerPrivilegeForKeyResult_isMember_expectedFalse() {
    mockCurrentUserRole(UserContextRole.OKRMEMBER);

    boolean actual = authorizationService.hasManagerPrivilegeForKeyResult(keyResultId);

    Assert.assertFalse(actual);
  }

  @Test
  public void hasMemberPrivilegeForDepartment_isMember_expectedTrue() {
    mockCurrentUserRole(UserContextRole.OKRMEMBER);

    boolean actual = authorizationService.hasMemberPrivilegeForDepartment(departmentId);

    Assert.assertTrue(actual);
  }

  @Test
  public void hasMemberPrivilegeForDepartment_isUser_expectedFalse() {
    mockCurrentUserRole(UserContextRole.USER);

    boolean actual = authorizationService.hasMemberPrivilegeForDepartment(departmentId);

    Assert.assertFalse(actual);
  }

  @Test
  public void hasMemberPrivilegeForObjective_isMember_expectedTrue() {
    mockCurrentUserRole(UserContextRole.OKRMEMBER);

    boolean actual = authorizationService.hasMemberPrivilegeForObjective(objectiveId);

    Assert.assertTrue(actual);
  }

  @Test
  public void hasMemberPrivilegeForObjective_isUser_expectedFalse() {
    mockCurrentUserRole(UserContextRole.USER);

    boolean actual = authorizationService.hasMemberPrivilegeForObjective(objectiveId);

    Assert.assertFalse(actual);
  }

  @Test
  public void hasMemberPrivilegeForKeyResult_isMember_expectedTrue() {
    mockCurrentUserRole(UserContextRole.OKRMEMBER);

    boolean actual = authorizationService.hasMemberPrivilegeForKeyResult(keyResultId);

    Assert.assertTrue(actual);
  }

  @Test
  public void hasMemberPrivilegeForKeyResult_isUser_expectedFalse() {
    mockCurrentUserRole(UserContextRole.USER);

    boolean actual = authorizationService.hasMemberPrivilegeForKeyResult(keyResultId);

    Assert.assertFalse(actual);
  }

  @Test
  public void isNoteOwner_isOwner_expectedTrue() {
    mockCurrentUserRole(UserContextRole.ENTITYOWNER);

    boolean actual = authorizationService.isNoteOwner(noteId);

    Assert.assertTrue(actual);
  }

  @Test
  public void isNoteOwner_isUser_expectedFalse() {
    mockCurrentUserRole(UserContextRole.USER);

    boolean actual = authorizationService.isNoteOwner(noteId);

    Assert.assertFalse(actual);
  }
}
