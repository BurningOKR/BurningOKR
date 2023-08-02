import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {UserSettingsComponent} from './user-settings.component';
import {MaterialTestingModule} from '../../../../testing/material-testing.module';
import {AbstractControl, FormsModule, ReactiveFormsModule} from '@angular/forms';
import {CompanyMapper} from '../../../../shared/services/mapper/company.mapper';
import {UserSettingsService} from '../../../services/user-settings.service';
import {DepartmentMapper} from '../../../../shared/services/mapper/department.mapper';
import {of} from 'rxjs';
import {UserSettings} from '../../../../shared/model/ui/user-settings';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {OkrDepartment} from '../../../../shared/model/ui/OrganizationalUnit/okr-department';
import {UnitType} from '../../../../shared/model/api/OkrUnit/unit-type.enum';

const companyService: any = {
  getActiveCompanies$: jest.fn(),
};

const userSettingsService: any = {
  getUserSettings$: jest.fn(),
};

const departmentService: any = {
  getAllDepartmentsForCompanyFlatted$: jest.fn(),
};

let userSettings: UserSettings;

const okrDepartment: OkrDepartment = {
  id: 2,
  isActive: true,
  isParentUnitABranch: false,
  label: 'department',
  name: 'test',
  photo: 'base64',
  objectives: [],
  okrMasterId: '',
  okrMemberIds: [],
  okrTopicSponsorId: '',
  parentUnitId: 1,
  type: UnitType.DEPARTMENT,
};

describe('UserSettingsComponent', () => {
  let component: UserSettingsComponent;
  let fixture: ComponentFixture<UserSettingsComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [UserSettingsComponent],
      imports: [MaterialTestingModule, FormsModule, ReactiveFormsModule, BrowserAnimationsModule],
      providers: [
        { provide: CompanyMapper, useValue: companyService },
        { provide: UserSettingsService, useValue: userSettingsService },
        { provide: DepartmentMapper, useValue: departmentService },
      ],
    })
      .compileComponents();
  }));

  beforeEach(() => {
    userSettings = {
      defaultCompanyId: 4,
      defaultTeamId: 5,
      id: 1,
      userId: '',
    };

    userSettingsService.getUserSettings$.mockReset();
    userSettingsService.getUserSettings$.mockReturnValue(of(userSettings));
    companyService.getActiveCompanies$.mockReset();
    companyService.getActiveCompanies$.mockReturnValue(of(null));
    departmentService.getAllDepartmentsForCompanyFlatted$.mockReset();
    departmentService.getAllDepartmentsForCompanyFlatted$.mockReturnValue(of([okrDepartment]));

    fixture = TestBed.createComponent(UserSettingsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });

  it('should create userSettingsForm on init', done => {
    setTimeout(() => {
      expect(component.userSettingsForm)
        .toBeTruthy();
      done();
    }, 1000); // wait a second for the getUserSettings$ Observable to emit.
  });

  it('userSettingsForm should have right formGroup', done => {
    setTimeout(() => {
      const formGroup: AbstractControl = component.userSettingsForm;

      expect(formGroup.get('defaultCompanyId').value)
        .toBe(userSettings.defaultCompanyId);
      expect(formGroup.get('defaultTeamId').value)
        .toBe(userSettings.defaultTeamId);
      done();
    }, 1000); // wait a second for the getUserSettings$ Observable to emit.
  });

  it('should get all departments for selected company', done => {
    setTimeout(() => {
      expect(departmentService.getAllDepartmentsForCompanyFlatted$)
        .toHaveBeenCalledWith(userSettings.defaultCompanyId);
      done();
    }, 1000); // wait a second for the getUserSettings$ Observable to emit.
  });

  it('should not get any departments, when no company is selected', done => {
    userSettings.defaultCompanyId = null;
    departmentService.getAllDepartmentsForCompanyFlatted$.mockReset();

    fixture = TestBed.createComponent(UserSettingsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    setTimeout(() => {
      expect(departmentService.getAllDepartmentsForCompanyFlatted$)
        .toHaveBeenCalledTimes(0);
      done();
    }, 1000); // wait a second for the getUserSettings$ Observable to emit.
  });

  it('onSelectCompany should get all departments for selected company', () => {
    component.onSelectCompany();

    expect(departmentService.getAllDepartmentsForCompanyFlatted$)
      .toHaveBeenCalledWith(component.userSettingsForm.get('defaultCompanyId').value);
    expect(departmentService.getAllDepartmentsForCompanyFlatted$)
      .toHaveBeenCalledTimes(2); // once on init and once onSelectCompany.
  });

  it('onSelectCompany should not get all departments when company is null', () => {
    component.userSettingsForm.get('defaultCompanyId')
      .setValue(null);

    component.onSelectCompany();

    expect(departmentService.getAllDepartmentsForCompanyFlatted$)
      .toHaveBeenCalledTimes(1); // only once on init, but not when onSelectCompany was called.
  });
});
