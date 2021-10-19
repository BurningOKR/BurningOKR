import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { OkrChildUnitTabComponent } from './okr-child-unit-tab.component';
import { MaterialTestingModule } from '../../../testing/material-testing.module';
import { OkrChildUnitPreviewButtonComponent } from '../okr-child-unit-preview-button/okr-child-unit-preview-button.component';
import { RouterTestingModule } from '@angular/router/testing';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CurrentOkrviewService } from '../../current-okrview.service';
import { OkrUnitService } from '../../../shared/services/mapper/okr-unit.service';
import { ContextRole } from '../../../shared/model/ui/context-role';
import { OkrBranch } from '../../../shared/model/ui/OrganizationalUnit/okr-branch';
import { CycleState, CycleUnit } from '../../../shared/model/ui/cycle-unit';
import { OkrChildUnitFormComponent } from '../okr-child-unit-form/okr-child-unit-form.component';
import { of } from 'rxjs';
import { UnitType } from '../../../shared/model/api/OkrUnit/unit-type.enum';
import { AddChildUnitButtonComponent } from '../../add-child-unit-button/add-child-unit-button.component';
import { I18n } from '@ngx-translate/i18n-polyfill';

const currentOkrViewServiceMock: any = {
  refreshCurrentDepartmentView: jest.fn()
};

const okrUnitServiceMock: any = {};

const dialogMock: any = {
  open: jest.fn()
};

const dialogRefMock: any = {
  afterClosed: jest.fn()
};

const snackBarMock: any = {
  open: jest.fn()
};

const i18nMock: any = jest.fn();

let currentUserRole: ContextRole;
let okrBranch: OkrBranch;
let cycle: CycleUnit;

describe('OkrChildUnitTabComponent', () => {
  let component: OkrChildUnitTabComponent;
  let fixture: ComponentFixture<OkrChildUnitTabComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ OkrChildUnitTabComponent, OkrChildUnitPreviewButtonComponent, AddChildUnitButtonComponent ],
      imports: [ MaterialTestingModule, RouterTestingModule, MatDialogModule ],
      providers: [
        { provide: CurrentOkrviewService, useValue: currentOkrViewServiceMock },
        { provide: OkrUnitService, useValue: okrUnitServiceMock },
        { provide: MatDialog, useValue: dialogMock },
        { provide: MatSnackBar, useValue: snackBarMock },
        { provide: I18n, useValue: i18nMock }
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {

    currentUserRole = new ContextRole();
    okrBranch = new OkrBranch(1, 'test', [], 'testBranch', 2, [], true, false);
    cycle = new CycleUnit(3, 'testCycle', [], new Date(), new Date(), CycleState.ACTIVE, true);

    dialogMock.open.mockReset();
    dialogMock.open.mockReturnValue(dialogRefMock);

    dialogRefMock.afterClosed.mockReset();
    dialogRefMock.afterClosed.mockReturnValue(of(of(okrBranch)));

    currentOkrViewServiceMock.refreshCurrentDepartmentView.mockReset();

    fixture = TestBed.createComponent(OkrChildUnitTabComponent);
    component = fixture.componentInstance;

    component.currentUserRole = currentUserRole;
    component.okrBranch = okrBranch;
    component.cycle = cycle;

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });

  it('clickedAddChildOkrBranch opens dialog', () => {
    component.clickedAddChildOkrBranch();

    expect(dialogMock.open)
      .toHaveBeenCalledWith(OkrChildUnitFormComponent, {
        data: { childUnitId: okrBranch.id, unitType: UnitType.OKR_BRANCH }
      });
  });

  it('clickedAddChildOkrBranch adds subdepartment after closed', done => {
    component.clickedAddChildOkrBranch();

    setTimeout(() => {
      expect(component.okrBranch.okrChildUnitIds.includes(1))
        .toBeTruthy();
      expect(currentOkrViewServiceMock.refreshCurrentDepartmentView)
        .toHaveBeenCalled();
      done();
    }, 500);
  });

  it('clickedAddChildDepartment opens dialog', () => {
    component.clickedAddChildDepartment();

    expect(dialogMock.open)
      .toHaveBeenCalledWith(OkrChildUnitFormComponent, {
        data: { childUnitId: okrBranch.id, unitType: UnitType.DEPARTMENT }
      });
  });

  it('clickedAddChildDepartment adds subdepartment after closed', done => {
    component.clickedAddChildDepartment();

    setTimeout(() => {
      expect(component.okrBranch.okrChildUnitIds.includes(1))
        .toBeTruthy();
      expect(currentOkrViewServiceMock.refreshCurrentDepartmentView)
        .toHaveBeenCalled();
      done();
    }, 500);
  });
});
