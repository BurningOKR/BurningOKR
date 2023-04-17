import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateDashboardComponent } from './create-dashboard.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { TranslateModule } from '@ngx-translate/core';
import { ReactiveFormsModule } from '@angular/forms';
import { CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA } from '@angular/core';
import { DepartmentMapper } from '../../../../shared/services/mapper/department.mapper';
import { MatDialog } from '@angular/material/dialog';
import { DashboardService } from '../../../services/dashboard.service';
import { ActivatedRoute, Router } from '@angular/router';
import { of } from 'rxjs';
import { OkrDepartment } from '../../../../shared/model/ui/OrganizationalUnit/okr-department';
import { UnitType } from '../../../../shared/model/api/OkrUnit/unit-type.enum';

const departmentService: any = {
  getAllDepartmentsForCompanyFlatted$: jest.fn(),
};

const dashboardService: any = {
  // getAllDepartmentsForCompanyFlatted$: jest.fn(),
};

const activatedRoute: any = {
  snapshot: jest.fn(),
  paramMap: jest.fn(),
  // getAllDepartmentsForCompanyFlatted$: jest.fn(),
};

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

describe('CreateDashboardComponent', () => {
  let component: CreateDashboardComponent;
  let fixture: ComponentFixture<CreateDashboardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        TranslateModule.forRoot(),
        ReactiveFormsModule,
      ],
      declarations: [CreateDashboardComponent],
      schemas: [CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA],
      providers: [
        { provide: DepartmentMapper, useValue: departmentService },
        { provide: DashboardService, useValue: dashboardService },
        { provide: MatDialog, useValue: {} },
        { provide: ActivatedRoute, useValue: activatedRoute },
        { provide: Router, useValue: {} },
      ],
    })
      .compileComponents();

    departmentService.getAllDepartmentsForCompanyFlatted$.mockReset();
    departmentService.getAllDepartmentsForCompanyFlatted$.mockReturnValue(of([okrTestDepartment1, okrTestDepartment2]));

    fixture = TestBed.createComponent(CreateDashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
