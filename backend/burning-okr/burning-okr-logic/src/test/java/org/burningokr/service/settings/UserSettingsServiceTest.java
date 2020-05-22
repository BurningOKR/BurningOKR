package org.burningokr.service.settings;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;
import javax.persistence.EntityNotFoundException;
import org.burningokr.model.cycles.Cycle;
import org.burningokr.model.cycles.CycleState;
import org.burningokr.model.settings.UserSettings;
import org.burningokr.model.structures.Company;
import org.burningokr.model.structures.Department;
import org.burningokr.model.users.User;
import org.burningokr.repositories.settings.UserSettingsRepository;
import org.burningokr.service.activity.ActivityService;
import org.burningokr.service.structure.CompanyService;
import org.burningokr.service.structure.StructureService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

@RunWith(MockitoJUnitRunner.class)
public class UserSettingsServiceTest {
  private static final UUID userId = UUID.randomUUID();
  private static User user;

  @Mock private UserSettingsRepository userSettingsRepository;

  @Mock private ActivityService activityService;

  @Mock private StructureService<Department> departmentService;

  @Mock private CompanyService companyService;

  @InjectMocks private UserSettingsService userSettingsService;

  @BeforeClass
  public static void initClass() {
    user = mock(User.class);
    when(user.getId()).thenReturn(userId);
  }

  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void getUserSettingsByUser_expectNewSettingsBeingCreatedIfThereNoSuchUsersUserSettings() {
    whenUserSettingsSaveReturnSameValue();

    UserSettings userSettings = this.userSettingsService.getUserSettingsByUser(user);

    Assert.assertEquals(userId, userSettings.getUserId());
    verify(userSettingsRepository, times(1)).save(any());
  }

  @Test
  public void getUserSettingsByUser_expectNoCompanyIfThereIsMoreThanOneCompany() {
    when(userSettingsRepository.findUserSettingsByUserId(userId)).thenReturn(null);
    whenUserSettingsSaveReturnSameValue();
    Company company = new Company();
    company.setCycle(new Cycle());
    company.getCycle().setCycleState(CycleState.ACTIVE);
    Company company1 = new Company();
    company1.setCycle(new Cycle());
    company1.getCycle().setCycleState(CycleState.ACTIVE);
    when(companyService.getAllCompanies()).thenReturn(Arrays.asList(company, company1));

    UserSettings userSettings = this.userSettingsService.getUserSettingsByUser(user);

    Assert.assertNull(userSettings.getDefaultCompany());
    verify(userSettingsRepository, times(1)).save(any());
    verify(companyService, times(1)).getAllCompanies();
  }

  @Test
  public void getUserSettingsByUser_expectCompanyIsSetIfThereIsOnlyOneCompany() {
    when(userSettingsRepository.findUserSettingsByUserId(userId)).thenReturn(null);
    Company company = new Company();
    company.setCycle(new Cycle());
    company.getCycle().setCycleState(CycleState.ACTIVE);
    when(companyService.getAllCompanies()).thenReturn(Collections.singletonList(company));
    whenUserSettingsSaveReturnSameValue();

    UserSettings userSettings = this.userSettingsService.getUserSettingsByUser(user);

    Assert.assertEquals(company, userSettings.getDefaultCompany());
    verify(userSettingsRepository, times(1)).save(any());
    verify(companyService, times(1)).getAllCompanies();
  }

  @Test
  public void getUserSettingsByUser_expectDepartmentIsSetIfThereIsOnlyOneDepartment() {
    when(userSettingsRepository.findUserSettingsByUserId(userId)).thenReturn(null);
    whenUserSettingsSaveReturnSameValue();
    Company company = new Company();
    company.setCycle(new Cycle());
    company.getCycle().setCycleState(CycleState.ACTIVE);
    when(companyService.getAllCompanies()).thenReturn(Collections.singletonList(company));
    Department department = new Department();
    company.getDepartments().add(department);
    department.setParentStructure(company);

    UserSettings userSettings = this.userSettingsService.getUserSettingsByUser(user);

    Assert.assertEquals(department, userSettings.getDefaultTeam());
    verify(userSettingsRepository, times(1)).save(any());
    verify(companyService, times(1)).getAllCompanies();
  }

  @Test
  public void getUserSettingsByUser_expectedDepartmentIsSetIfUserIsSponsorInOnlyOneTeam() {
    when(userSettingsRepository.findUserSettingsByUserId(userId)).thenReturn(null);
    Company company = new Company();
    company.setCycle(new Cycle());
    company.getCycle().setCycleState(CycleState.ACTIVE);
    when(companyService.getAllCompanies()).thenReturn(Collections.singletonList(company));
    whenUserSettingsSaveReturnSameValue();
    Department department = new Department();
    Department subDepartmentWithUserAsSponsor = new Department();
    subDepartmentWithUserAsSponsor.setOkrTopicSponsorId(userId);
    Department subDepartment = new Department();
    company.getDepartments().add(department);
    department.setParentStructure(company);
    department.setDepartments(Arrays.asList(subDepartment, subDepartmentWithUserAsSponsor));

    UserSettings userSettings = this.userSettingsService.getUserSettingsByUser(user);

    Assert.assertEquals(subDepartmentWithUserAsSponsor, userSettings.getDefaultTeam());
    verify(userSettingsRepository, times(1)).save(any());
    verify(companyService, times(1)).getAllCompanies();
  }

