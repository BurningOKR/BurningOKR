package org.burningokr.service.settings;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;
import org.assertj.core.util.Lists;
import org.burningokr.model.settings.UserSettings;
import org.burningokr.model.structures.Company;
import org.burningokr.repositories.settings.UserSettingsRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UserSettingsOnCompanyDeleteServiceTest {

  @Mock UserSettingsRepository userSettingsRepository;

  @InjectMocks UserSettingsOnCompanyDeleteService userSettingsOnCompanyDeleteService;

  private Company company1 = new Company();
  private Company company2 = new Company();

  @Before
  public void setup() {
    company1.setId(1L);
    company2.setId(2L);
    UserSettings userSettings1 = new UserSettings();
    UserSettings userSettings2 = new UserSettings();
    userSettings1.setDefaultCompany(company1);
    userSettings2.setDefaultCompany(company2);

    UserSettings[] userSettings = {userSettings1, userSettings2};

    when(userSettingsRepository.findAll()).thenReturn(Lists.newArrayList(userSettings));
  }

  @Test
  public void removeCompaniesFromSettings_shouldRemoveOneCompany() {
    long expected = 1;

    List<UserSettings> userSettings =
        userSettingsOnCompanyDeleteService.removeCompaniesFromSettings(
            Lists.newArrayList(company1));

    long actual =
        userSettings.stream()
            .filter(userSetting -> userSetting.getDefaultCompany() == null)
            .count();

    assertEquals(expected, actual);
  }

  @Test
  public void removeCompaniesFromSettings_shouldRemoveAllCompanies() {
    long expected = 2;

    List<UserSettings> userSettings =
        userSettingsOnCompanyDeleteService.removeCompaniesFromSettings(
            Lists.newArrayList(company1, company2));

    long actual =
        userSettings.stream()
            .filter(userSetting -> userSetting.getDefaultCompany() == null)
            .count();

    assertEquals(expected, actual);
  }

  @Test
  public void removeCompaniesFromSettings_shouldRemoveNoCompanies() {
    long expected = 0;

    List<UserSettings> userSettings =
        userSettingsOnCompanyDeleteService.removeCompaniesFromSettings(Lists.newArrayList());

    long actual =
        userSettings.stream()
            .filter(userSetting -> userSetting.getDefaultCompany() == null)
            .count();

    assertEquals(expected, actual);
  }
}
