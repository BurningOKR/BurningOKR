import { Injectable } from '@angular/core';
import { UserSettingsApi } from '../../shared/model/api/user-settings.api';
import { UserSettings } from '../../shared/model/ui/user-settings';

@Injectable({
  providedIn: 'root',
})
export class UserSettingsMapper {

  mapToUserSettings(userSettingsDto: UserSettingsApi): UserSettings {
    return new UserSettings(userSettingsDto.id, userSettingsDto.userId,
      userSettingsDto.defaultCompanyId, userSettingsDto.defaultTeamId,
    );
  }

  mapToUserSettingsApi(userSettings: UserSettings): UserSettingsApi {
    return {
      id: userSettings.id,
      userId: userSettings.userId,
      defaultCompanyId: userSettings.defaultCompanyId,
      defaultTeamId: userSettings.defaultTeamId,
    };
  }
}
