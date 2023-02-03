package org.burningokr.service.settings;

import com.google.common.collect.Lists;
import org.burningokr.model.activity.Action;
import org.burningokr.model.cycles.CycleState;
import org.burningokr.model.okrUnits.OkrCompany;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.burningokr.model.settings.UserSettings;
import org.burningokr.model.users.User;
import org.burningokr.repositories.settings.UserSettingsRepository;
import org.burningokr.service.activity.ActivityService;
import org.burningokr.service.okrUnit.CompanyService;
import org.burningokr.service.okrUnit.departmentservices.BranchHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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
      userSettings.setDefaultOkrCompany(getDefaultCompany());
      userSettings.setDefaultTeam(getDefaultTeam(user, userSettings.getDefaultOkrCompany()));
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

  private OkrCompany getDefaultCompany() {
    Collection<OkrCompany> okrCompanyCollection =
        companyService.getAllCompanies().stream()
            .filter(company -> company.getCycle().getCycleState() == CycleState.ACTIVE)
            .collect(Collectors.toList());
    if (okrCompanyCollection.size() == 1) {
      return (OkrCompany) okrCompanyCollection.toArray()[0];
    }
    return null;
  }

  private OkrDepartment getDefaultTeam(User user, OkrCompany okrCompany) {
    if (okrCompany == null) {
      return null;
    }
    List<OkrDepartment> okrDepartments =
        Lists.newArrayList(BranchHelper.collectDepartments(okrCompany));
    if (okrDepartments.size() == 1) {
      return okrDepartments.get(0);
    }
    List<OkrDepartment> departmentsWhereUserIsSponsor =
        okrDepartments.stream()
            .filter(department -> isUserSponsorOrOkrMasterInDepartment(user, department))
            .collect(Collectors.toList());
    if (departmentsWhereUserIsSponsor.size() == 1) {
      return departmentsWhereUserIsSponsor.get(0);
    }
    List<OkrDepartment> departmentsWhereUserIsMember =
        okrDepartments.stream()
            .filter(department -> isUserMemberInDepartment(user, department))
            .collect(Collectors.toList());
    if (departmentsWhereUserIsMember.size() == 1) {
      return departmentsWhereUserIsMember.get(0);
    }
    return null;
  }

  private boolean isUserSponsorOrOkrMasterInDepartment(User user, OkrDepartment okrDepartment) {
    return (okrDepartment.getOkrTopicSponsorId() != null
            && okrDepartment.getOkrTopicSponsorId().equals(user.getId()))
        || (okrDepartment.getOkrMasterId() != null
            && okrDepartment.getOkrMasterId().equals(user.getId()));
  }

  private boolean isUserMemberInDepartment(User user, OkrDepartment okrDepartment) {
    return okrDepartment.getOkrMemberIds().stream()
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

    dbUserSettings.setDefaultOkrCompany(userSettings.getDefaultOkrCompany());
    dbUserSettings.setDefaultTeam(userSettings.getDefaultTeam());

    userSettingsRepository.save(dbUserSettings);

    return dbUserSettings;
  }
}
