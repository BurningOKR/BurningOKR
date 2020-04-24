import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, of } from 'rxjs';
import { CompanyMapper } from '../../shared/services/mapper/company.mapper';
import { HealthcheckApiService } from '../../shared/services/api/healthcheck-api.service';
import { filter, map, switchMap, take } from 'rxjs/operators';
import { UserSettingsManagerService } from '../services/user-settings-manager.service';
import { UserSettings } from '../../shared/model/ui/user-settings';
import { ConfigurationManagerService } from '../settings/configuration-manager.service';
import { CompanyUnit } from '../../shared/model/ui/OrganizationalUnit/company-unit';
import { RouterParams } from '../../../typings';

@Component({
  selector: 'app-landing-page-navigation',
  templateUrl: './landing-page-navigation.component.html',
  styleUrls: ['./landing-page-navigation.component.scss']
})
export class LandingPageNavigationComponent implements OnInit {
  constructor(private companyMapperService: CompanyMapper,
              private userSettingsManagerService: UserSettingsManagerService,
              private configurationManagerService: ConfigurationManagerService,
              private router: Router,
              private healthcheckService: HealthcheckApiService) {
  }

  static userHasDefaultTeam(userSettings: UserSettings): boolean {
    return userSettings.defaultTeamId !== null;
  }

  static userHasNoDefaultTeamButADefaultCompany(userSettings: UserSettings): boolean {
    return userSettings.defaultTeamId === null && userSettings.defaultCompanyId !== null;
  }

  ngOnInit(): void {
    this.navigateToUsersDefaultTeamAfterCompletionOf(this.performHealthCheck$());
  }

  private navigateToUsersDefaultTeamAfterCompletionOf(healthCheck$: Observable<boolean>): void {
      healthCheck$
        .pipe(
      switchMap(() => this.getRouterLink$()))
        .subscribe((routerLink: RouterParams) => {
        this.router.navigate(routerLink);
      }, () => this.router.navigate(['/error']));
  }

  private getRouterLink$(): Observable<RouterParams> {
    return this.companyMapperService.getActiveCompanies$()
      .pipe(
        switchMap((uniqueCompanies: CompanyUnit[]) => {
          if (uniqueCompanies.length === 1) {
            return of(['/okr/companies/', uniqueCompanies[0].id]);
          } else {
            return this.getDefaultCompanyAndTeam$();
          }
        })
      )
      .pipe(
        take(1)
      );
  }

  private performHealthCheck$(): Observable<boolean> {
    return this.healthcheckService.isAlive$()
      .pipe(take(1));
  }

  private getDefaultCompanyAndTeam$(): Observable<RouterParams> {
    return this.userSettingsManagerService.getUserSettings$()
      .pipe(
        filter((userSettings: UserSettings) => !!userSettings),
        map((userSettings: UserSettings) => {
          if (LandingPageNavigationComponent.userHasNoDefaultTeamButADefaultCompany(userSettings)) {
            return [`/okr/companies/`, userSettings.defaultCompanyId];
          } else if (LandingPageNavigationComponent.userHasDefaultTeam(userSettings)) {
            return [`/okr/departments/`, userSettings.defaultTeamId];
          } else {
            return ['/companies'];
          }
        }),
        take(1));
  }
}
