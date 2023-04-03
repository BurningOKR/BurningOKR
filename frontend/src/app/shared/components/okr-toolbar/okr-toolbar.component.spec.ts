import { TestBed } from '@angular/core/testing';

import { OkrToolbarComponent } from './okr-toolbar.component';
import { CurrentCompanyService } from '../../../okrview/current-company.service';
import { CompanyUnit } from '../../model/ui/OrganizationalUnit/company-unit';
import { MaterialTestingModule } from '../../../testing/material-testing.module';
import { MatDialog } from '@angular/material/dialog';
import { CurrentUserService } from '../../../core/services/current-user.service';
import { ConfigurationService } from '../../../core/settings/configuration.service';
import { OkrUnitService } from '../../services/mapper/okr-unit.service';
import { of } from 'rxjs';
import { Router } from '@angular/router';
import { CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA } from '@angular/core';

describe('OkrToolbarComponent', () => {
  let component: any;
  let fixture: any;

  const currentCompanyMock: CompanyUnit = {
    id: 1,
    name: '',
    okrChildUnitIds: null,
    objectives: null,
    cycleId: 0,
    label: '',
  };
  const currentCompanyService: any = {
    getCurrentCompany$: jest.fn(),
  };
  const router: any = {
    navigate: jest.fn(),
  };
  const dialog: any = {
    open: jest.fn(),
  };
  const currentUserService: any = {
    getCurrentUser$: jest.fn(),
    isCurrentUserAdmin$: jest.fn(),
  };
  const oAuthDetails: any = {
    isLocalAuthType$: jest.fn(),
  };
  const configurationService: any = {
    getHasMailConfigured$: jest.fn(),
  };
  const okrUnitService: any = {
    refreshOkrChildUnit: jest.fn(),
  };

  beforeEach((() => {
    TestBed.configureTestingModule({
      declarations: [OkrToolbarComponent],
      schemas: [CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA],
      imports: [MaterialTestingModule],
      providers: [
        { provide: CurrentCompanyService, useValue: currentCompanyService },
        { provide: MatDialog, useValue: dialog },
        { provide: CurrentUserService, useValue: currentUserService },
        { provide: ConfigurationService, useValue: configurationService },
        { provide: OkrUnitService, useValue: okrUnitService },
        { provide: Router, useValue: router },
      ],
    })
      .compileComponents();
  }));

  beforeEach(() => {

    currentCompanyService.getCurrentCompany$.mockReset();
    oAuthDetails.isLocalAuthType$.mockReset();
    dialog.open.mockReset();

    currentCompanyService.getCurrentCompany$.mockReturnValue(of(currentCompanyMock));
    oAuthDetails.isLocalAuthType$.mockReturnValue(of(false));
    dialog.open.mockReturnValue({ afterClosed: () => of(of(true)) });

    fixture = TestBed.createComponent(OkrToolbarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });

  it('should route to admin cycle panel', () => {
    component.routeToCycleAdminPanel();

    expect(router.navigate)
      .toHaveBeenCalledWith(['cycle-admin/', currentCompanyMock.id]);
  });

  it('should open settings and refresh after closing', () => {
    component.openSettings();

    expect(okrUnitService.refreshOkrChildUnit)
      .toHaveBeenCalled();
  });
});
