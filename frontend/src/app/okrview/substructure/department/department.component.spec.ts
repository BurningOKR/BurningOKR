// tslint:disable:rxjs-finnish

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

describe('DepartmentComponent', () => {
  let component: DepartmentComponent;
  let fixture: ComponentFixture<DepartmentComponent>;

  const departmentMapperService: any = {};
  const departmentContextRoleService: any = {};
  const router: any = {};
  const route: any = {
    paramMap: of({})
  };
  const currentOkrViewService: any = {};
  const currentCycleService: any = {
    getCurrentCycle$: jest.fn()
  };
  const excelService: any = {};
  const i18n: any = {};

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
    currentCycleService.getCurrentCycle$.mockReset();
    currentCycleService.getCurrentCycle$.mockReturnValue(of(null));

    fixture = TestBed.createComponent(DepartmentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