  @Test
  public void getUserSettingsByUser_expectedDepartmentIsSetIfUserIsOkrMasterInOnlyOneTeam() {
    when(userSettingsRepository.findUserSettingsByUserId(userId)).thenReturn(null);
    Company company = new Company();
    company.setCycle(new Cycle());
    company.getCycle().setCycleState(CycleState.ACTIVE);
    when(companyService.getAllCompanies()).thenReturn(Collections.singletonList(company));
    whenUserSettingsSaveReturnSameValue();
    Department department = new Department();
    Department subDepartmentWithUserAsOkrMaster = new Department();
    subDepartmentWithUserAsOkrMaster.setOkrMasterId(userId);
    Department subDepartment = new Department();
    company.getDepartments().add(department);
    department.setParentStructure(company);
    department.setDepartments(Arrays.asList(subDepartment, subDepartmentWithUserAsOkrMaster));

    UserSettings userSettings = this.userSettingsService.getUserSettingsByUser(user);

    Assert.assertEquals(subDepartmentWithUserAsOkrMaster, userSettings.getDefaultTeam());
    verify(userSettingsRepository, times(1)).save(any());
    verify(companyService, times(1)).getAllCompanies();
  }

  @Test
  public void getUserSettingsByUser_expectDepartmentIsNotSetIfUserIsSponsorInMultipleTeams() {
    when(userSettingsRepository.findUserSettingsByUserId(userId)).thenReturn(null);
    Company company = new Company();
    company.setCycle(new Cycle());
    company.getCycle().setCycleState(CycleState.ACTIVE);
    when(companyService.getAllCompanies()).thenReturn(Collections.singletonList(company));
    whenUserSettingsSaveReturnSameValue();
    Department department = new Department();
    Department subDepartmentWithUserAsSponsor = new Department();
    subDepartmentWithUserAsSponsor.setOkrTopicSponsorId(userId);
    Department subDepartmentWithUserAsSponsor1 = new Department();
    subDepartmentWithUserAsSponsor1.setOkrTopicSponsorId(userId);
    company.getDepartments().add(department);
    department.setParentStructure(company);
    department.setDepartments(
        Arrays.asList(subDepartmentWithUserAsSponsor1, subDepartmentWithUserAsSponsor));

    UserSettings userSettings = this.userSettingsService.getUserSettingsByUser(user);

    Assert.assertNull(userSettings.getDefaultTeam());
    verify(userSettingsRepository, times(1)).save(any());
    verify(companyService, times(1)).getAllCompanies();
  }

  @Test
  public void getUserSettingsByUser_expectDepartmentIsNotSetIfUserIsOkrMasterInMultipleTeams() {
    when(userSettingsRepository.findUserSettingsByUserId(userId)).thenReturn(null);
    whenUserSettingsSaveReturnSameValue();
    Company company = new Company();
    company.setCycle(new Cycle());
    company.getCycle().setCycleState(CycleState.ACTIVE);
    when(companyService.getAllCompanies()).thenReturn(Collections.singletonList(company));
    Department department = new Department();
    Department subDepartmentWithUserAsOkrMaster = new Department();
    subDepartmentWithUserAsOkrMaster.setOkrMasterId(userId);
    Department subDepartmentWithUserAsOkrMaster1 = new Department();
    subDepartmentWithUserAsOkrMaster1.setOkrMasterId(userId);
    company.getDepartments().add(department);
    department.setParentStructure(company);
    department.setDepartments(
        Arrays.asList(subDepartmentWithUserAsOkrMaster1, subDepartmentWithUserAsOkrMaster));

    UserSettings userSettings = this.userSettingsService.getUserSettingsByUser(user);

    Assert.assertNull(userSettings.getDefaultTeam());
    verify(userSettingsRepository, times(1)).save(any());
    verify(companyService, times(1)).getAllCompanies();
  }

  @Test
  public void
      getUserSettingsByUser_expectDepartmentIsNotSetIfUserIsOkrMasterOrTopicSponsorInMultipleTeams() {
    when(userSettingsRepository.findUserSettingsByUserId(userId)).thenReturn(null);
    Company company = new Company();
    company.setCycle(new Cycle());
    company.getCycle().setCycleState(CycleState.ACTIVE);
    when(companyService.getAllCompanies()).thenReturn(Collections.singletonList(company));
    whenUserSettingsSaveReturnSameValue();
    Department department = new Department();
    Department subDepartmentWithUserAsOkrMaster = new Department();
    subDepartmentWithUserAsOkrMaster.setOkrMasterId(userId);
    Department subDepartmentWithUserAsTopicSponsor = new Department();
    subDepartmentWithUserAsTopicSponsor.setOkrTopicSponsorId(userId);
    company.getDepartments().add(department);
    department.setParentStructure(company);
    department.setDepartments(
        Arrays.asList(subDepartmentWithUserAsTopicSponsor, subDepartmentWithUserAsOkrMaster));

    UserSettings userSettings = this.userSettingsService.getUserSettingsByUser(user);

    Assert.assertNull(userSettings.getDefaultTeam());
    verify(userSettingsRepository, times(1)).save(any());
    verify(companyService, times(1)).getAllCompanies();
  }

