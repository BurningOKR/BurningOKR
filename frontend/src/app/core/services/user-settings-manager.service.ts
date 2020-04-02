import { Injectable } from '@angular/core';
import { UserSettingsMapper } from '../settings/user-settings.mapper';
import { BehaviorSubject, Observable, Subscription } from 'rxjs';
import { UserSettings } from '../../shared/model/ui/user-settings';

@Injectable({
  providedIn: 'root'
})
export class UserSettingsManagerService {

  private _userSettings$: BehaviorSubject<UserSettings>;

  constructor(private userSettingsService: UserSettingsMapper) {
    this.fetchUserSettings$();
  }

  get userUserSettings(): UserSettings {
    return this._userSettings$.getValue();
  }

  fetchUserSettings$(): Subscription {
    return this.userSettingsService.getUserSettings$()
      .subscribe((userSettings: UserSettings) => {
        this._userSettings$ = new BehaviorSubject(userSettings);
      });
  }

  updateUserSettings(userSettings: UserSettings): Observable<UserSettings> {
    this._userSettings$.next(userSettings);

    return this.userSettingsService.updateUserSettings$(userSettings);
  }
}
