import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DashboardModificationComponent } from './dashboard-modification.component';
import { DepartmentMapper } from '../../../../shared/services/mapper/department.mapper';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { MatDialog } from '@angular/material/dialog';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA } from '@angular/core';

describe('DashboardModificationComponent', () => {
  let component: DashboardModificationComponent;
  let fixture: ComponentFixture<DashboardModificationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        TranslateModule,
      ],
      declarations: [DashboardModificationComponent],
      schemas: [CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA],
      providers: [
        { provide: DepartmentMapper, useValue: {} }, //, useClass: DepartmentMapper},
        { provide: MatDialog, useValue: {} },
        { provide: TranslateService, useValue: {} },
        // { provide: DepartmentService}
      ],
    })
      .overrideComponent(DashboardModificationComponent, {})
      .compileComponents();

    fixture = TestBed.createComponent(DashboardModificationComponent);
    component = fixture.componentInstance;
    component.dashboard = {
      id: 1000,
      title: 'DashboardMock',
      companyId: 1,
      creatorId: '1',
      charts: [],
      creationDate: null,
    };
    fixture.detectChanges();
  });

  // afterEach(() => {
  //   fixture.destroy();
  // });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

// import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
//
// import { DashboardModificationComponent } from './dashboard-modification.component';
// import { CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA } from '@angular/core';
// import { HttpClientTestingModule } from '@angular/common/http/testing';
// import { MatDialog } from '@angular/material/dialog';
// import { DepartmentMapper } from '../../../../shared/services/mapper/department.mapper';
// import { TranslateModule, TranslateService } from '@ngx-translate/core';
//
// describe('DashboardModificationComponent', () => {
//   let fixture: ComponentFixture<DashboardModificationComponent>;
//   let component: DashboardModificationComponent;
//   // let department: OkrDepartment;
//
//   // const departmentService: any = {
//   //   getAllDepartmentsForCompanyFlatted$: jest.fn(),
//   // };
//
//   // beforeEach(waitForAsync(() => {
//   //   TestBed.configureTestingModule({
//   //     imports: [
//   //       HttpClientTestingModule,
//   //       TranslateModule,
//   //     ],
//   //     declarations: [DashboardModificationComponent],
//   //     schemas: [CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA],
//   //     providers: [
//   //       { provide: DepartmentMapper, useValue: {} },
//   //       { provide: MatDialog, useValue: {} },
//   //       { provide: TranslateService, useValue: {} },
//   //     ],
//   //   })
//   //     .compileComponents();
//   // }));
//
//   beforeEach(() => {
//     TestBed.configureTestingModule({
//       imports: [
//         HttpClientTestingModule,
//         TranslateModule,
//       ],
//       declarations: [DashboardModificationComponent],
//       schemas: [CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA],
//       providers: [
//         { provide: DepartmentMapper, useValue: {} },
//         { provide: MatDialog, useValue: {} },
//         { provide: TranslateService, useValue: {} },
//       ],
//     })
//       .compileComponents();
//
//     fixture = TestBed.createComponent(DashboardModificationComponent);
//     component = fixture.componentInstance;
//
//     // component.dashboard = {
//     //   id: 1000,
//     //   title: 'DashboardMock',
//     //   companyId: 1,
//     //   creatorId: '1',
//     //   charts: [],
//     //   creationDate: null,
//     // };
//
//     // department = {
//     //   id: 1,
//     //   name: 'testDepartment',
//     //   objectives: [],
//     //   parentUnitId: 0,
//     //   label: 'department',
//     //   okrMasterId: 'master',
//     //   okrTopicSponsorId: 'topicSponsor',
//     //   okrMemberIds: ['member'],
//     //   isActive: true,
//     //   isParentUnitABranch: false,
//     //   type: UnitType.DEPARTMENT,
//     // };
//
//     // departmentService.getAllDepartmentsForCompanyFlatted$.mockReset();
//     // departmentService.getAllDepartmentsForCompanyFlatted$.mockReturnValue(of(department));
//
//     fixture.detectChanges();
//   });
//
//   it('should create', () => {
//     expect(component).toBeTruthy();
//   });
//
//   // it('should call updateDashboard', () => {
//   //   spyOn(component, 'updateDashboard');
//   //   const button = fixture.debugElement.nativeElement.querySelector('button[id="save-changes-button"]');
//   //   button.click();
//   //   tick();
//   //   expect(component.updateDashboard).toHaveBeenCalled();
//   //
//   //   // const onClickMock = spyOn(component, 'updateDashboard');
//   //   // // const ne = fixture.nativeElement as HTMLElement;
//   //   // // ne.querySelector('button[id="save-changes-button"]');
//   //   // de.query(By.css('button')).triggerEventHandler('click', null);
//   //   // expect(onClickMock).toHaveBeenCalled();
//   //
//   //   // spyOn(component, 'updateDashboard');
//   //   // const button = de.nativeElement.querySelector('button');
//   //   // button.click();
//   //   // tick();
//   //   // expect(component.updateDashboard).toHaveBeenCalled();
//   // });
// });
