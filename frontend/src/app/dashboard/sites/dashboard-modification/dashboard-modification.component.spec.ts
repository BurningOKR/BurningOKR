import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { DashboardModificationComponent } from './dashboard-modification.component';
import { DepartmentMapper } from '../../../shared/services/mapper/department.mapper';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { MatDialog } from '@angular/material/dialog';
import { TranslateModule } from '@ngx-translate/core';
import { Component, CUSTOM_ELEMENTS_SCHEMA, DebugElement, NO_ERRORS_SCHEMA, ViewChild } from '@angular/core';
import { of } from 'rxjs';
import { OkrDepartment } from '../../../shared/model/ui/OrganizationalUnit/okr-department';
import { Dashboard } from '../../model/ui/dashboard';
import { UnitType } from '../../../shared/model/api/OkrUnit/unit-type.enum';
import { ReactiveFormsModule } from '@angular/forms';
import { By } from '@angular/platform-browser';

// const departmentMapperMock: any = {
//   getDepartmentById$: jest.fn(getDepartmentByIdMock$),
//   getAllDepartmentsForCompanyFlatted$: jest.fn(getAllDepartmentsForCompanyFlattedMock$)
// };
//
// function getDepartmentByIdMock$(departmentId: OkrUnitId): Observable<OkrDepartment> {
//   console.log('Hello World!');
//
//   return null; //of(testCompanyList.filter(c => c.id === companyId).pop());
// }
//
// function getAllDepartmentsForCompanyFlattedMock$(companyId: CompanyId): Observable<OkrDepartment[]> {
//   console.log('Hello World!');
//
//   return [okrTestDepartment1, okrTestDepartment2]; //of(testCompanyList.filter(c => c.id === companyId).pop());
// }
//

@Component({
  selector: 'app-host-component',
  template: '<app-dashboard-modification></app-dashboard-modification>',
})
class TestHostComponent {
  @ViewChild(DashboardModificationComponent)
  dashboardModificationComponent: DashboardModificationComponent;
}

const okrTestDepartment1: OkrDepartment = {
  id: 1,
  isActive: true,
  isParentUnitABranch: false,
  label: 'department',
  name: 'test1',
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
  let testHostComponent: TestHostComponent;
  let testHostFixture: ComponentFixture<TestHostComponent>;

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

    fixture = TestBed.createComponent(DashboardModificationComponent);
    component = fixture.componentInstance;
    component.dashboard = testDashboard;
    fixture.detectChanges();
  }));

  beforeEach(() => {
    testHostFixture = TestBed.createComponent(TestHostComponent);
    testHostComponent = testHostFixture.componentInstance;
    testHostFixture.detectChanges();
  });

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

  // it('should submit form when enter was pressed on submit button', () => {
  //   const enterKeypress: KeyboardEvent = new KeyboardEvent('keypress', {
  //     key: '13',
  //     cancelable: true
  //   });
  //
  //   const btn: DebugElement = fixture.debugElement.query(By.css('#save-changes-button'));
  //
  //   fixture.detectChanges();
  // });

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

  // it('dashboard title input labeled to fulfill wcag 3.3.2 criteria', () => {
  //   const input: DebugElement = fixture.debugElement.query(By.css('#dashboard-title'));
  //
  //   expect(
  //     input.nativeElement.hasAttribute('label')
  //     || input.nativeElement.hasAttribute('aria-label')
  //     || input.nativeElement.hasAttribute('aria-labelledby')
  //     || input.nativeElement.hasAttribute('aria-describedby')
  //     || (input.nativeElement.hasAttribute('placeholder') && input.parent.nativeElement.ty ),
  //   ).toBe(true);
  // });

  // it('dashboard title is required', () => {
  //   // const input: DebugElement = fixture.debugElement.query(By.css('#dashboard-title'));
  //   const ctrl: any = component.registerForm.get('fcDashboardTitle');
  //
  //   ctrl.setValue(null);
  //   fixture.detectChanges();
  //
  //   expect(ctrl.invalid).toBeTruthy();
  // });

  it('chart title is displayed in input', () => {
    for (const chart of testDashboard.charts) {
      const input: DebugElement = fixture.debugElement.query(By.css(`#chart-${chart.id}-title`));

      expect(input.nativeElement.value).toBe(chart.title.text);
    }
  });

  it('test dashboard input', () => {
    testHostComponent.dashboardModificationComponent.dashboard = testDashboard;
    testHostFixture.detectChanges();
    expect(testHostFixture.debugElement.nativeElement.query(By.css('#dashboard-title')) === testDashboard.title).toBeTruthy();
  });

  afterEach(() => {
    fixture.destroy();
  });
});
