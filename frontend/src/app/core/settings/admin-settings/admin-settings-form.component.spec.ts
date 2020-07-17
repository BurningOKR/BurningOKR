import { AdminSettingsFormComponent } from './admin-settings-form.component';
import { CompanyMapper } from '../../../shared/services/mapper/company.mapper';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ConfigurationManagerService } from '../configuration-manager.service';
import { CurrentUserService } from '../../services/current-user.service';
import { CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA } from '@angular/core';
import { DepartmentMapper } from '../../../shared/services/mapper/department.mapper';
import { I18n } from '@ngx-translate/i18n-polyfill';
import { i18nMock } from '../../../shared/mocks/i18n-mock';
import { MatDialog, MatDialogRef } from '@angular/material';
import { OAuthFrontendDetailsService } from '../../auth/services/o-auth-frontend-details.service';
import { UserSettingsManagerService } from '../../services/user-settings-manager.service';
import { of } from 'rxjs';
import { UserSettings } from '../../../shared/model/ui/user-settings';
import { CompanyUnit } from '../../../shared/model/ui/OrganizationalUnit/company-unit';

const configurationManagerServiceStub: any = {
  getAllConfigurations$: jest.fn()
};
const currentUserServiceStub: any = {
  isCurrentUserAdmin$: jest.fn(),
};
const oAuthFrontendDetailsServiceStub: any = {
  getAuthType$: jest.fn(),
  isAzureAuthType$: jest.fn()
};
const userSettingsManagerServiceStub: any = {
  getUserSettings$: jest.fn(),
};
const companyServiceStub: any = {
  getActiveCompanies$: jest.fn()
};
const dialog: any = {};

describe('AdminSettingsForm', () => {

  let fixture: ComponentFixture<AdminSettingsFormComponent>;
  let component: AdminSettingsFormComponent;

  beforeEach(() => {
    configurationManagerServiceStub.getAllConfigurations$.mockReset();
    currentUserServiceStub.isCurrentUserAdmin$.mockReset();
    userSettingsManagerServiceStub.getUserSettings$.mockReset();
    oAuthFrontendDetailsServiceStub.isAzureAuthType$.mockReset();
    oAuthFrontendDetailsServiceStub.getAuthType$.mockReset();
    companyServiceStub.getActiveCompanies$.mockReset();

    configurationManagerServiceStub.getAllConfigurations$.mockReturnValue(
      of([
        {
          id: 0,
          name: 'config_name',
          value: 'config_value',
          type: 'text'
        }
      ]));
    currentUserServiceStub.isCurrentUserAdmin$.mockReturnValue(of(true));
    userSettingsManagerServiceStub.getUserSettings$.mockReturnValue(
      of(new UserSettings(0, '', undefined, undefined))
    );
    oAuthFrontendDetailsServiceStub.isAzureAuthType$.mockReturnValue(of('false'));
    oAuthFrontendDetailsServiceStub.getAuthType$.mockReturnValue(of('local'));
    companyServiceStub.getActiveCompanies$.mockReturnValue(
      of([new CompanyUnit(0, '', [], [], 0, '')])
    );

    TestBed.configureTestingModule(
      {
        declarations: [AdminSettingsFormComponent],
        providers: [
          {provide: CompanyMapper, useValue: companyServiceStub},
          {provide: ConfigurationManagerService, useValue: configurationManagerServiceStub},
          {provide: CurrentUserService, useValue: currentUserServiceStub},
          {provide: DepartmentMapper, useValue: {}},
          {provide: I18n, useValue: i18nMock},
          {provide: MatDialogRef, useValue: {}},
          {provide: OAuthFrontendDetailsService, useValue: oAuthFrontendDetailsServiceStub},
          {provide: UserSettingsManagerService, useValue: userSettingsManagerServiceStub},
          {provide: MatDialog, useValue: dialog},
        ],
        schemas: [CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA],
      })
      .compileComponents();
    fixture = TestBed.createComponent(AdminSettingsFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  afterEach(() => {
    fixture.destroy();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
