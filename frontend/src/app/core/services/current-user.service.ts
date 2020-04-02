import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { UserApiService } from '../../shared/services/api/user-api.service';
import { ConfigurationManagerService } from '../settings/configuration-manager.service';
import { UserSettingsManagerService } from './user-settings-manager.service';
import { UserSettings } from '../../shared/model/ui/user-settings';
import { Router } from '@angular/router';
import { shareReplay, tap } from 'rxjs/operators';
import { OAuthService } from 'angular-oauth2-oidc';
import { User } from '../../shared/model/api/user';

@Injectable({
  providedIn: 'root'
})
export class CurrentUserService {
  private isAdmin$: Observable<boolean>;

  constructor(private oAuthService: OAuthService,
              private userApiService: UserApiService,
              private configurationManagerService: ConfigurationManagerService,
              private userSettingsManagerService: UserSettingsManagerService,
              private router: Router) {
    this.initUserAdmin();
  }

  thereIsACurrentUser(): boolean {
    return this.oAuthService.hasValidAccessToken();
  }

  getCurrentUser(): Observable<User> {
    return this.userApiService.getCurrentUser();
  }

  redirect(userSettings: UserSettings): void {
    // has to be null checks, else business logic will fail
    if (this.isRedirectableSite()) {
      if (this.userHasNoDefaultTeamButDefaultCompany(userSettings)) {
        this.router.navigate([`/okr/companies/`, userSettings.defaultCompanyId])
          .catch();
      } else if (this.userHasADefaultTeam(userSettings)) {
        this.router.navigate([`/okr/departments/`, userSettings.defaultTeamId])
          .catch();
      }
    }
  }

  isCurrentUserAdmin(): Observable<boolean> {
    return this.isAdmin$;
  }

  private initUserAdmin(): void {
    this.isAdmin$ = this.userApiService.isCurrentUserAdmin$()
      .pipe(
        tap(() => {
          this.configurationManagerService.fetchConfigurations();
          this.userSettingsManagerService.fetchUserSettings$()
            .add(() => {
              this.redirect(this.userSettingsManagerService.userUserSettings);
            });
        }),
        shareReplay(1),
      );
  }

  private userHasADefaultTeam(userSettings: UserSettings): boolean {
    return userSettings.defaultTeamId !== null;
  }

  private userHasNoDefaultTeamButDefaultCompany(userSettings: UserSettings): boolean {
    return userSettings.defaultTeamId === null && userSettings.defaultCompanyId !== null;
  }

  private isRedirectableSite(): boolean {
    return this.router.url === '/'
      || this.router.url === '/landingpage'
      || this.router.url.includes('companies/')
      || this.router.url.includes('/companies');
  }
}
