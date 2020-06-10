package org.burningokr.service.security;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.Note;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.okrUnits.OkrBranch;
import org.burningokr.model.okrUnits.OkrChildUnit;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.burningokr.model.users.AdminUser;
import org.burningokr.model.users.User;
import org.burningokr.repositories.okr.KeyResultRepository;
import org.burningokr.repositories.okr.NoteRepository;
import org.burningokr.repositories.okr.ObjectiveRepository;
import org.burningokr.repositories.okrUnit.UnitRepository;
import org.burningokr.repositories.users.AdminUserRepository;
import org.burningokr.service.userhandling.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UserRoleFromContextServiceTest {

  @Mock private UnitRepository<OkrChildUnit> unitRepository;
  @Mock private ObjectiveRepository objectiveRepository;
  @Mock private KeyResultRepository keyResultRepository;
  @Mock private NoteRepository noteRepository;
  @Mock private AdminUserRepository adminUserRepository;
  @Mock private UserService userService;
  @InjectMocks private UserRoleFromContextService userRoleFromContextService;

  private User currentUser;
  private AdminUser returnedAdminUser;

  private UUID adminId;
  private AdminUser adminUser;
  private UUID unrelatedUserId = UUID.fromString("5a64439b-adea-4c3c-99d9-fdc791264ee0");
  private UUID okrMasterId = UUID.fromString("6a64439b-adea-4c3c-99d9-fdc791264ee0");
  private UUID okrThemenpateId = UUID.fromString("7a64439b-adea-4c3c-99d9-fdc791264ee0");
  private UUID okrMemberId = UUID.fromString("8a64439b-adea-4c3c-99d9-fdc791264ee0");
  private UUID noteOwnerId = UUID.fromString("9a64439b-adea-4c3c-99d9-fdc791264ee0");

  private Long departmentId = 100L;
  private OkrDepartment dbOkrDepartment;
  private Long objectiveId = 200L;
  private Objective dbObjective;
  private Long keyResultId = 300L;
  private KeyResult dbKeyResult;
  private Long noteId = 400L;
  private Note dbNote;

  @Before
  public void init() {
    setupFakeEntities();
    setupRepositoryMocks();
    setupUserQueryMocks();
  }

  private void setupFakeEntities() {
    currentUser = mock(User.class);

    adminId = UUID.randomUUID();

    unrelatedUserId = UUID.randomUUID();
    okrMasterId = UUID.randomUUID();
    okrMemberId = UUID.randomUUID();
    okrThemenpateId = UUID.randomUUID();
    okrMemberId = UUID.randomUUID();
    noteOwnerId = UUID.randomUUID();

    adminUser = new AdminUser();
    adminUser.setId(adminId);

    dbOkrDepartment = new OkrDepartment();
    dbOkrDepartment.setId(departmentId);
    dbOkrDepartment.setOkrMasterId(okrMasterId);
    dbOkrDepartment.setOkrTopicSponsorId(okrThemenpateId);
    ArrayList<UUID> members = new ArrayList<>();
    members.add(okrMemberId);
    dbOkrDepartment.setOkrMemberIds(members);

    dbObjective = new Objective();
    dbObjective.setId(objectiveId);
    dbObjective.setParentOkrUnit(dbOkrDepartment);

    dbKeyResult = new KeyResult();
    dbKeyResult.setId(keyResultId);
    dbKeyResult.setParentObjective(dbObjective);

    dbNote = new Note();
    dbNote.setId(noteId);
    dbNote.setParentKeyResult(dbKeyResult);
    dbNote.setUserId(noteOwnerId);
  }

  private void setupRepositoryMocks() {
    when(unitRepository.findByIdOrThrow(departmentId)).thenReturn(dbOkrDepartment);
    when(objectiveRepository.findByIdOrThrow(objectiveId)).thenReturn(dbObjective);
    when(keyResultRepository.findByIdOrThrow(keyResultId)).thenReturn(dbKeyResult);
    when(noteRepository.findByIdOrThrow(noteId)).thenReturn(dbNote);
  }

  private void setupUserQueryMocks() {
    when(userService.getCurrentUser()).thenReturn(currentUser);
    Optional<AdminUser> returnedAdminOptionalEmpty = Optional.empty();
    Optional<AdminUser> returnedAdminOptionalFilled = Optional.of(adminUser);
    when(adminUserRepository.findById(any())).thenReturn(returnedAdminOptionalEmpty);
    when(adminUserRepository.findById(adminId)).thenReturn(returnedAdminOptionalFilled);
  }

  @Test
  public void getRoleWithoutContext_AdminUser_expectedAdminRole() {
    when(currentUser.getId()).thenReturn(adminId);

    UserContextRole actualRole = userRoleFromContextService.getUserRoleWithoutContext();

    Assert.assertEquals(UserContextRole.ADMIN, actualRole);
  }

  @Test
  public void getRoleWithoutContext_User_expectedUserRole() {
    when(currentUser.getId()).thenReturn(unrelatedUserId);

    UserContextRole actualRole = userRoleFromContextService.getUserRoleWithoutContext();

    Assert.assertEquals(UserContextRole.USER, actualRole);
  }

  @Test
  public void getRoleFromDepartment_OkrMaster_expectedManagerRole() {
    when(currentUser.getId()).thenReturn(okrMasterId);

    UserContextRole actualRole = userRoleFromContextService.getUserRoleInUnitId(departmentId);

    Assert.assertEquals(UserContextRole.OKRMANAGER, actualRole);
  }

  @Test
  public void getRoleFromDepartment_OkrTopicSponsor_expectedManagerRole() {
    when(currentUser.getId()).thenReturn(okrThemenpateId);

    UserContextRole actualRole = userRoleFromContextService.getUserRoleInUnitId(departmentId);

    Assert.assertEquals(UserContextRole.OKRMANAGER, actualRole);
  }

  @Test
  public void getRoleFromDepartment_OkrMember_expectedMemberRole() {
    when(currentUser.getId()).thenReturn(okrMemberId);

    UserContextRole actualRole = userRoleFromContextService.getUserRoleInUnitId(departmentId);

    Assert.assertEquals(UserContextRole.OKRMEMBER, actualRole);
  }

  @Test
  public void getRoleFromDepartment_User_expectedUserRole() {
    when(currentUser.getId()).thenReturn(unrelatedUserId);

    UserContextRole actualRole = userRoleFromContextService.getUserRoleInUnitId(departmentId);

    Assert.assertEquals(UserContextRole.USER, actualRole);
  }

  @Test
  public void getRoleFromObjective_OkrMember_expectedMemberRole() {
    when(currentUser.getId()).thenReturn(okrMemberId);

    UserContextRole actualRole = userRoleFromContextService.getUserRoleInObjectiveId(objectiveId);

    Assert.assertEquals(UserContextRole.OKRMEMBER, actualRole);
  }

  @Test
  public void getRoleFromKeyResult_OkrMaster_expectedManagerRole() {
    when(currentUser.getId()).thenReturn(okrMasterId);

    UserContextRole actualRole = userRoleFromContextService.getUserRoleInKeyResultId(keyResultId);

    Assert.assertEquals(UserContextRole.OKRMANAGER, actualRole);
  }

  @Test
  public void getRoleFromKeyResult_OkrTopicSponsor_expectedManagerRole() {
    when(currentUser.getId()).thenReturn(okrThemenpateId);

    UserContextRole actualRole = userRoleFromContextService.getUserRoleInKeyResultId(keyResultId);

    Assert.assertEquals(UserContextRole.OKRMANAGER, actualRole);
  }

  @Test
  public void getRoleFromKeyResult_OkrMember_expectedMemberRole() {
    when(currentUser.getId()).thenReturn(okrMemberId);

    UserContextRole actualRole = userRoleFromContextService.getUserRoleInKeyResultId(keyResultId);

    Assert.assertEquals(UserContextRole.OKRMEMBER, actualRole);
  }

  @Test
  public void getRoleFromKeyResult_User_expectedUserRole() {
    when(currentUser.getId()).thenReturn(unrelatedUserId);

    UserContextRole actualRole = userRoleFromContextService.getUserRoleInKeyResultId(keyResultId);

    Assert.assertEquals(UserContextRole.USER, actualRole);
  }

  @Test
  public void getRoleFromNote_Owner_expectedOwnerRole() {
    when(currentUser.getId()).thenReturn(noteOwnerId);

    UserContextRole actualRole = userRoleFromContextService.getUserRoleInNoteId(noteId);

    Assert.assertEquals(UserContextRole.ENTITYOWNER, actualRole);
  }

  @Test
  public void getRoleFromNote_User_expectedUserRole() {
    when(currentUser.getId()).thenReturn(unrelatedUserId);

    UserContextRole actualRole = userRoleFromContextService.getUserRoleInNoteId(noteId);

    Assert.assertEquals(UserContextRole.USER, actualRole);
  }

  @Test
  public void getRoleFromUnit_Department_User_expectedUserRole() {
    when(currentUser.getId()).thenReturn(unrelatedUserId);

    OkrChildUnit okrChildUnit = new OkrDepartment();

    UserContextRole actualRole = userRoleFromContextService.getUserRoleInUnit(okrChildUnit);

    Assert.assertEquals(UserContextRole.USER, actualRole);
  }

  @Test
  public void getRoleFromUnit_Department_Master_expectedManagerRole() {
    when(currentUser.getId()).thenReturn(okrMasterId);

    OkrDepartment okrDepartment = new OkrDepartment();
    okrDepartment.setOkrMasterId(okrMasterId);

    UserContextRole actualRole = userRoleFromContextService.getUserRoleInUnit(okrDepartment);

    Assert.assertEquals(UserContextRole.OKRMANAGER, actualRole);
  }

  @Test
  public void getRoleFromUnit_Department_TopicSponsor_expectedManagerRole() {
    when(currentUser.getId()).thenReturn(okrThemenpateId);

    OkrDepartment okrDepartment = new OkrDepartment();
    okrDepartment.setOkrTopicSponsorId(okrThemenpateId);

    UserContextRole actualRole = userRoleFromContextService.getUserRoleInUnit(okrDepartment);

    Assert.assertEquals(UserContextRole.OKRMANAGER, actualRole);
  }

  @Test
  public void getRoleFromUnit_Department_Member_expectedMemberRole() {
    when(currentUser.getId()).thenReturn(okrMemberId);

    OkrDepartment okrDepartment = new OkrDepartment();
    okrDepartment.setOkrMemberIds(Collections.singleton(okrMemberId));

    UserContextRole actualRole = userRoleFromContextService.getUserRoleInUnit(okrDepartment);

    Assert.assertEquals(UserContextRole.OKRMEMBER, actualRole);
  }

  @Test
  public void getRoleFromUnit_OkrBranch_User_expectedUserRole() {
    when(currentUser.getId()).thenReturn(unrelatedUserId);

    OkrChildUnit okrChildUnit = new OkrBranch();

    UserContextRole actualRole = userRoleFromContextService.getUserRoleInUnit(okrChildUnit);

    Assert.assertEquals(UserContextRole.USER, actualRole);
  }

  @Test
  public void getRoleFromUnit_OkrBranch_Admin_expectedAdminRole() {
    when(currentUser.getId()).thenReturn(adminId);

    OkrChildUnit okrChildUnit = new OkrBranch();

    UserContextRole actualRole = userRoleFromContextService.getUserRoleInUnit(okrChildUnit);

    Assert.assertEquals(UserContextRole.ADMIN, actualRole);
  }
}
