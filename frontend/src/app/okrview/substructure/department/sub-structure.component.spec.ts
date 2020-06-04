// tslint:disable:rxjs-finnish

import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { SubStructureComponent } from './sub-structure.component';
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
import { NoopAnimationsModule } from '@angular/platform-browser/animations';

describe('SubStructureComponent', () => {
  let component: SubStructureComponent;
  let fixture: ComponentFixture<SubStructureComponent>;

  const departmentMapperService: any = {
    getDepartmentById$: jest.fn(),
    putDepartment$: jest.fn(),
    deleteDepartment$: jest.fn()
  };

  const departmentContextRoleService: any = {
    getContextRoleForDepartment$: jest.fn()
  };
  const router: any = {
    navigate: jest.fn()
  };

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
    browseDepartment: jest.fn(),
    refreshCurrentDepartmentView: jest.fn(),
    refreshCurrentCompanyView: jest.fn()
  };

  const currentCycleService: any = {
    getCurrentCycle$: jest.fn()
  };

  const excelService: any = {};
  const i18n: any = {};

  let department: DepartmentUnit;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        SubStructureComponent,
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
      imports: [SharedModule, MaterialTestingModule, RouterTestingModule, NoopAnimationsModule],
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
    departmentMapperService.putDepartment$.mockReset();
    departmentMapperService.putDepartment$.mockReturnValue(of(department));
    departmentMapperService.deleteDepartment$.mockReset();
    departmentMapperService.deleteDepartment$.mockReturnValue(of(true));

    currentCycleService.getCurrentCycle$.mockReset();
    currentCycleService.getCurrentCycle$.mockReturnValue(of(null));

    currentOkrViewService.browseDepartment.mockReset();
    currentOkrViewService.refreshCurrentCompanyView.mockReset();
    currentOkrViewService.refreshCurrentDepartmentView.mockReset();

    departmentContextRoleService.getContextRoleForDepartment$.mockReset();
    departmentContextRoleService.getContextRoleForDepartment$.mockReturnValue(of(null));

    router.navigate.mockReset();
    router.navigate.mockReturnValue({catch: jest.fn()});

    department = new DepartmentUnit(
      1,
      'testDepartment',
      [],
      0,
      'department',
      'master', 'topicSponsor', ['member'],
      true, false);
  });

  it('should create', () => {
    fixture = TestBed.createComponent(SubStructureComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    expect(component)
      .toBeTruthy();
  });

  it('should get department by id given in params', done => {
    paramMapGetSpy.mockReturnValue('1');

    fixture = TestBed.createComponent(SubStructureComponent);
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

    fixture = TestBed.createComponent(SubStructureComponent);
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

    fixture = TestBed.createComponent(SubStructureComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    component.currentUserRole$.subscribe(() => {
      expect(departmentContextRoleService.getContextRoleForDepartment$)
        .toHaveBeenCalledWith(department);
      done();
    });
  });

  it('toggleDepartmentActive toggles active', () => {
    fixture = TestBed.createComponent(SubStructureComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    department.isActive = false;

    component.toggleDepartmentActive(department);

    expect(department.isActive)
      .toBeTruthy();
  });

  it('toggleDepartmentActive toggles active with falsy value', () => {
    fixture = TestBed.createComponent(SubStructureComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    department.isActive = null;

    component.toggleDepartmentActive(department);

    expect(department.isActive)
      .toBeTruthy();
  });

  it('toggleDepartmentActive toggles inactive', () => {
    fixture = TestBed.createComponent(SubStructureComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    department.isActive = true;

    component.toggleDepartmentActive(department);

    expect(department.isActive)
      .toBeFalsy();
  });

  it('toggleDepartmentActive puts department', () => {
    fixture = TestBed.createComponent(SubStructureComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    department.isActive = true;

    component.toggleDepartmentActive(department);

    expect(departmentMapperService.putDepartment$)
      .toHaveBeenCalledWith(department);
  });

  it('queryRemoveDepartment deletes department', () => {
    fixture = TestBed.createComponent(SubStructureComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    component.queryRemoveDepartment(department);

    expect(departmentMapperService.deleteDepartment$)
      .toHaveBeenCalled();
  });

  it('onDepartmentDeleted refreshes department view when parent structure is a corporateObjectiveStructure', () => {
    fixture = TestBed.createComponent(SubStructureComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    department.isParentStructureACorporateObjectiveStructure = true;

    component.onDepartmentDeleted(department);

    expect(currentOkrViewService.refreshCurrentDepartmentView)
      .toHaveBeenCalled();
  });

  it('onDepartmentDeleted refreshes department view when parent structure is not a corporateObjectiveStructure', () => {
    fixture = TestBed.createComponent(SubStructureComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    department.isParentStructureACorporateObjectiveStructure = false;

    component.onDepartmentDeleted(department);

    expect(currentOkrViewService.refreshCurrentCompanyView)
      .toHaveBeenCalled();
  });
});
