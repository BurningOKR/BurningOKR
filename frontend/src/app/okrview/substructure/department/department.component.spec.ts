import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { DepartmentComponent } from './department.component';
import { DepartmentMapper } from '../../../shared/services/mapper/department.mapper';
import { DepartmentContextRoleService } from '../../../shared/services/helper/department-context-role.service';
import { ActivatedRoute, Router } from '@angular/router';
import { CurrentOkrviewService } from '../../current-okrview.service';
import { ExcelMapper } from '../../excel-file/excel.mapper';
import { CurrentCycleService } from '../../current-cycle.service';
import { I18n } from '@ngx-translate/i18n-polyfill';
import { SharedModule } from '../../../shared/shared.module';
import { MaterialTestingModule } from '../../../testing/material-testing.module';

describe('DepartmentComponent', () => {
  let component: DepartmentComponent;
  let fixture: ComponentFixture<DepartmentComponent>;

  const departmentMapperService: any = {};
  const departmentContextRoleService: any = {};
  const router: any = {};
  const route: any = {};
  const currentOkrViewService: any = {};
  const currentCycleService: any = {};
  const excelService: any = {};
  const i18n: any = {};

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DepartmentComponent],
      imports: [SharedModule, MaterialTestingModule],
      providers: [
        {provide: DepartmentMapper, useValue: departmentMapperService},
        {provide: DepartmentContextRoleService, useValue: departmentContextRoleService},
        {provide: Router, useValue: router},
        {provide: ActivatedRoute, useValue: route},
        {provide: CurrentOkrviewService, useValue: currentOkrViewService},
        {provide: CurrentCycleService, useValue: currentCycleService},
        {provide: ExcelMapper, useValue: excelService},
        {provide: I18n, useValue: i18n}

      ]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DepartmentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
