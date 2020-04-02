import { Component, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material';
import { CurrentUserService } from '../../services/current-user.service';
import { forkJoin, NEVER, Observable, of } from 'rxjs';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ConfigurationManagerService } from '../configuration-manager.service';
import { UserSettings } from '../../../shared/model/ui/user-settings';
import { UserSettingsManagerService } from '../../services/user-settings-manager.service';
import { CompanyMapper } from '../../../shared/services/mapper/company.mapper';
import { DepartmentMapper } from '../../../shared/services/mapper/department.mapper';
import { DepartmentUnit } from '../../../shared/model/ui/OrganizationalUnit/department-unit';
import { CompanyUnit } from '../../../shared/model/ui/OrganizationalUnit/company-unit';
import { Configuration } from '../../../shared/model/ui/configuration';
import { take } from 'rxjs/operators';
import { OAuthFrontendDetailsService } from '../../auth/services/o-auth-frontend-details.service';

@Component({
  selector: 'app-admin-settings',
  templateUrl: './admin-settings-form.component.html',
  styleUrls: ['./admin-settings-form.component.scss']
})
// TODO: rename to AdminSettingsFormComponent or dir
export class AdminSettingsFormComponent implements OnInit {

  adminSettingsForm: FormGroup;
  userSettingsForm: FormGroup;
  companies$: Observable<CompanyUnit[]>;
  departments$: Observable<DepartmentUnit[]>;

  constructor(private dialogRef: MatDialogRef<AdminSettingsFormComponent>,
              private currentUserService: CurrentUserService,
              private configurationManagerService: ConfigurationManagerService,
              private userSettingsManager: UserSettingsManagerService,
              private companyService: CompanyMapper,
              private departmentService: DepartmentMapper,
              private oAuthDetails: OAuthFrontendDetailsService) {
  }

  private _isCurrentUserAdmin$: Observable<boolean>;

  get isCurrentUserAdmin$(): Observable<boolean> {
    return this._isCurrentUserAdmin$;
  }

  ngOnInit(): void {
    this._isCurrentUserAdmin$ = this.currentUserService.isCurrentUserAdmin();
    this.initAdminSettingsForm();
    this.initUserSettingsForm();
    this.companies$ = this.companyService.getActiveCompanies$();
  }

  sendOk(): void {
    const updates$: Observable<UserSettings | Configuration>[] = [];

    this.currentUserService.isCurrentUserAdmin()
      .pipe(take(1))
      .subscribe(isAdmin => {
        if (isAdmin) {
          this.saveAdminSettings(updates$);
        }
        this.saveUserSettings(updates$);

        this.dialogRef.close(forkJoin(updates$));
      });
  }

  saveAdminSettings(updates$: Observable<UserSettings | Configuration>[]): void {
    const maxKeyResults: number = this.adminSettingsForm.get('maxKeyResults').value;
    const objectiveProgressGreenYellowThreshold: number = this.adminSettingsForm.get('objectiveProgressGreenYellowThreshold').value;
    const objectiveProgressYellowRedThreshold: number = this.adminSettingsForm.get('objectiveProgressYellowRedThreshold').value;
    const generalFrontendBaseUrl: string = this.adminSettingsForm.get('generalFrontendBaseUrl').value;
    const emailFrom: string = this.adminSettingsForm.get('emailFrom').value;
    const emailSubjectNewUser: string = this.adminSettingsForm.get('emailSubjectNewUser').value;
    const emailSubjectForgotPassword: string = this.adminSettingsForm.get('emailSubjectForgotPassword').value;

    updates$.push(this.configurationManagerService.updateMaxKeyResults$(maxKeyResults));
    updates$.push(this.configurationManagerService.updateObjectiveProgressGreenYellowThreshold(objectiveProgressGreenYellowThreshold));
    updates$.push(this.configurationManagerService.updateObjectiveProgressYellowRedThreshold(objectiveProgressYellowRedThreshold));
    updates$.push(this.configurationManagerService.updateGeneralFrontendBaseUrl(generalFrontendBaseUrl));
    updates$.push(this.configurationManagerService.updateEmailFrom(emailFrom));
    updates$.push(this.configurationManagerService.updateEmailSubjectNewUser(emailSubjectNewUser));
    updates$.push(this.configurationManagerService.updateEmailSubjectForgotPassword(emailSubjectForgotPassword));
  }

