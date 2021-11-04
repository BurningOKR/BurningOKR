import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { SettingsFormComponent } from './settings-form.component';
import { UserSettingsComponent } from './user-settings/user-settings.component';
import { AdminSettingsFormComponent } from './admin-settings/admin-settings-form.component';
import { MaterialTestingModule } from '../../../testing/material-testing.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { OAuthFrontendDetailsService } from '../../auth/services/o-auth-frontend-details.service';
import { I18n } from '@ngx-translate/i18n-polyfill';
import { RouterTestingModule } from '@angular/router/testing';
import { i18nMock } from '../../../shared/mocks/i18n-mock';
import { CompanyMapper } from '../../../shared/services/mapper/company.mapper';
import { UserSettingsManagerService } from '../../services/user-settings-manager.service';
import { DepartmentMapper } from '../../../shared/services/mapper/department.mapper';
import { ConfigurationManagerService } from '../configuration-manager.service';
import { CurrentUserService } from '../../services/current-user.service';
import { of } from 'rxjs';
import { UserSettings } from '../../../shared/model/ui/user-settings';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

const companyService: any = {
  getActiveCompanies$: jest.fn()
};

const userSettingsManager: any = {
  getUserSettings$: jest.fn()
};

const departmentService: any = {};

const configurationManagerService: any = {
  getAllConfigurations$: jest.fn()
};

const currentUserService: any = {
  isCurrentUserAdmin$: jest.fn()
};

const dialog: any = {};

const oAuthFrontendDetailsService: any = {
  isAzureAuthType$: jest.fn()
};

const dialogRef: any = {};

const userSettings: UserSettings = {
  defaultCompanyId: 0,
  defaultTeamId: 0,
  id: 0,
  userId: ''
};

describe('SettingsFormComponent', () => {
  let component: SettingsFormComponent;
  let fixture: ComponentFixture<SettingsFormComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ SettingsFormComponent, UserSettingsComponent, AdminSettingsFormComponent ],
      imports: [
        MaterialTestingModule,
        FormsModule,
        ReactiveFormsModule,
        RouterTestingModule,
        BrowserAnimationsModule
      ],
      providers: [
        { provide: I18n, useValue: i18nMock },
        { provide: CompanyMapper, useValue: companyService },
        { provide: UserSettingsManagerService, useValue: userSettingsManager },
        { provide: DepartmentMapper, useValue: departmentService },
        { provide: ConfigurationManagerService, useValue: configurationManagerService },
        { provide: CurrentUserService, useValue: currentUserService },
        { provide: MatDialog, useValue: dialog },
        { provide: OAuthFrontendDetailsService, useValue: oAuthFrontendDetailsService },
        { provide: MatDialogRef, useValue: dialogRef }
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    currentUserService.isCurrentUserAdmin$.mockReset();
    currentUserService.isCurrentUserAdmin$.mockReturnValue(of(false));

    userSettingsManager.getUserSettings$.mockReset();
    userSettingsManager.getUserSettings$.mockReturnValue(of(userSettings));

    companyService.getActiveCompanies$.mockReset();
    companyService.getActiveCompanies$.mockReturnValue(of([]));

    configurationManagerService.getAllConfigurations$.mockReset();
    configurationManagerService.getAllConfigurations$.mockReturnValue(of([]));

    oAuthFrontendDetailsService.isAzureAuthType$.mockReset();
    oAuthFrontendDetailsService.isAzureAuthType$.mockReturnValue(of(true));

    fixture = TestBed.createComponent(SettingsFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
