import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { DashboardModificationComponent } from './dashboard-modification.component';
import { DepartmentMapper } from '../../../shared/services/mapper/department.mapper';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { MatDialog } from '@angular/material/dialog';
import { TranslateModule } from '@ngx-translate/core';
import { CUSTOM_ELEMENTS_SCHEMA, DebugElement, NO_ERRORS_SCHEMA } from '@angular/core';
import { of } from 'rxjs';
import { OkrDepartment } from '../../../shared/model/ui/OrganizationalUnit/okr-department';
import { Dashboard } from '../../model/ui/dashboard';
import { UnitType } from '../../../shared/model/api/OkrUnit/unit-type.enum';
import { ReactiveFormsModule } from '@angular/forms';
import { By } from '@angular/platform-browser';
import { PieChartOptions } from '../../model/ui/pie-chart-options';

const okrTestDepartment1: OkrDepartment = {
  id: 1,
  isActive: true,
  isParentUnitABranch: false,
  label: 'department',
  name: 'test1',
  photo: 'base64',
  objectives: [],
  okrMasterId: '',
  okrMemberIds: [],
  okrTopicSponsorId: '',
  parentUnitId: 1,
  type: UnitType.DEPARTMENT,
};

const okrTestDepartment2: OkrDepartment = {
  id: 2,
  isActive: true,
  isParentUnitABranch: false,
  label: 'department',
  name: 'test2',
  photo: 'base64',
  objectives: [],
  okrMasterId: '',
  okrMemberIds: [],
  okrTopicSponsorId: '',
  parentUnitId: 1,
  type: UnitType.DEPARTMENT,
};

const testDashboard: Dashboard = {
  id: 1000,
  title: 'DashboardMock',
  companyId: 1,
  creatorId: '1',
  charts: [],
  creationDate: new Date(),
};

const departmentService: any = {
  getAllDepartmentsForCompanyFlatted$: jest.fn(),
};

describe('DashboardModificationComponent', () => {
  let component: DashboardModificationComponent;
  let fixture: ComponentFixture<DashboardModificationComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        TranslateModule.forRoot(),
        ReactiveFormsModule,
      ],
      declarations: [DashboardModificationComponent],
      schemas: [CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA],
      providers: [
        { provide: DepartmentMapper, useValue: departmentService },
        { provide: MatDialog, useValue: {} },
      ],
    })
      .compileComponents();

    departmentService.getAllDepartmentsForCompanyFlatted$.mockReset();
    departmentService.getAllDepartmentsForCompanyFlatted$.mockReturnValue(of([okrTestDepartment1, okrTestDepartment2]));

    testDashboard.charts.push(new PieChartOptions());
    testDashboard.charts.forEach(chart => chart.title.text = 'Chart Title');

    fixture = TestBed.createComponent(DashboardModificationComponent);
    component = fixture.componentInstance;
    component.dashboard = testDashboard;
    fixture.detectChanges();
  }));

  it('should be created', () => {
    expect(component).toBeTruthy();
  });

  it('form should exist', () => {
    const form: DebugElement = fixture.debugElement.query(By.css('#dashboard-form'));
    expect(form).toBeTruthy();
  });

  it('should call the submitForm method when the signup-form is submitted', () => {
    const form: DebugElement = fixture.debugElement.query(By.css('#dashboard-form'));
    const fnc: any = spyOn(component, 'submitDashboard');

    form.triggerEventHandler('ngSubmit', null);

    expect(fnc).toHaveBeenCalled();
  });

  it('submit button should exist', () => {
    const btn: DebugElement = fixture.debugElement.query(By.css('#save-changes-button'));
    expect(btn).toBeTruthy();
  });

  it('should submit form when submit button is clicked', () => {
    const btn: DebugElement = fixture.debugElement.query(By.css('#save-changes-button'));
    const fnc: any = spyOn(component, 'submitDashboard');

    (btn.nativeElement as HTMLButtonElement).click();
    fixture.detectChanges();

    expect(fnc).toHaveBeenCalled();
  });

  it('submit button should be disabled if dashboard is invalid', () => {
    const btn: DebugElement = fixture.debugElement.query(By.css('#save-changes-button'));

    expect(btn.nativeElement.disabled).toBe(!component.dbFormValid());
  });

  it('dashboard title input exists', () => {
    const input: DebugElement = fixture.debugElement.query(By.css('#dashboard-title'));

    expect(input).toBeTruthy();
  });

  it('dashboard title is displayed in input', () => {
    const input: DebugElement = fixture.debugElement.query(By.css('#dashboard-title'));

    expect(input.nativeElement.value).toBe(testDashboard.title);
  });

  it('chart title is displayed in input', () => {
    for (const chart of testDashboard.charts) {
      const input: DebugElement = fixture.debugElement.query(By.css(`#chart-${chart.id}-title`));

      expect(input.nativeElement.value).toBe(chart.title.text);
    }
  });

  it('expect input to display initial value of dashboard', () => {
    const input: DebugElement = fixture.debugElement.query(By.css('#dashboard-title'));

    expect(input.nativeElement.value === testDashboard.title).toBeTruthy();
  });

  afterEach(() => {
    fixture.destroy();
  });
});
