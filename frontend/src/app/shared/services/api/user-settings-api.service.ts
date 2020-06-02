// istanbul ignore file
import { Injectable } from '@angular/core';
import { ApiHttpService } from '../../../core/services/api-http.service';
import { Observable } from 'rxjs';
import { UserSettingsApi } from '../../model/api/user-settings.api';

@Injectable({
  providedIn: 'root'
})
export class UserSettingsApiService {

  constructor(private api: ApiHttpService) {
  }

  getUserSettings$(): Observable<UserSettingsApi> {
    return this.api.getData$('settings');
  }

  updateUserSettings$(userSettingsApi: UserSettingsApi): Observable<UserSettingsApi> {
    return this.api.putData$('settings', userSettingsApi);
  }
}
