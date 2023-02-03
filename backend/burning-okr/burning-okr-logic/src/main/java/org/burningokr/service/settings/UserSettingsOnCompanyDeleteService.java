package org.burningokr.service.settings;

import com.google.common.collect.Lists;
import org.burningokr.model.okrUnits.OkrCompany;
import org.burningokr.model.settings.UserSettings;
import org.burningokr.repositories.settings.UserSettingsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserSettingsOnCompanyDeleteService {
  private final Logger logger = LoggerFactory.getLogger(UserSettingsService.class);
  private UserSettingsRepository userSettingsRepository;

  @Autowired
  public UserSettingsOnCompanyDeleteService(UserSettingsRepository userSettingsRepository) {
    this.userSettingsRepository = userSettingsRepository;
  }

  /**
   * Removes Default OkrCompany from UserSettings when OkrCompany is deleted.
   *
   * @param companiesToDelete a {@link Collection} of {@link OkrCompany}
   * @return a {@link List} of {@link UserSettings}
   */
  public List<UserSettings> removeCompaniesFromSettings(Collection<OkrCompany> companiesToDelete) {
    List<UserSettings> allUserSettings =
      Lists.newArrayList(userSettingsRepository.findAll()).stream()
        .filter(
          userSettings ->
            userSettings.getDefaultOkrCompany() != null
              && companiesToDelete.contains(userSettings.getDefaultOkrCompany()))
        .collect(Collectors.toList());

    for (UserSettings userSetting : allUserSettings) {
      userSetting.setDefaultOkrCompany(null);
      userSetting.setDefaultTeam(null);
    }
    return allUserSettings;
  }
}
