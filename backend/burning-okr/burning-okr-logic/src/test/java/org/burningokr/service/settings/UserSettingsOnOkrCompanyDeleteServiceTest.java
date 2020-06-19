package org.burningokr.service.settings;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;
import org.assertj.core.util.Lists;
import org.burningokr.model.okrUnits.OkrCompany;
import org.burningokr.model.settings.UserSettings;
import org.burningokr.repositories.settings.UserSettingsRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UserSettingsOnOkrCompanyDeleteServiceTest {

  @Mock UserSettingsRepository userSettingsRepository;

  @InjectMocks UserSettingsOnCompanyDeleteService userSettingsOnCompanyDeleteService;

  private OkrCompany okrCompany1 = new OkrCompany();
  private OkrCompany okrCompany2 = new OkrCompany();

  @Before
  public void setup() {
    okrCompany1.setId(1L);
    okrCompany2.setId(2L);
    UserSettings userSettings1 = new UserSettings();
    UserSettings userSettings2 = new UserSettings();
    userSettings1.setDefaultOkrCompany(okrCompany1);
    userSettings2.setDefaultOkrCompany(okrCompany2);

    UserSettings[] userSettings = {userSettings1, userSettings2};

    when(userSettingsRepository.findAll()).thenReturn(Lists.newArrayList(userSettings));
  }

  @Test
  public void removeCompaniesFromSettings_shouldRemoveOneCompany() {
    long expected = 1;

    List<UserSettings> userSettings =
        userSettingsOnCompanyDeleteService.removeCompaniesFromSettings(
            Lists.newArrayList(okrCompany1));

    long actual =
        userSettings.stream()
            .filter(userSetting -> userSetting.getDefaultOkrCompany() == null)
            .count();

    assertEquals(expected, actual);
  }

  @Test
  public void removeCompaniesFromSettings_shouldRemoveAllCompanies() {
    long expected = 2;

    List<UserSettings> userSettings =
        userSettingsOnCompanyDeleteService.removeCompaniesFromSettings(
            Lists.newArrayList(okrCompany1, okrCompany2));

    long actual =
        userSettings.stream()
            .filter(userSetting -> userSetting.getDefaultOkrCompany() == null)
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
            .filter(userSetting -> userSetting.getDefaultOkrCompany() == null)
            .count();

    assertEquals(expected, actual);
  }
}
