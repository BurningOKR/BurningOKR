import { Injectable } from '@angular/core';
import { UserSettingsApi } from '../../shared/model/api/user-settings.api';
import { UserSettingsApiService } from '../../shared/services/api/user-settings-api.service';
import { Observable } from 'rxjs';
import { UserSettings } from '../../shared/model/ui/user-settings';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class UserSettingsMapper {

  constructor(private userSettingsApiService: UserSettingsApiService) {
  }

  getUserSettings$(): Observable<UserSettings> {
    return this.userSettingsApiService.getUserSettings$()
      .pipe(map(this.mapToUserSettings));
  }

  updateUserSettings$(userSettings: UserSettings): Observable<UserSettings> {
    return this.userSettingsApiService.updateUserSettings$(
      this.mapToUserSettingsApi(userSettings))
      .pipe(map(this.mapToUserSettings));
  }

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
