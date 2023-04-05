import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { DashboardModificationComponent } from './dashboard-modification.component';
import { DepartmentMapper } from '../../../../shared/services/mapper/department.mapper';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { MatDialog } from '@angular/material/dialog';
import { TranslateModule } from '@ngx-translate/core';
import { CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA } from '@angular/core';
import { of } from 'rxjs';
import { OkrDepartment } from '../../../../shared/model/ui/OrganizationalUnit/okr-department';
import { Dashboard } from '../../../model/ui/dashboard';
import { UnitType } from '../../../../shared/model/api/OkrUnit/unit-type.enum';
import { ReactiveFormsModule } from '@angular/forms';

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

  // beforeEach(() => {
  //   // fixture = TestBed.createComponent(DashboardModificationComponent);
  //   // component = fixture.componentInstance;
  //   // component = TestBed.inject(DashboardModificationComponent);
  //   // departmentMapperMock.getDepartmentById$.mockReset();
  //   // departmentMapperMock.getDepartmentById$.mockImplementation(getDepartmentByIdMock$);
  //   // departmentMapperMock.getAllDepartmentsForCompanyFlatted$.mockReset();
  //   // departmentMapperMock.getAllDepartmentsForCompanyFlatted$.mockImplementation(getAllDepartmentsForCompanyFlattedMock$);
  //
  //   // departmentService.getAllDepartmentsForCompanyFlatted$.mockReset();
  //   // departmentService.getAllDepartmentsForCompanyFlatted$.mockReturnValue(of([okrTestDepartment1, okrTestDepartment2]));
  //   //
  //   // fixture = TestBed.createComponent(DashboardModificationComponent);
  //   // component = fixture.componentInstance;
  //   // component.dashboard = testDashboard;
  //   // fixture.detectChanges();
  // });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });

  // beforeEach(async () => {
  //   await TestBed.configureTestingModule({
  //     imports: [
  //       HttpClientTestingModule,
  //       TranslateModule,
  //     ],
  //     declarations: [DashboardModificationComponent],
  //     schemas: [CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA],
  //     providers: [
  //       { provide: DepartmentMapper }, //, useClass: DepartmentMapper},
  //       { provide: MatDialog, useValue: {} },
  //       { provide: TranslateService, useValue: {} },
  //       { provide: AuthenticationService },
  //       { provide: OAuthService },
  //       { provide: DepartmentService}
  //     ],
  //   })
  //     .overrideComponent(DashboardModificationComponent, {})
  //     .compileComponents();
  //
  //   fixture = TestBed.createComponent(DashboardModificationComponent);
  //   component = fixture.componentInstance;
  //   component.dashboard = {
  //     id: 1000,
  //     title: 'DashboardMock',
  //     companyId: 1,
  //     creatorId: '1',
  //     charts: [],
  //     creationDate: null,
  //   };
  //   fixture.detectChanges();
  // });

  afterEach(() => {
    fixture.destroy();
  });

  // it('should create', () => {
  //   expect(component).toBeTruthy();
  // });
});
