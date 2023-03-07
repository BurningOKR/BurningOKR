import { Injectable } from '@angular/core';
import { UserSettingsMapper } from '../settings/user-settings.mapper';
import { BehaviorSubject, Observable } from 'rxjs';
import { UserSettings } from '../../shared/model/ui/user-settings';
import { Fetchable } from '../../shared/decorators/fetchable.decorator';
import { UserSettingsApiService } from '../../shared/services/api/user-settings-api.service';
import { map, take } from 'rxjs/operators';

@Fetchable()
@Injectable({
  providedIn: 'root',
})
export class UserSettingsService implements Fetchable {

  private userSettings$: BehaviorSubject<UserSettings> = new BehaviorSubject<UserSettings>(null);

  constructor(private userSettingsMapper: UserSettingsMapper, private userSettingsApiService: UserSettingsApiService) {
  }

  getUserSettings$(): Observable<UserSettings> {
    return this.userSettings$.asObservable();
  }

  fetchData(): void {
    this.userSettingsApiService.getUserSettings$()
      .pipe(map(this.userSettingsMapper.mapToUserSettings))
      .subscribe((userSettings: UserSettings) => {
        this.userSettings$.next(userSettings);
      });
  }

  updateUserSettings$(userSettings: UserSettings): Observable<UserSettings> {
    const returnedUserSettings$: Observable<UserSettings> = this.userSettingsApiService.updateUserSettings$(
      this.userSettingsMapper.mapToUserSettingsApi(userSettings))
      .pipe(map(this.userSettingsMapper.mapToUserSettings));

    returnedUserSettings$
      .pipe(take(1))
      .subscribe(uSettings => this.userSettings$.next(uSettings));

    return returnedUserSettings$;
  }
}
