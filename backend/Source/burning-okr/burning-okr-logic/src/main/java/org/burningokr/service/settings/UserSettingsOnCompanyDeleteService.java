package org.burningokr.service.settings;

import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.burningokr.model.settings.UserSettings;
import org.burningokr.model.structures.Company;
import org.burningokr.repositories.settings.UserSettingsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserSettingsOnCompanyDeleteService {
  private final Logger logger = LoggerFactory.getLogger(UserSettingsService.class);
  private UserSettingsRepository userSettingsRepository;

  @Autowired
  public UserSettingsOnCompanyDeleteService(UserSettingsRepository userSettingsRepository) {
    this.userSettingsRepository = userSettingsRepository;
  }

  /**
   * Removes Default Company from UserSettings when Company is deleted.
   *
   * @param companiesToDelete a {@link Collection} of {@link Company}
   * @return a {@link List} of {@link UserSettings}
   */
  public List<UserSettings> removeCompaniesFromSettings(Collection<Company> companiesToDelete) {
    List<UserSettings> allUserSettings =
        Lists.newArrayList(userSettingsRepository.findAll()).stream()
            .filter(
                userSettings ->
                    userSettings.getDefaultCompany() != null
                        && companiesToDelete.contains(userSettings.getDefaultCompany()))
            .collect(Collectors.toList());

    for (UserSettings userSetting : allUserSettings) {
      userSetting.setDefaultCompany(null);
      userSetting.setDefaultTeam(null);
    }
    return allUserSettings;
  }
}
