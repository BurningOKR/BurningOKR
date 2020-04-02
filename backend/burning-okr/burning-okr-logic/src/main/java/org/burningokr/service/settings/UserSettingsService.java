package org.burningokr.service.settings;

import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.burningokr.model.activity.Action;
import org.burningokr.model.cycles.CycleState;
import org.burningokr.model.settings.UserSettings;
import org.burningokr.model.structures.Company;
import org.burningokr.model.structures.Department;
import org.burningokr.model.users.User;
import org.burningokr.repositories.settings.UserSettingsRepository;
import org.burningokr.service.activity.ActivityService;
import org.burningokr.service.structure.CompanyService;
import org.burningokr.service.structure.departmentservices.DepartmentHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserSettingsService {

  private final Logger logger = LoggerFactory.getLogger(UserSettingsService.class);
  private UserSettingsRepository userSettingsRepository;
  private ActivityService activityService;
  private CompanyService companyService;

  /**
   * Initialize UserSettingsService.
   *
   * @param userSettingsRepository an {@link UserSettingsRepository} object
   * @param activityService an {@link ActivityService} object
   * @param companyService a {@link CompanyService} object
   */
  @Autowired
  public UserSettingsService(
      UserSettingsRepository userSettingsRepository,
      ActivityService activityService,
      CompanyService companyService) {
    this.userSettingsRepository = userSettingsRepository;
    this.activityService = activityService;
    this.companyService = companyService;
  }

  /**
   * Gets the User Settings of a given User.
   *
   * @param user an {@link User} object
   * @return an {@link UserSettings} object
   */
  public UserSettings getUserSettingsByUser(User user) {
    UserSettings userSettings = userSettingsRepository.findUserSettingsByUserId(user.getId());
    if (userSettings == null) {
      userSettings = new UserSettings();
      userSettings.setUserId(user.getId());
      userSettings.setDefaultCompany(getDefaultCompany());
      userSettings.setDefaultTeam(getDefaultTeam(user, userSettings.getDefaultCompany()));
      userSettings = userSettingsRepository.save(userSettings);
      activityService.createActivity(user, userSettings, Action.CREATED);
      logger.info(
          "Created User Settings "
              + userSettings.getName()
              + "(id: "
              + userSettings.getId()
              + ") ");
    }
    return userSettings;
  }

  private Company getDefaultCompany() {
    Collection<Company> companyCollection =
        companyService.getAllCompanies().stream()
            .filter(company -> company.getCycle().getCycleState() == CycleState.ACTIVE)
            .collect(Collectors.toList());
    if (companyCollection.size() == 1) {
      return (Company) companyCollection.toArray()[0];
    }
    return null;
  }

  private Department getDefaultTeam(User user, Company company) {
    if (company == null) {
      return null;
    }
    List<Department> departments = Lists.newArrayList(DepartmentHelper.collectDepartments(company));
    if (departments.size() == 1) {
      return departments.get(0);
    }
    List<Department> departmentsWhereUserIsSponsor =
        departments.stream()
            .filter(department -> isUserSponsorOrOkrMasterInDepartment(user, department))
            .collect(Collectors.toList());
    if (departmentsWhereUserIsSponsor.size() == 1) {
      return departmentsWhereUserIsSponsor.get(0);
    }
    List<Department> departmentsWhereUserIsMember =
        departments.stream()
            .filter(department -> isUserMemberInDepartment(user, department))
            .collect(Collectors.toList());
    if (departmentsWhereUserIsMember.size() == 1) {
      return departmentsWhereUserIsMember.get(0);
    }
    return null;
  }

  private boolean isUserSponsorOrOkrMasterInDepartment(User user, Department department) {
    return (department.getOkrTopicSponsorId() != null
            && department.getOkrTopicSponsorId().equals(user.getId()))
        || (department.getOkrMasterId() != null
            && department.getOkrMasterId().equals(user.getId()));
  }

  private boolean isUserMemberInDepartment(User user, Department department) {
    return department.getOkrMemberIds().stream()
        .anyMatch(memberId -> memberId.equals(user.getId()));
  }

  /**
   * Updates the User Settings.
   *
   * @param userSettings an {@link UserSettings} object
   * @param user an {@link User} object
   * @return an {@link UserSettings} object
   */
  public UserSettings updateUserSettings(UserSettings userSettings, User user) {
    UserSettings dbUserSettings = userSettingsRepository.findByIdOrThrow(userSettings.getId());

    dbUserSettings.setDefaultCompany(userSettings.getDefaultCompany());
    dbUserSettings.setDefaultTeam(userSettings.getDefaultTeam());

    userSettingsRepository.save(dbUserSettings);

    return dbUserSettings;
  }
}
