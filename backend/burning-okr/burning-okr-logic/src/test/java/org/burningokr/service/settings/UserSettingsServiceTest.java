package org.burningokr.service.settings;

import jakarta.persistence.EntityNotFoundException;
import org.burningokr.model.cycles.Cycle;
import org.burningokr.model.cycles.CycleState;
import org.burningokr.model.okrUnits.OkrBranch;
import org.burningokr.model.okrUnits.OkrCompany;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.burningokr.model.settings.UserSettings;
import org.burningokr.model.users.User;
import org.burningokr.repositories.settings.UserSettingsRepository;
import org.burningokr.service.activity.ActivityService;
import org.burningokr.service.okrUnit.CompanyService;
import org.burningokr.service.okrUnit.OkrUnitService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserSettingsServiceTest {
  private static final UUID userId = UUID.randomUUID();
  private static User user;

  @Mock
  private UserSettingsRepository userSettingsRepository;

  @Mock
  private ActivityService activityService;

  @Mock
  private OkrUnitService<OkrDepartment> departmentService;

  @Mock
  private CompanyService companyService;

  @InjectMocks
  private UserSettingsService userSettingsService;

  @BeforeAll
  public static void initClass() {
    user = mock(User.class);
    when(user.getId()).thenReturn(userId);
  }

  @Test
  public void getUserSettingsByUser_expectNewSettingsBeingCreatedIfThereNoSuchUsersUserSettings() {
    whenUserSettingsSaveReturnSameValue();

    UserSettings userSettings = this.userSettingsService.getUserSettingsByUser(user);

    assertEquals(userId, userSettings.getUserId());
    verify(userSettingsRepository, times(1)).save(any());
  }

  @Test
  public void getUserSettingsByUser_expectNoCompanyIfThereIsMoreThanOneCompany() {
    when(userSettingsRepository.findUserSettingsByUserId(userId)).thenReturn(null);
    whenUserSettingsSaveReturnSameValue();
    OkrCompany okrCompany = new OkrCompany();
    okrCompany.setCycle(new Cycle());
    okrCompany.getCycle().setCycleState(CycleState.ACTIVE);
    OkrCompany okrCompany1 = new OkrCompany();
    okrCompany1.setCycle(new Cycle());
    okrCompany1.getCycle().setCycleState(CycleState.ACTIVE);
    when(companyService.getAllCompanies()).thenReturn(Arrays.asList(okrCompany, okrCompany1));

    UserSettings userSettings = this.userSettingsService.getUserSettingsByUser(user);

    assertNull(userSettings.getDefaultOkrCompany());
    verify(userSettingsRepository, times(1)).save(any());
    verify(companyService, times(1)).getAllCompanies();
  }

  @Test
  public void getUserSettingsByUser_expectCompanyIsSetIfThereIsOnlyOneCompany() {
    when(userSettingsRepository.findUserSettingsByUserId(userId)).thenReturn(null);
    OkrCompany okrCompany = new OkrCompany();
    okrCompany.setCycle(new Cycle());
    okrCompany.getCycle().setCycleState(CycleState.ACTIVE);
    when(companyService.getAllCompanies()).thenReturn(Collections.singletonList(okrCompany));
    whenUserSettingsSaveReturnSameValue();

    UserSettings userSettings = this.userSettingsService.getUserSettingsByUser(user);

    assertEquals(okrCompany, userSettings.getDefaultOkrCompany());
    verify(userSettingsRepository, times(1)).save(any());
    verify(companyService, times(1)).getAllCompanies();
  }

  @Test
  public void getUserSettingsByUser_expectDepartmentIsSetIfThereIsOnlyOneDepartment() {
    when(userSettingsRepository.findUserSettingsByUserId(userId)).thenReturn(null);
    whenUserSettingsSaveReturnSameValue();
    OkrCompany okrCompany = new OkrCompany();
    okrCompany.setCycle(new Cycle());
    okrCompany.getCycle().setCycleState(CycleState.ACTIVE);
    when(companyService.getAllCompanies()).thenReturn(Collections.singletonList(okrCompany));
    OkrDepartment okrDepartment = new OkrDepartment();
    okrCompany.getOkrChildUnits().add(okrDepartment);
    okrDepartment.setParentOkrUnit(okrCompany);

    UserSettings userSettings = this.userSettingsService.getUserSettingsByUser(user);

    assertEquals(okrDepartment, userSettings.getDefaultTeam());
    verify(userSettingsRepository, times(1)).save(any());
    verify(companyService, times(1)).getAllCompanies();
  }

  @Test
  public void getUserSettingsByUser_expectedDepartmentIsSetIfUserIsSponsorInOnlyOneTeam() {
    when(userSettingsRepository.findUserSettingsByUserId(userId)).thenReturn(null);
    OkrCompany okrCompany = new OkrCompany();
    okrCompany.setCycle(new Cycle());
    okrCompany.getCycle().setCycleState(CycleState.ACTIVE);
    when(companyService.getAllCompanies()).thenReturn(Collections.singletonList(okrCompany));
    whenUserSettingsSaveReturnSameValue();
    OkrBranch okrBranch = new OkrBranch();
    OkrDepartment subOkrDepartmentWithUserAsSponsor = new OkrDepartment();
    subOkrDepartmentWithUserAsSponsor.setOkrTopicSponsorId(userId);
    OkrDepartment subOkrDepartment = new OkrDepartment();
    okrCompany.getOkrChildUnits().add(okrBranch);
    okrBranch.setParentOkrUnit(okrCompany);
    okrBranch.setOkrChildUnits(Arrays.asList(subOkrDepartment, subOkrDepartmentWithUserAsSponsor));

    UserSettings userSettings = this.userSettingsService.getUserSettingsByUser(user);

    assertEquals(subOkrDepartmentWithUserAsSponsor, userSettings.getDefaultTeam());
    verify(userSettingsRepository, times(1)).save(any());
    verify(companyService, times(1)).getAllCompanies();
  }

  @Test
  public void getUserSettingsByUser_expectedDepartmentIsSetIfUserIsOkrMasterInOnlyOneTeam() {
    when(userSettingsRepository.findUserSettingsByUserId(userId)).thenReturn(null);
    OkrCompany okrCompany = new OkrCompany();
    okrCompany.setCycle(new Cycle());
    okrCompany.getCycle().setCycleState(CycleState.ACTIVE);
    when(companyService.getAllCompanies()).thenReturn(Collections.singletonList(okrCompany));
    whenUserSettingsSaveReturnSameValue();
    OkrBranch okrBranch = new OkrBranch();
    OkrDepartment subOkrDepartmentWithUserAsOkrMaster = new OkrDepartment();
    subOkrDepartmentWithUserAsOkrMaster.setOkrMasterId(userId);
    OkrDepartment subOkrDepartment = new OkrDepartment();
    okrCompany.getOkrChildUnits().add(okrBranch);
    okrBranch.setParentOkrUnit(okrCompany);
    okrBranch.setOkrChildUnits(
      Arrays.asList(subOkrDepartment, subOkrDepartmentWithUserAsOkrMaster));

    UserSettings userSettings = this.userSettingsService.getUserSettingsByUser(user);

    assertEquals(subOkrDepartmentWithUserAsOkrMaster, userSettings.getDefaultTeam());
    verify(userSettingsRepository, times(1)).save(any());
    verify(companyService, times(1)).getAllCompanies();
  }

  @Test
  public void getUserSettingsByUser_expectDepartmentIsNotSetIfUserIsSponsorInMultipleTeams() {
    when(userSettingsRepository.findUserSettingsByUserId(userId)).thenReturn(null);
    OkrCompany okrCompany = new OkrCompany();
    okrCompany.setCycle(new Cycle());
    okrCompany.getCycle().setCycleState(CycleState.ACTIVE);
    when(companyService.getAllCompanies()).thenReturn(Collections.singletonList(okrCompany));
    whenUserSettingsSaveReturnSameValue();
    OkrBranch okrBranch = new OkrBranch();
    OkrDepartment subOkrDepartmentWithUserAsSponsor = new OkrDepartment();
    subOkrDepartmentWithUserAsSponsor.setOkrTopicSponsorId(userId);
    OkrDepartment subOkrDepartmentWithUserAsSponsor1 = new OkrDepartment();
    subOkrDepartmentWithUserAsSponsor1.setOkrTopicSponsorId(userId);
    okrCompany.getOkrChildUnits().add(okrBranch);
    okrBranch.setParentOkrUnit(okrCompany);
    okrBranch.setOkrChildUnits(
      Arrays.asList(subOkrDepartmentWithUserAsSponsor1, subOkrDepartmentWithUserAsSponsor));

    UserSettings userSettings = this.userSettingsService.getUserSettingsByUser(user);

    assertNull(userSettings.getDefaultTeam());
    verify(userSettingsRepository, times(1)).save(any());
    verify(companyService, times(1)).getAllCompanies();
  }

  @Test
  public void getUserSettingsByUser_expectDepartmentIsNotSetIfUserIsOkrMasterInMultipleTeams() {
    when(userSettingsRepository.findUserSettingsByUserId(userId)).thenReturn(null);
    whenUserSettingsSaveReturnSameValue();
    OkrCompany okrCompany = new OkrCompany();
    okrCompany.setCycle(new Cycle());
    okrCompany.getCycle().setCycleState(CycleState.ACTIVE);
    when(companyService.getAllCompanies()).thenReturn(Collections.singletonList(okrCompany));
    OkrBranch okrBranch = new OkrBranch();
    OkrDepartment subOkrDepartmentWithUserAsOkrMaster = new OkrDepartment();
    subOkrDepartmentWithUserAsOkrMaster.setOkrMasterId(userId);
    OkrDepartment subOkrDepartmentWithUserAsOkrMaster1 = new OkrDepartment();
    subOkrDepartmentWithUserAsOkrMaster1.setOkrMasterId(userId);
    okrCompany.getOkrChildUnits().add(okrBranch);
    okrBranch.setParentOkrUnit(okrCompany);
    okrBranch.setOkrChildUnits(
      Arrays.asList(subOkrDepartmentWithUserAsOkrMaster1, subOkrDepartmentWithUserAsOkrMaster));

    UserSettings userSettings = this.userSettingsService.getUserSettingsByUser(user);

    assertNull(userSettings.getDefaultTeam());
    verify(userSettingsRepository, times(1)).save(any());
    verify(companyService, times(1)).getAllCompanies();
  }

  @Test
  public void
  getUserSettingsByUser_expectDepartmentIsNotSetIfUserIsOkrMasterOrTopicSponsorInMultipleTeams() {
    when(userSettingsRepository.findUserSettingsByUserId(userId)).thenReturn(null);
    OkrCompany okrCompany = new OkrCompany();
    okrCompany.setCycle(new Cycle());
    okrCompany.getCycle().setCycleState(CycleState.ACTIVE);
    when(companyService.getAllCompanies()).thenReturn(Collections.singletonList(okrCompany));
    whenUserSettingsSaveReturnSameValue();
    OkrBranch okrBranch = new OkrBranch();
    OkrDepartment subOkrDepartmentWithUserAsOkrMaster = new OkrDepartment();
    subOkrDepartmentWithUserAsOkrMaster.setOkrMasterId(userId);
    OkrDepartment subOkrDepartmentWithUserAsTopicSponsor = new OkrDepartment();
    subOkrDepartmentWithUserAsTopicSponsor.setOkrTopicSponsorId(userId);
    okrCompany.getOkrChildUnits().add(okrBranch);
    okrBranch.setParentOkrUnit(okrCompany);
    okrBranch.setOkrChildUnits(
      Arrays.asList(subOkrDepartmentWithUserAsTopicSponsor, subOkrDepartmentWithUserAsOkrMaster));

    UserSettings userSettings = this.userSettingsService.getUserSettingsByUser(user);

    assertNull(userSettings.getDefaultTeam());
    verify(userSettingsRepository, times(1)).save(any());
    verify(companyService, times(1)).getAllCompanies();
  }

  @Test
  public void getUserSettingsByUser_expectedDepartmentIsSetIfUserIsMemberInOnlyOneTeam() {
    when(userSettingsRepository.findUserSettingsByUserId(userId)).thenReturn(null);
    whenUserSettingsSaveReturnSameValue();
    OkrCompany okrCompany = new OkrCompany();
    okrCompany.setCycle(new Cycle());
    okrCompany.getCycle().setCycleState(CycleState.ACTIVE);
    when(companyService.getAllCompanies()).thenReturn(Collections.singletonList(okrCompany));
    OkrBranch okrBranch = new OkrBranch();
    OkrDepartment subOkrDepartmentWithUserAsMember = new OkrDepartment();
    subOkrDepartmentWithUserAsMember.setOkrMemberIds(Collections.singleton(userId));
    OkrDepartment subOkrDepartment = new OkrDepartment();
    okrCompany.getOkrChildUnits().add(okrBranch);
    okrBranch.setParentOkrUnit(okrCompany);
    okrBranch.setOkrChildUnits(Arrays.asList(subOkrDepartment, subOkrDepartmentWithUserAsMember));

    UserSettings userSettings = this.userSettingsService.getUserSettingsByUser(user);

    assertEquals(subOkrDepartmentWithUserAsMember, userSettings.getDefaultTeam());
    verify(userSettingsRepository, times(1)).save(any());
    verify(companyService, times(1)).getAllCompanies();
  }

  @Test
  public void getUserSettingsByUser_expectDepartmentIsNotSetIfUserMemberInMultipleTeams() {
    when(userSettingsRepository.findUserSettingsByUserId(userId)).thenReturn(null);
    OkrCompany okrCompany = new OkrCompany();
    okrCompany.setCycle(new Cycle());
    okrCompany.getCycle().setCycleState(CycleState.ACTIVE);
    when(companyService.getAllCompanies()).thenReturn(Collections.singletonList(okrCompany));
    whenUserSettingsSaveReturnSameValue();
    OkrBranch okrBranch = new OkrBranch();
    OkrDepartment okrDepartmentWithUserAsMember = new OkrDepartment();
    okrDepartmentWithUserAsMember.getOkrMemberIds().add(userId);
    OkrDepartment okrDepartmentWithUserAsMember1 = new OkrDepartment();
    okrDepartmentWithUserAsMember1.getOkrMemberIds().add(userId);
    okrCompany.getOkrChildUnits().add(okrBranch);
    okrBranch.setParentOkrUnit(okrCompany);
    okrBranch.setOkrChildUnits(
      Arrays.asList(okrDepartmentWithUserAsMember1, okrDepartmentWithUserAsMember));

    UserSettings userSettings = this.userSettingsService.getUserSettingsByUser(user);

    assertNull(userSettings.getDefaultTeam());
    verify(userSettingsRepository, times(1)).save(any());
    verify(companyService, times(1)).getAllCompanies();
  }

  @Test
  public void getUserSettingsByUser_expectUserSettingsByUser() {
    UserSettings storedUserSettings = new UserSettings();
    Long userSettingsId = 40L;
    storedUserSettings.setUserId(userId);
    storedUserSettings.setId(userSettingsId);
    when(userSettingsRepository.findUserSettingsByUserId(userId)).thenReturn(storedUserSettings);

    UserSettings userSettings = this.userSettingsService.getUserSettingsByUser(user);

    assertEquals(userId, userSettings.getUserId());
    assertEquals(userSettingsId, userSettings.getId());
    verify(userSettingsRepository, times(0)).save(any());
  }

  @Test
  public void updateUserSettings_expectEntityNotFoundExceptionIfThereIsNoSuchUserSettings() {
    Long id = 400L;
    when(userSettingsRepository.findByIdOrThrow(id)).thenThrow(EntityNotFoundException.class);
    UserSettings userSettings = new UserSettings();
    userSettings.setId(id);
    try {
      this.userSettingsService.updateUserSettings(userSettings, user);
      fail();
    } catch (Exception ex) {
      assertEquals(ex.getClass(), EntityNotFoundException.class);
    }
  }

  @Test
  public void updateUserSettings_expectEntityShouldBeUpdated() {
    Long id = 400L;
    UserSettings dbUserSettings = new UserSettings();
    dbUserSettings.setId(400L);
    dbUserSettings.setUserId(userId);
    when(userSettingsRepository.findByIdOrThrow(id)).thenReturn(dbUserSettings);
    UserSettings userSettingsParam = new UserSettings();
    userSettingsParam.setId(id);
    userSettingsParam.setUserId(userId);
    Long defaultCompanyId = 550L;
    Long defaultTeamId = 1300L;
    OkrCompany defaultOkrCompany = new OkrCompany();
    defaultOkrCompany.setId(defaultCompanyId);
    OkrDepartment defaultTeam = new OkrDepartment();
    defaultOkrCompany.setId(defaultTeamId);
    userSettingsParam.setDefaultOkrCompany(defaultOkrCompany);
    userSettingsParam.setDefaultTeam(defaultTeam);

    UserSettings userSettingsResult =
      this.userSettingsService.updateUserSettings(userSettingsParam, user);

    assertEquals(userSettingsParam.getId(), userSettingsResult.getId());
    assertEquals(userSettingsParam.getUserId(), userSettingsResult.getUserId());
    assertEquals(
      userSettingsParam.getDefaultOkrCompany().getId(),
      userSettingsParam.getDefaultOkrCompany().getId()
    );
    assertEquals(
      userSettingsParam.getDefaultTeam().getId(), userSettingsParam.getDefaultTeam().getId());
    verify(userSettingsRepository, times(1)).save(any());
    verify(userSettingsRepository, times(1)).findByIdOrThrow(id);
  }

  @Test
  public void updateUserSetting_expectEntitysDefaultCompanyShouldBeUpdated() {
    Long id = 400L;
    UserSettings dbUserSettings = new UserSettings();
    dbUserSettings.setId(400L);
    dbUserSettings.setUserId(userId);
    when(userSettingsRepository.findByIdOrThrow(id)).thenReturn(dbUserSettings);
    UserSettings userSettingsParam = new UserSettings();
    userSettingsParam.setId(id);
    userSettingsParam.setUserId(userId);
    Long defaultCompanyId = 550L;
    OkrCompany defaultOkrCompany = new OkrCompany();
    defaultOkrCompany.setId(defaultCompanyId);
    userSettingsParam.setDefaultOkrCompany(defaultOkrCompany);

    UserSettings userSettingsResult =
      this.userSettingsService.updateUserSettings(userSettingsParam, user);

    assertEquals(userSettingsParam.getId(), userSettingsResult.getId());
    assertEquals(userSettingsParam.getUserId(), userSettingsResult.getUserId());
    assertEquals(
      userSettingsParam.getDefaultOkrCompany().getId(),
      userSettingsParam.getDefaultOkrCompany().getId()
    );
    verify(userSettingsRepository, times(1)).save(any());
    verify(userSettingsRepository, times(1)).findByIdOrThrow(id);
  }

  @Test
  public void updateUserSetting_expectEntitysDefaultTeamShouldBeUpdated() {
    Long id = 400L;
    UserSettings dbUserSettings = new UserSettings();
    dbUserSettings.setId(400L);
    dbUserSettings.setUserId(userId);
    when(userSettingsRepository.findByIdOrThrow(id)).thenReturn(dbUserSettings);
    UserSettings userSettingsParam = new UserSettings();
    userSettingsParam.setId(id);
    userSettingsParam.setUserId(userId);
    Long defaultTeamId = 1300L;
    OkrDepartment defaultTeam = new OkrDepartment();
    defaultTeam.setId(defaultTeamId);
    userSettingsParam.setDefaultTeam(defaultTeam);

    UserSettings userSettingsResult =
      this.userSettingsService.updateUserSettings(userSettingsParam, user);

    assertEquals(userSettingsParam.getId(), userSettingsResult.getId());
    assertEquals(userSettingsParam.getUserId(), userSettingsResult.getUserId());
    assertEquals(
      userSettingsParam.getDefaultTeam().getId(), userSettingsParam.getDefaultTeam().getId());
    verify(userSettingsRepository, times(1)).save(any());
    verify(userSettingsRepository, times(1)).findByIdOrThrow(id);
  }

  private void whenUserSettingsSaveReturnSameValue() {
    when(userSettingsRepository.save(any(UserSettings.class)))
      .thenAnswer(
        (Answer<UserSettings>) invocation -> (UserSettings) invocation.getArguments()[0]);
  }
}
