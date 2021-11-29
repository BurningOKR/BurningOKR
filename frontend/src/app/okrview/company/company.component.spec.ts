/* eslint-disable */

import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { CompanyComponent } from './company.component';
import { MaterialTestingModule } from '../../testing/material-testing.module';
import { RouterTestingModule } from '@angular/router/testing';
import { CurrentOkrviewService } from '../current-okrview.service';
import { CurrentOkrUnitSchemaService } from '../current-okr-unit-schema.service';
import { CurrentCycleService } from '../current-cycle.service';
import { CompanyMapper } from '../../shared/services/mapper/company.mapper';
import { OkrChildUnitRoleService } from '../../shared/services/helper/okr-child-unit-role.service';
import { ExcelMapper } from '../excel-file/excel.mapper';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { of } from 'rxjs';
import { CycleState, CycleUnit } from '../../shared/model/ui/cycle-unit';
import { OkrUnitRole, OkrUnitSchema } from '../../shared/model/ui/okr-unit-schema';
import { ActivatedRoute, convertToParamMap } from '@angular/router';
import { CompanyUnit } from '../../shared/model/ui/OrganizationalUnit/company-unit';
import { ContextRole } from '../../shared/model/ui/context-role';
import { Component, Input } from '@angular/core';
import { AddChildUnitButtonComponent } from '../add-child-unit-button/add-child-unit-button.component';
import { I18n } from '@ngx-translate/i18n-polyfill';

@Component({
  selector: 'app-okr-child-unit-preview-button',
  template: '',
  styleUrls: []
})
export class OkrChildUnitPreviewButtonMockComponent {
  @Input() unitId: number;
}

