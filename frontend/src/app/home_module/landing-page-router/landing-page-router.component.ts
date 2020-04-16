import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, of, Subscription } from 'rxjs';
import { CompanyMapper } from '../../shared/services/mapper/company.mapper';
import { HealthcheckApiService } from '../../shared/services/api/healthcheck-api.service';
import { filter, map, switchMap, take } from 'rxjs/operators';
import { UserSettingsManagerService } from '../../core/services/user-settings-manager.service';
import { UserSettings } from '../../shared/model/ui/user-settings';
import { ConfigurationManagerService } from '../../core/settings/configuration-manager.service';
import { CompanyUnit } from '../../shared/model/ui/OrganizationalUnit/company-unit';

@Component({
  selector: 'app-landing-page-router',
  templateUrl: './landing-page-router.component.html',
  styleUrls: ['./landing-page-router.component.scss']
})
export class LandingPageRouterComponent implements OnInit, OnDestroy {
  subscription: Subscription;

  constructor(private companyMapperService: CompanyMapper,
              private userSettingsManagerService: UserSettingsManagerService,
              private configurationManagerService: ConfigurationManagerService,
              private router: Router,
              private healthcheckService: HealthcheckApiService) {
  }

  ngOnInit(): void {
    this.subscription = this.healthcheckService.isAlive$()
      .pipe(
        switchMap(() => this.getRouterLink$()))
      .subscribe((routerLink: (string | number)[]) => {
        this.router.navigate(routerLink);
      }, () => this.router.navigate(['/error']));
  }

  private getRouterLink$(): Observable<(string | number)[]> {
    return this.companyMapperService.getActiveCompanies$()
      .pipe(
        switchMap((uniqueCompanies: CompanyUnit[]) => {
          if (uniqueCompanies.length === 1) {
            return of(['/okr/companies/', uniqueCompanies[0].id]);
          } else {
            return this.userSettingsManagerService.userUserSettings$.pipe(
              filter((userSettings: UserSettings) => !!userSettings),
              map((userSettings: UserSettings) => {
                if (this.isUserHavingNoDefaultTeamButDefaultCompany(userSettings)) {
                  return [`/okr/companies/`, userSettings.defaultCompanyId];
                } else if (this.isUserHavingADefaultTeam(userSettings)) {
                  return [`/okr/departments/`, userSettings.defaultTeamId];
                } else {
                  return ['/companies'];
                }
              }),
              take(1)
            );
          }
        })
      );
  }

  private isUserHavingADefaultTeam(userSettings: UserSettings): boolean {
    return userSettings.defaultTeamId !== null;
  }

  private isUserHavingNoDefaultTeamButDefaultCompany(userSettings: UserSettings): boolean {
    return userSettings.defaultTeamId === null && userSettings.defaultCompanyId !== null;
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }
}
