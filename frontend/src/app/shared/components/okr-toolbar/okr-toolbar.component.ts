import { User } from '../../model/api/user';
import { map, shareReplay, switchMap } from 'rxjs/operators';
import { CurrentOkrviewService } from '../../../okrview/current-okrview.service';
import { CurrentUserService } from '../../../core/services/current-user.service';
import { Router } from '@angular/router';
import { Component, Input, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material';
import { AdminSettingsFormComponent } from '../../../core/settings/admin-settings/admin-settings-form.component';
import { VersionFormComponent } from '../../../core/version-form/version-form.component';
import { ChangePasswordDialogComponent } from '../../../core/auth/local-auth/change-password-dialog/change-password-dialog.component';
import { Observable } from 'rxjs';
import versions from '../../../../../src/_versions';
import { OAuthFrontendDetailsService } from '../../../core/auth/services/o-auth-frontend-details.service';

@Component({
  selector: 'app-okr-toolbar',
  templateUrl: './okr-toolbar.component.html',
  styleUrls: ['./okr-toolbar.component.scss']
})
export class OkrToolbarComponent implements OnInit {
  @Input() isCycleManagementOptionVisible = false;
  versionString: string = versions.version;
  currentUser$: Observable<User>;
  isCurrentUserAdmin$: Observable<boolean>;
  isLocalUserbase$: Observable<boolean>;

  constructor(
    private router: Router,
    private dialog: MatDialog,
    private currentUserService: CurrentUserService,
    private currentOkrViewService: CurrentOkrviewService,
    private oAuthDetails: OAuthFrontendDetailsService
  ) {
    this.isLocalUserbase$ = this.oAuthDetails.getAuthType$()
      .pipe(
        map(authType => authType === 'local'),
        shareReplay()
      );
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
    const companyId: number = this.currentOkrViewService.currentCompany.id;
    this.router.navigate([`cycle-admin/`, companyId]);
  }

  openSettings(): void {
    this.dialog.open(AdminSettingsFormComponent, {disableClose: true})
      .afterClosed()
      .pipe(switchMap(_ => _))
      .subscribe();
  }

  openPasswordChangeForm(): void {
    this.dialog.open(ChangePasswordDialogComponent, {disableClose: true})
      .afterClosed()
      .subscribe();
  }
}
