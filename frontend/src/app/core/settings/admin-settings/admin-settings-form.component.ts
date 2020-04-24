import { Component, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material';
import { CurrentUserService } from '../../services/current-user.service';
import { forkJoin, NEVER, Observable, of } from 'rxjs';
import { FormArray, FormControl, FormGroup, Validators } from '@angular/forms';
import { ConfigurationManagerService } from '../configuration-manager.service';
import { UserSettings } from '../../../shared/model/ui/user-settings';
import { UserSettingsManagerService } from '../../services/user-settings-manager.service';
import { CompanyMapper } from '../../../shared/services/mapper/company.mapper';
import { DepartmentMapper } from '../../../shared/services/mapper/department.mapper';
import { DepartmentUnit } from '../../../shared/model/ui/OrganizationalUnit/department-unit';
import { CompanyUnit } from '../../../shared/model/ui/OrganizationalUnit/company-unit';
import { Configuration } from '../../../shared/model/ui/configuration';
import { filter, map, switchMap, take } from 'rxjs/operators';
import { OAuthFrontendDetailsService } from '../../auth/services/o-auth-frontend-details.service';
import { I18n } from '@ngx-translate/i18n-polyfill';

@Component({
  selector: 'app-admin-settings',
  templateUrl: './admin-settings-form.component.html',
  styleUrls: ['./admin-settings-form.component.scss']
})
export class AdminSettingsFormComponent implements OnInit {

  adminSettingsForm: FormGroup;
  userSettingsForm: FormGroup;
  companies$: Observable<CompanyUnit[]>;
  departments$: Observable<DepartmentUnit[]>;
  authType$: Observable<string>;

  private configurationNames: { [key: string]: string } = {
    'max-key-results': this.i18n({
      id: '@@settingsFormMaxKeyResultsPerObjective',
      description: 'Placeholder for maximal amount of keyresults per objective',
      value: 'Maximale Anzahl von Key Results'
    }),
    'objective-progress-green-yellow-threshold': this.i18n({
      id: '@@settingsFormObjectiveBarTresholdGreenYellow',
      description: 'Treshold for objective progress bar (Green/Yellow)',
      value: 'Objective Prognose Schwellenwert (Grün/Gelb)'
    }),
    'objective-progress-yellow-red-threshold': this.i18n({
      id: '@@settingsFormObjectiveBarTresholdYellowRed',
      description: 'Treshold for objective progress bar (Yellow/Red)',
      value: 'Objective Prognose Schwellenwert (Gelb/Rot)'
    }),
    'general_frontend-base-url': this.i18n({
      id: '@@settingsFormGeneralFrontendBaseUrl',
      description: 'the domain of this angular application',
      value: 'Frontend-Baseurl'
    }),
    email_from: this.i18n({
      id: '@@settingsFormGeneralFrontendBaseUrl',
      description: 'The address, from which emails are sent',
      value: 'Email Adresse des OKR Tools'
    }),
    'email_subject_new-user': this.i18n({
      id: '@@settingsFormGeneralFrontendBaseUrl',
      description: 'the subject of the email, which is sent to new users',
      value: 'Email Betreff für neue Benutzer'
    }),
    'email_subject_forgot-password': this.i18n({
      id: '@@settingsFormGeneralFrontendBaseUrl',
      description: 'the subject of the email, which is sent to users who forgot their password',
      value: 'Email Betreff für die Passwort-Zurücksetzen Email'
    }),
    email_subject_feedback: this.i18n({
      id: '@@settingsFormEmailSubjectFeedback',
      description: 'the subject of the feedback email',
      value: 'Email Betreff für Feedback'
    }),
    feedback_receivers: this.i18n({
      id: '@@settingsFormFeedbackReceivers',
      description: 'the email adresses of the people who receive feedback',
      value: 'Email Adressen der Feedback Empfänger. (Durch Komma getrennt)'
    })
  };

  constructor(private dialogRef: MatDialogRef<AdminSettingsFormComponent>,
              private currentUserService: CurrentUserService,
              private configurationManagerService: ConfigurationManagerService,
              private userSettingsManager: UserSettingsManagerService,
              private companyService: CompanyMapper,
              private departmentService: DepartmentMapper,
              private oAuthDetails: OAuthFrontendDetailsService,
              private i18n: I18n) {
  }

  private _isCurrentUserAdmin$: Observable<boolean>;

  get isCurrentUserAdmin$(): Observable<boolean> {
    return this._isCurrentUserAdmin$;
  }

  ngOnInit(): void {
    this._isCurrentUserAdmin$ = this.currentUserService.isCurrentUserAdmin$();
    this.initAdminSettingsForm();
    this.initUserSettingsForm();
    this.companies$ = this.companyService.getActiveCompanies$();
  }

  sendOk(): void {
    const updates$: Observable<UserSettings | Configuration>[] = [];

    this.currentUserService.isCurrentUserAdmin$()
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
    const configurations: Configuration[] = this.adminSettingsForm.getRawValue().settings;
    updates$.push(this.configurationManagerService.updateConfigurations$(configurations));
  }

  saveUserSettings(updates$: Observable<UserSettings | Configuration>[]): void {
    updates$.push(this.userSettingsManager.getUserSettings$()
      .pipe(
        take(1),
        switchMap((userSettings: UserSettings) => {
          userSettings.defaultCompanyId = this.userSettingsForm.get('defaultCompanyId').value;
          userSettings.defaultTeamId = this.userSettingsForm.get('defaultTeamId').value;

          return this.userSettingsManager.updateUserSettings$(userSettings);
        })
      )
    );
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
    this.userSettingsManager.getUserSettings$()
      .pipe(filter(value => !!value), take(1))
      .subscribe((userSettings: UserSettings) => {
        this.userSettingsForm = new FormGroup({
          defaultCompanyId: new FormControl(userSettings.defaultCompanyId),
          defaultTeamId: new FormControl(userSettings.defaultTeamId)
        });
        this.initDepartmentsForCompany(userSettings.defaultCompanyId);
      });

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
    this.configurationManagerService.getAllConfigurations$()
      .pipe(
        map((configurations: Configuration[]) => {
          return configurations
            .map((configuration: Configuration) => {
              return new FormGroup({
                id: new FormControl(configuration.id),
                name: new FormControl(configuration.name),
                value: new FormControl(configuration.value, [Validators.required]),
                type: new FormControl(configuration.type)
              });
            })
            .sort((a: FormGroup, b: FormGroup) => +a.controls.id.value - +b.controls.id.value);
        })
      )
      .subscribe((formGroups: FormGroup[]) => {
        this.adminSettingsForm = new FormGroup({
          settings: new FormArray(formGroups)
        });
      });

    this.authType$ = this.oAuthDetails.getAuthType$()
      .pipe(take(1));
  }

  get settings(): FormArray {
    return this.adminSettingsForm.get('settings') as FormArray;
  }
}