describe('CompanyComponent', () => {
  let component: CompanyComponent;
  let fixture: ComponentFixture<CompanyComponent>;

  const currentOkrViewServiceMock: any = {
    browseCompany: jest.fn()
  };

  const currentOkrUnitSchemaServiceMock: any = {
    getCurrentUnitSchemas$: jest.fn()
  };

  const currentCycleServiceMock: any = {
    getCurrentCycle$: jest.fn()
  };

  const companyServiceMock: any = {
    getCompanyById$: jest.fn()
  };

  const roleServiceMock: any = {
    getRoleWithoutContext$: jest.fn()
  };

  const excelServiceMock: any = {
    downloadExcelFileForCompany: jest.fn(),
    downloadExcelEmailFileForCompany: jest.fn()
  };

  const matDialogMock: any = {
    open: jest.fn()
  };

  const dialogRefMock: any = {
    afterClosed: jest.fn()
  };

  const route: any = {
    paramMap: of(convertToParamMap({companyId: '10'}))
  };

  const snackBarMock: any = {
    open: jest.fn()
  };

  const i18nMock: any = jest.fn();

  let cycle: CycleUnit;
  let unitSchemas: OkrUnitSchema[];
  let company: CompanyUnit;
  let contextRole: ContextRole;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [
        CompanyComponent, OkrChildUnitPreviewButtonMockComponent, AddChildUnitButtonComponent
      ],
      imports: [MaterialTestingModule, RouterTestingModule],
      providers: [
        { provide: CurrentOkrviewService, useValue: currentOkrViewServiceMock },
        { provide: CurrentOkrUnitSchemaService, useValue: currentOkrUnitSchemaServiceMock },
        { provide: CurrentCycleService, useValue: currentCycleServiceMock },
        { provide: CompanyMapper, useValue: companyServiceMock },
        { provide: OkrChildUnitRoleService, useValue: roleServiceMock },
        { provide: ExcelMapper, useValue: excelServiceMock },
        { provide: MatDialog, useValue: matDialogMock },
        { provide: ActivatedRoute, useValue: route },
        { provide: I18n, useValue: i18nMock },
        { provide: MatSnackBar, useValue: snackBarMock }
      ]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    cycle = new CycleUnit(1, 'TestCycle', [10], new Date(), new Date(), CycleState.ACTIVE, true);

    unitSchemas = [
      new OkrUnitSchema(11, 'Unit', OkrUnitRole.MANAGER, true, true),
      new OkrUnitSchema(12, 'Name', OkrUnitRole.MEMBER, true, true),
      new OkrUnitSchema(13, 'Schema', OkrUnitRole.USER, true, true)
    ];
    unitSchemas[2].subDepartments = [
      new OkrUnitSchema(14, 'SubUnit', OkrUnitRole.MANAGER, true, true)
    ];

    company = new CompanyUnit(10, 'TestCompany', [], [], 1, 'label');

    contextRole = new ContextRole();

    roleServiceMock.getRoleWithoutContext$.mockReset();
    roleServiceMock.getRoleWithoutContext$.mockReturnValue(of(contextRole));

    currentCycleServiceMock.getCurrentCycle$.mockReset();
    currentCycleServiceMock.getCurrentCycle$.mockReturnValue(of(cycle));

    currentOkrUnitSchemaServiceMock.getCurrentUnitSchemas$.mockReset();
    currentOkrUnitSchemaServiceMock.getCurrentUnitSchemas$.mockReturnValue(of(unitSchemas));

    currentOkrViewServiceMock.browseCompany.mockReset();

    companyServiceMock.getCompanyById$.mockReset();
    companyServiceMock.getCompanyById$.mockReturnValue(of(company));

    excelServiceMock.downloadExcelFileForCompany.mockReset();
    excelServiceMock.downloadExcelEmailFileForCompany.mockReset();

    matDialogMock.open.mockReset();
    matDialogMock.open.mockReturnValue(dialogRefMock);

    dialogRefMock.afterClosed.mockReset();
    dialogRefMock.afterClosed.mockReturnValue(of(of(company)));
  });

  it('should create', () => {
    fixture = TestBed.createComponent(CompanyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    expect(component)
      .toBeTruthy();
  });

  it('gets a company by id given in route params', done => {
    fixture = TestBed.createComponent(CompanyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    component.company$.subscribe(() => {
      expect(companyServiceMock.getCompanyById$)
        .toHaveBeenCalledWith(10, false);
      done();
    });
  });

  it('browseCompany by id given in route params', done => {
    fixture = TestBed.createComponent(CompanyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    component.company$.subscribe(() => {
      expect(currentOkrViewServiceMock.browseCompany)
        .toHaveBeenCalledWith(10);
      done();
    });
  });

  it('cycle$ is set', done => {
    fixture = TestBed.createComponent(CompanyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    component.cycle$.subscribe((cylce: CycleUnit) => {
      expect(cylce)
        .toBeTruthy();
      done();
    });
  });

  it('currentUserRole$ is set', done => {
    fixture = TestBed.createComponent(CompanyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    component.currentUserRole$.subscribe((role: ContextRole) => {
      expect(role)
        .toBeTruthy();
      done();
    });
  });

  it('companyView$ is combined cycle and company', done => {
    fixture = TestBed.createComponent(CompanyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    component.companyView$.subscribe((view: any) => {
      expect(view.company)
        .toBe(company);
      expect(view.cycle)
        .toBe(cycle);
      done();
    });
  });

  it('roleDepartmentIds$ empty schema, contains no ids', done => {
    currentOkrUnitSchemaServiceMock.getCurrentUnitSchemas$.mockReturnValue(of([]));

    fixture = TestBed.createComponent(CompanyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    component.roleDepartmentIds$.subscribe((ids: any) => {
      expect(ids.currentlyMemberDepartmentIds)
        .toEqual([]);
      expect(ids.currentlyManagerDepartmentIds)
        .toEqual([]);
      done();
    });
  });

  it('roleDepartmentIds$ filled schema, contains correct ids', done => {
    fixture = TestBed.createComponent(CompanyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    component.roleDepartmentIds$.subscribe((ids: any) => {
      expect(ids.currentlyMemberDepartmentIds)
        .toEqual([12]);
      expect(ids.currentlyManagerDepartmentIds)
        .toEqual([11, 14]);
      done();
    });
  });

  it('clickedDownloadExcelFileForCompany downloads excel file', () => {
    fixture = TestBed.createComponent(CompanyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    component.clickedDownloadExcelFileForCompany(company);

    expect(excelServiceMock.downloadExcelFileForCompany)
      .toHaveBeenCalledWith(company.id);
  });

  it('clickedDownloadExcelEmailFileForCompany downloads excel file', () => {
    fixture = TestBed.createComponent(CompanyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    component.clickedDownloadExcelEmailFileForCompany(company);

    expect(excelServiceMock.downloadExcelEmailFileForCompany)
      .toHaveBeenCalledWith(company.id);
  });
});
