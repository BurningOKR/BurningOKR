import { User } from '../../model/api/user';
import { map, switchMap, take } from 'rxjs/operators';
import { CurrentUserService } from '../../../core/services/current-user.service';
import { Router } from '@angular/router';
import { Component, Input, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { VersionFormComponent } from '../../../core/version-form/version-form.component';
import { ChangePasswordDialogComponent } from '../../../core/auth/local-auth/change-password-dialog/change-password-dialog.component';
import { Observable, of } from 'rxjs';
import versions from '../../../../../src/_versions';
import { CurrentCompanyService } from '../../../okrview/current-company.service';
import { CompanyUnit } from '../../model/ui/OrganizationalUnit/company-unit';
import { ConfigurationService } from '../../../core/settings/configuration.service';
import { OkrUnitService } from '../../services/mapper/okr-unit.service';
import { SettingsFormComponent } from '../../../core/settings/settings-form/settings-form.component';
import { environment } from '../../../../environments/environment';
import { PickLanguageComponent } from '../../../core/settings/pick-language/pick-language.component';

@Component({
  selector: 'app-okr-toolbar',
  templateUrl: './okr-toolbar.component.html',
  styleUrls: ['./okr-toolbar.component.scss'],
})
export class OkrToolbarComponent implements OnInit {
  @Input() isCycleManagementOptionVisible = false;
  versionString: string = versions.version;
  currentUser$: Observable<User>;
  isCurrentUserAdmin$: Observable<boolean>;
  isLocalUserbase$: Observable<boolean>;
  hasMailConfigured$: Observable<boolean>;
  isPlayground: boolean = environment.playground;

  constructor(
    private router: Router,
    private dialog: MatDialog,
    private currentUserService: CurrentUserService,
    private currentCompanyService: CurrentCompanyService,
    // private oAuthDetails: OAuthFrontendDetailsService,
    private configurationService: ConfigurationService,
    private okrUnitService: OkrUnitService,
  ) {
    this.isLocalUserbase$ = of(false);
    // this.isLocalUserbase$ = this.oAuthDetails.isLocalAuthType$() TODO fix auth
    //   .pipe(take(1));

    this.hasMailConfigured$ = this.configurationService.getHasMailConfigured$();
  }

  ngOnInit(): void {
    this.currentUser$ = this.currentUserService.getCurrentUser$();
    this.isCurrentUserAdmin$ = this.currentUserService.isCurrentUserAdmin$();
  }

  openVersionChangelog(): void {
    this.dialog.open(VersionFormComponent)
      .afterClosed()
      .pipe(switchMap(v => v))
      .subscribe(_ => _);
  }

  routeToCycleAdminPanel(): void {
    this.currentCompanyService.getCurrentCompany$()
      .pipe(take(1))
      .subscribe((currentCompany: CompanyUnit) => {
        this.router.navigate(['cycle-admin/', currentCompany.id]);
      });
  }

  openSettings(): void {
    this.dialog.open(SettingsFormComponent, { disableClose: true })
      .afterClosed()
      .pipe(switchMap(_ => _))
      .subscribe(() => this.okrUnitService.refreshOkrChildUnit());
  }

  openPasswordChangeForm(): void {
    this.dialog.open(ChangePasswordDialogComponent, { disableClose: true })
      .afterClosed()
      .subscribe();
  }

  pickLanguageClicked() {
    this.dialog.open(PickLanguageComponent)
      .afterClosed()
      .subscribe();
  }

  getCurrentCompanyId$(): Observable<number> {
    return this.currentCompanyService.getCurrentCompany$().pipe(map(company => company.id));
  }
}
