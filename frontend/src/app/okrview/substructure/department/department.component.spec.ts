// tslint:disable:rxjs-finnish

import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { DepartmentComponent } from './department.component';
import { DepartmentMapper } from '../../../shared/services/mapper/department.mapper';
import { DepartmentContextRoleService } from '../../../shared/services/helper/department-context-role.service';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { CurrentOkrviewService } from '../../current-okrview.service';
import { ExcelMapper } from '../../excel-file/excel.mapper';
import { CurrentCycleService } from '../../current-cycle.service';
import { I18n } from '@ngx-translate/i18n-polyfill';
import { SharedModule } from '../../../shared/shared.module';
import { MaterialTestingModule } from '../../../testing/material-testing.module';
import { SubStructuresTabComponent } from '../substructures-tab/sub-structures-tab.component';
import { SubstructureOverviewTabComponent } from '../substructure-tab-overview/substructure-overview-tab.component';
import { DepartmentTabTeamComponent } from './department-tab-team/department-tab-team.component';
import { SubstructurePreviewButtonComponent } from '../substructure-preview-button/substructure-preview-button.component';
import { CdkDropList } from '@angular/cdk/drag-drop';
import { ObjectiveComponent } from '../../objective/objective.component';
import { DepartmentTeamNewUserComponent } from './department-team-new-user/department-team-new-user.component';
import { RouterTestingModule } from '@angular/router/testing';
import { ObjectiveContentsComponent } from '../../objective/objective-contents/objective-contents.component';
import { KeyresultComponent } from '../../keyresult/keyresult.component';
import { of } from 'rxjs';
import { DepartmentUnit } from '../../../shared/model/ui/OrganizationalUnit/department-unit';
import { ObjectiveId, UserId } from '../../../shared/model/id-types';

describe('DepartmentComponent', () => {
  let component: DepartmentComponent;
  let fixture: ComponentFixture<DepartmentComponent>;

  const departmentMapperService: any = {
    getDepartmentById$: jest.fn()
  };

  const departmentContextRoleService: any = {
    getContextRoleForDepartment$: jest.fn()
  };
  const router: any = {};

  const paramMapGetSpy: any = jest.fn();
  const paramMapGetAllSpy: any = jest.fn();
  const paramMapHasSpy: any = jest.fn();

  const route: any = {
    // tslint:disable-next-line:no-object-literal-type-assertion
    paramMap: of({
      get: paramMapGetSpy,
      getAll: paramMapGetAllSpy,
      has: paramMapHasSpy,
      keys: []
    } as ParamMap)
  };

  const currentOkrViewService: any = {
    browseDepartment: jest.fn()
  };

  const currentCycleService: any = {
    getCurrentCycle$: jest.fn()
  };

  const excelService: any = {};
  const i18n: any = {};

  const department: DepartmentUnit =
    new DepartmentUnit(
      1,
      'testDepartment',
      [],
      0,
      'department',
      'master', 'topicSponsor', ['member'],
    true, false);

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        DepartmentComponent,
        SubStructuresTabComponent,
        SubstructureOverviewTabComponent,
        DepartmentTabTeamComponent,
        SubstructurePreviewButtonComponent,
        CdkDropList,
        ObjectiveComponent,
        DepartmentTeamNewUserComponent,
        ObjectiveContentsComponent,
        KeyresultComponent
      ],
      imports: [SharedModule, MaterialTestingModule, RouterTestingModule],
      providers: [
        { provide: DepartmentMapper, useValue: departmentMapperService },
        { provide: DepartmentContextRoleService, useValue: departmentContextRoleService },
        { provide: Router, useValue: router },
        { provide: ActivatedRoute, useValue: route },
        { provide: CurrentOkrviewService, useValue: currentOkrViewService },
        { provide: CurrentCycleService, useValue: currentCycleService },
        { provide: ExcelMapper, useValue: excelService },
        { provide: I18n, useValue: i18n }

      ]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    paramMapGetSpy.mockReset();
    paramMapGetAllSpy.mockReset();
    paramMapHasSpy.mockReset();

    departmentMapperService.getDepartmentById$.mockReset();
    departmentMapperService.getDepartmentById$.mockReturnValue(of(department));

    currentCycleService.getCurrentCycle$.mockReset();
    currentCycleService.getCurrentCycle$.mockReturnValue(of(null));

    currentOkrViewService.browseDepartment.mockReset();

    departmentContextRoleService.getContextRoleForDepartment$.mockReset();
    departmentContextRoleService.getContextRoleForDepartment$.mockReturnValue(of(null));
  });

  it('should create', () => {
    fixture = TestBed.createComponent(DepartmentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    expect(component)
      .toBeTruthy();
  });

  it('should get department by id given in params', done => {
    paramMapGetSpy.mockReturnValue('1');

    fixture = TestBed.createComponent(DepartmentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    component.department$.subscribe(() => {
      expect(departmentMapperService.getDepartmentById$)
        .toHaveBeenCalledWith(1);
      done();
    });
  });

  it('should browse department with id given in params', done => {
    paramMapGetSpy.mockReturnValue('1');

    fixture = TestBed.createComponent(DepartmentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    component.department$.subscribe(() => {
      expect(currentOkrViewService.browseDepartment)
        .toHaveBeenCalledWith(1);
      done();
    });
  });

  it('should getContextRoleForDepartment$', done => {
    paramMapGetSpy.mockReturnValue('1');

    fixture = TestBed.createComponent(DepartmentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    component.currentUserRole$.subscribe(() => {
      expect(departmentContextRoleService.getContextRoleForDepartment$)
        .toHaveBeenCalledWith(department);
      done();
    });
  });
});
