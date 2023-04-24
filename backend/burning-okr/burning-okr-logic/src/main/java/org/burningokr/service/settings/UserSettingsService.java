package org.burningokr.service.settings;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.burningokr.model.activity.Action;
import org.burningokr.model.cycles.CycleState;
import org.burningokr.model.okrUnits.OkrCompany;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.burningokr.model.settings.UserSettings;
import org.burningokr.model.users.IUser;
import org.burningokr.repositories.settings.UserSettingsRepository;
import org.burningokr.service.activity.ActivityService;
import org.burningokr.service.okrUnit.CompanyService;
import org.burningokr.service.okrUnit.departmentservices.BranchHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserSettingsService {

  private final Logger logger = LoggerFactory.getLogger(UserSettingsService.class);
  private final UserSettingsRepository userSettingsRepository;
  private final ActivityService activityService;
  private final CompanyService companyService;

  /**
   * Gets the User Settings of a given User.
   *
   * @param IUser an {@link IUser} object
   * @return an {@link UserSettings} object
   */
  public UserSettings getUserSettingsByUser(IUser IUser) {
    UserSettings userSettings = userSettingsRepository.findUserSettingsByUserId(IUser.getId());
    if (userSettings == null) {
      userSettings = new UserSettings();
      userSettings.setUserId(IUser.getId());
      userSettings.setDefaultOkrCompany(getDefaultCompany());
      userSettings.setDefaultTeam(getDefaultTeam(IUser, userSettings.getDefaultOkrCompany()));
      userSettings = userSettingsRepository.save(userSettings);
      activityService.createActivity(userSettings, Action.CREATED);
      logger.debug(
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
        .toList();
    if (okrCompanyCollection.size() == 1) {
      return (OkrCompany) okrCompanyCollection.toArray()[0];
    }
    return null;
  }

  private OkrDepartment getDefaultTeam(IUser IUser, OkrCompany okrCompany) {
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
        .filter(department -> isUserSponsorOrOkrMasterInDepartment(IUser, department))
        .toList();
    if (departmentsWhereUserIsSponsor.size() == 1) {
      return departmentsWhereUserIsSponsor.get(0);
    }
    List<OkrDepartment> departmentsWhereUserIsMember =
      okrDepartments.stream()
        .filter(department -> isUserMemberInDepartment(IUser, department))
        .toList();
    if (departmentsWhereUserIsMember.size() == 1) {
      return departmentsWhereUserIsMember.get(0);
    }
    return null;
  }

  private boolean isUserSponsorOrOkrMasterInDepartment(IUser IUser, OkrDepartment okrDepartment) {
    return (okrDepartment.getOkrTopicSponsorId() != null
      && okrDepartment.getOkrTopicSponsorId().equals(IUser.getId()))
      || (okrDepartment.getOkrMasterId() != null
      && okrDepartment.getOkrMasterId().equals(IUser.getId()));
  }

  private boolean isUserMemberInDepartment(IUser IUser, OkrDepartment okrDepartment) {
    return okrDepartment.getOkrMemberIds().stream()
      .anyMatch(memberId -> memberId.equals(IUser.getId()));
  }

  /**
   * Updates the User Settings.
   *
   * @param userSettings an {@link UserSettings} object
   * @return an {@link UserSettings} object
   */
  public UserSettings updateUserSettings(UserSettings userSettings) {
    UserSettings dbUserSettings = userSettingsRepository.findByIdOrThrow(userSettings.getId());

    dbUserSettings.setDefaultOkrCompany(userSettings.getDefaultOkrCompany());
    dbUserSettings.setDefaultTeam(userSettings.getDefaultTeam());

    userSettingsRepository.save(dbUserSettings);

    return dbUserSettings;
  }
}
