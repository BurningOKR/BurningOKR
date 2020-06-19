import { Injectable } from '@angular/core';
import { UserSettingsMapper } from '../settings/user-settings.mapper';
import { BehaviorSubject, Observable } from 'rxjs';
import { UserSettings } from '../../shared/model/ui/user-settings';
import { Fetchable } from '../../shared/decorators/fetchable.decorator';

@Fetchable()
@Injectable({
  providedIn: 'root'
})
export class UserSettingsManagerService implements Fetchable {

  private userSettings$: BehaviorSubject<UserSettings> = new BehaviorSubject<UserSettings>(null);

  constructor(private userSettingsService: UserSettingsMapper) {
  }

  getUserSettings$(): Observable<UserSettings> {
    return this.userSettings$.asObservable();
  }

  fetchData(): void {
    this.userSettingsService.getUserSettings$()
      .subscribe((userSettings: UserSettings) => {
        this.userSettings$.next(userSettings);
      });
  }

  updateUserSettings$(userSettings: UserSettings): Observable<UserSettings> {
    this.userSettings$.next(userSettings);

    return this.userSettingsService.updateUserSettings$(userSettings);
  }
}
