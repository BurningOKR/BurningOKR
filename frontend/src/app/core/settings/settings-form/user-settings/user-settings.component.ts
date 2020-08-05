import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { SettingsForm } from '../settings-form';
import { Observable, of } from 'rxjs';
import { UserSettings } from '../../../../shared/model/ui/user-settings';
import { Configuration } from '../../../../shared/model/ui/configuration';
import { FormControl, FormGroup } from '@angular/forms';
import { CompanyUnit } from '../../../../shared/model/ui/OrganizationalUnit/company-unit';
import { OkrDepartment } from '../../../../shared/model/ui/OrganizationalUnit/okr-department';
import { CompanyMapper } from '../../../../shared/services/mapper/company.mapper';
import { filter, switchMap, take } from 'rxjs/operators';
import { UserSettingsManagerService } from '../../../services/user-settings-manager.service';
import { DepartmentMapper } from '../../../../shared/services/mapper/department.mapper';

@Component({
  selector: 'app-user-settings',
  templateUrl: './user-settings.component.html',
  styleUrls: ['./user-settings.component.css'],
  providers: [{provide: SettingsForm, useExisting: UserSettingsComponent}]
})
export class UserSettingsComponent extends SettingsForm implements OnInit {

  userSettingsForm: FormGroup;
  companies$: Observable<CompanyUnit[]>;
  departments$: Observable<OkrDepartment[]>;

  @Output() valid: EventEmitter<boolean> = new EventEmitter<boolean>();

  constructor(private companyService: CompanyMapper,
              private userSettingsManager: UserSettingsManagerService,
              private departmentService: DepartmentMapper) {
    super();
  }

  ngOnInit(): void {
    this.initUserSettingsForm();
    this.companies$ = this.companyService.getActiveCompanies$();
    this.userSettingsForm.statusChanges.subscribe(() => {
      this.valid.emit(this.userSettingsForm.valid);
    });
  }

  createUpdate$(): Observable<UserSettings | Configuration> {
    return this.userSettingsManager.getUserSettings$()
      .pipe(
        take(1),
        switchMap((userSettings: UserSettings) => {
          userSettings.defaultCompanyId = this.userSettingsForm.get('defaultCompanyId').value;
          userSettings.defaultTeamId = this.userSettingsForm.get('defaultTeamId').value;

          return this.userSettingsManager.updateUserSettings$(userSettings);
        })
      );
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
}
