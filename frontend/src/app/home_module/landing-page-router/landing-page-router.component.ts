import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { CompanyMapper } from '../../shared/services/mapper/company.mapper';
import { HealthcheckApiService } from '../../shared/services/api/healthcheck-api.service';
import { switchMap } from 'rxjs/operators';
import { UserSettingsManagerService } from '../../core/services/user-settings-manager.service';
import { UserSettings } from '../../shared/model/ui/user-settings';
import { ConfigurationManagerService } from '../../core/settings/configuration-manager.service';

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
    this.configurationManagerService.fetchData();
    this.userSettingsManagerService.fetchUserSettings$()
      .add(() => {
        this.redirect();
      });
  }

  private redirect(): void {
    this.subscription = this.healthcheckService.isAlive$()
      .pipe(
        switchMap(() => this.companyMapperService.getActiveCompanies$()))
      .subscribe(uniqueCompanies => {
        if (uniqueCompanies.length === 1) {
          this.router.navigate(['/okr/companies/', uniqueCompanies[0].id])
            .catch();
        } else {
          const userSettings: UserSettings = this.userSettingsManagerService.userUserSettings;
          if (this.isUserHavingNoDefaultTeamButDefaultCompany(userSettings)) {
            this.router.navigate([`/okr/companies/`, userSettings.defaultCompanyId])
              .catch();
          } else if (this.isUserHavingADefaultTeam(userSettings)) {
            this.router.navigate([`/okr/departments/`, userSettings.defaultTeamId])
              .catch();
          } else {
            this.router.navigate(['/companies'])
              .catch();
          }
        }
      }, () => this.router.navigate(['/error']));
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
