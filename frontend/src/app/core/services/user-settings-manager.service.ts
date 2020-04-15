import { Injectable } from '@angular/core';
import { UserSettingsMapper } from '../settings/user-settings.mapper';
import { BehaviorSubject, Observable, Subscription } from 'rxjs';
import { UserSettings } from '../../shared/model/ui/user-settings';
import { Fetchable } from '../../shared/decorators/fetchable.decorator';

@Fetchable()
@Injectable({
  providedIn: 'root'
})
export class UserSettingsManagerService implements Fetchable {

  private _userSettings$: BehaviorSubject<UserSettings> = new BehaviorSubject<UserSettings>(null);

  constructor(private userSettingsService: UserSettingsMapper) {
  }

  get userUserSettings$(): Observable<UserSettings> {
    return this._userSettings$.asObservable();
  }

  fetchUserSettings$(): Subscription {
    return this.userSettingsService.getUserSettings$()
      .subscribe((userSettings: UserSettings) => {
        this._userSettings$.next(userSettings);
      });
  }

  fetchData(): void {
    this.fetchUserSettings$();
  }

  updateUserSettings(userSettings: UserSettings): Observable<UserSettings> {
    this._userSettings$.next(userSettings);

    return this.userSettingsService.updateUserSettings$(userSettings);
  }
}