  @Test
  public void getUserSettingsByUser_expectedDepartmentIsSetIfUserIsMemberInOnlyOneTeam() {
    when(userSettingsRepository.findUserSettingsByUserId(userId)).thenReturn(null);
    whenUserSettingsSaveReturnSameValue();
    Company company = new Company();
    company.setCycle(new Cycle());
    company.getCycle().setCycleState(CycleState.ACTIVE);
    when(companyService.getAllCompanies()).thenReturn(Collections.singletonList(company));
    Department department = new Department();
    Department subDepartmentWithUserAsMember = new Department();
    subDepartmentWithUserAsMember.setOkrMemberIds(Collections.singleton(userId));
    Department subDepartment = new Department();
    company.getDepartments().add(department);
    department.setParentStructure(company);
    department.setDepartments(Arrays.asList(subDepartment, subDepartmentWithUserAsMember));

    UserSettings userSettings = this.userSettingsService.getUserSettingsByUser(user);

    Assert.assertEquals(subDepartmentWithUserAsMember, userSettings.getDefaultTeam());
    verify(userSettingsRepository, times(1)).save(any());
    verify(companyService, times(1)).getAllCompanies();
  }

  @Test
  public void getUserSettingsByUser_expectDepartmentIsNotSetIfUserMemberInMultipleTeams() {
    when(userSettingsRepository.findUserSettingsByUserId(userId)).thenReturn(null);
    Company company = new Company();
    company.setCycle(new Cycle());
    company.getCycle().setCycleState(CycleState.ACTIVE);
    when(companyService.getAllCompanies()).thenReturn(Collections.singletonList(company));
    whenUserSettingsSaveReturnSameValue();
    Department department = new Department();
    Department departmentWithUserAsMember = new Department();
    departmentWithUserAsMember.getOkrMemberIds().add(userId);
    Department departmentWithUserAsMember1 = new Department();
    departmentWithUserAsMember1.getOkrMemberIds().add(userId);
    company.getDepartments().add(department);
    department.setParentStructure(company);
    department.setDepartments(
        Arrays.asList(departmentWithUserAsMember1, departmentWithUserAsMember));

    UserSettings userSettings = this.userSettingsService.getUserSettingsByUser(user);

    Assert.assertNull(userSettings.getDefaultTeam());
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

    Assert.assertEquals(userId, userSettings.getUserId());
    Assert.assertEquals(userSettingsId, userSettings.getId());
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
      Assert.fail();
    } catch (Exception ex) {
      assertThat(
          "Should only throw EntityNotFoundException.",
          ex,
          instanceOf(EntityNotFoundException.class));
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
    Company defaultCompany = new Company();
    defaultCompany.setId(defaultCompanyId);
    Department defaultTeam = new Department();
    defaultCompany.setId(defaultTeamId);
    userSettingsParam.setDefaultCompany(defaultCompany);
    userSettingsParam.setDefaultTeam(defaultTeam);

    UserSettings userSettingsResult =
        this.userSettingsService.updateUserSettings(userSettingsParam, user);

    Assert.assertEquals(userSettingsParam.getId(), userSettingsResult.getId());
    Assert.assertEquals(userSettingsParam.getUserId(), userSettingsResult.getUserId());
    Assert.assertEquals(
        userSettingsParam.getDefaultCompany().getId(),
        userSettingsParam.getDefaultCompany().getId());
    Assert.assertEquals(
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
    Company defaultCompany = new Company();
    defaultCompany.setId(defaultCompanyId);
    userSettingsParam.setDefaultCompany(defaultCompany);

    UserSettings userSettingsResult =
        this.userSettingsService.updateUserSettings(userSettingsParam, user);

    Assert.assertEquals(userSettingsParam.getId(), userSettingsResult.getId());
    Assert.assertEquals(userSettingsParam.getUserId(), userSettingsResult.getUserId());
    Assert.assertEquals(
        userSettingsParam.getDefaultCompany().getId(),
        userSettingsParam.getDefaultCompany().getId());
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
    Department defaultTeam = new Department();
    defaultTeam.setId(defaultTeamId);
    userSettingsParam.setDefaultTeam(defaultTeam);

    UserSettings userSettingsResult =
        this.userSettingsService.updateUserSettings(userSettingsParam, user);

    Assert.assertEquals(userSettingsParam.getId(), userSettingsResult.getId());
    Assert.assertEquals(userSettingsParam.getUserId(), userSettingsResult.getUserId());
    Assert.assertEquals(
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