  saveUserSettings(updates$: Observable<UserSettings | Configuration>[]): void {
    const userSettings: UserSettings = this.userSettingsManager.userUserSettings;
    userSettings.defaultCompanyId = this.userSettingsForm.get('defaultCompanyId').value;
    userSettings.defaultTeamId = this.userSettingsForm.get('defaultTeamId').value;
    updates$.push(this.userSettingsManager.updateUserSettings(userSettings));
  }

  closeDialog(): void {
    this.dialogRef.close(NEVER);
  }

  onSelectCompany(): void {
    const companyId: number = this.userSettingsForm.get('defaultCompanyId').value;
    if (companyId !== null) {
      this.departments$ = this.departmentService.getAllDepartmentsForCompanyFlatted$(companyId);
    } else {
      this.userSettingsForm.get('defaultTeamId')
        .setValue(null);
      this.departments$ = of([]);
    }
  }

  private initUserSettingsForm(): void {
    const userSettings: UserSettings = this.userSettingsManager.userUserSettings;
    this.userSettingsForm = new FormGroup({
      defaultCompanyId: new FormControl(userSettings.defaultCompanyId),
      defaultTeamId: new FormControl(userSettings.defaultTeamId)
    });
    this.initDepartmentsForCompany(userSettings.defaultCompanyId);
    this.userSettingsForm.get('defaultCompanyId').valueChanges
      .subscribe(() => {
      const companyId: number = this.userSettingsForm.get('defaultCompanyId').value;
      if (companyId !== null) {
        this.departments$ = this.departmentService.getAllDepartmentsForCompanyFlatted$(companyId);
      }
    });
  }

  private initDepartmentsForCompany(companyId: number): void {
    if (companyId !== null) {
      this.departments$ = this.departmentService.getAllDepartmentsForCompanyFlatted$(companyId);
    } else {
      this.userSettingsForm.get('defaultTeamId')
        .setValue(null);
      this.departments$ = of([]);
    }
  }

  private initAdminSettingsForm(): void {
    const maxKeyResults: number = +this.configurationManagerService.maxKeyResults.value;
    const objectiveProgressGreenYellowThreshold: number =
      +this.configurationManagerService.objectiveProgressGreenYellowThreshold.value;
    const objectiveProgressYellowRedThreshold: number =
      +this.configurationManagerService.objectiveProgressYellowRedThreshold.value;
    const generalFrontendBaseUrl: string = this.configurationManagerService.generalFrontendBaseUrl.value;
    const emailFrom: string = this.configurationManagerService.emailFrom.value;
    const emailSubjectNewUser: string = this.configurationManagerService.emailSubjectNewUser.value;
    const emailSubjectForgotPassword: string = this.configurationManagerService.emailSubjectForgotPassword.value;

    this.adminSettingsForm = new FormGroup({
      maxKeyResults: new FormControl(maxKeyResults, [Validators.required]),
      objectiveProgressGreenYellowThreshold: new FormControl(objectiveProgressGreenYellowThreshold, [Validators.required]),
      objectiveProgressYellowRedThreshold: new FormControl(objectiveProgressYellowRedThreshold, [Validators.required]),
      generalFrontendBaseUrl: new FormControl(generalFrontendBaseUrl, [Validators.required]),
      emailFrom: new FormControl(emailFrom, [Validators.required]),
      emailSubjectNewUser: new FormControl(emailSubjectNewUser, [Validators.required]),
      emailSubjectForgotPassword: new FormControl(emailSubjectForgotPassword, [Validators.required])
    });

    this.oAuthDetails.getAuthType$()
      .pipe(take(1))
      .subscribe(authType => {
        if (authType === 'azure') {
          this.adminSettingsForm.get('generalFrontendBaseUrl')
            .disable();
        }
      });
  }
}
