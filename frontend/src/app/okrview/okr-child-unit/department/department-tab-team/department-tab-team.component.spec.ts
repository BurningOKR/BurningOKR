/* eslint-disable */

import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { DepartmentTabTeamComponent } from './department-tab-team.component';
import { MaterialTestingModule } from '../../../../testing/material-testing.module';
import { CurrentUserService } from '../../../../core/services/current-user.service';
import { DepartmentMapper } from '../../../../shared/services/mapper/department.mapper';
import { ConfigurationManagerService } from '../../../../core/settings/configuration-manager.service';
import { MatDialog } from '@angular/material/dialog';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CurrentOkrUnitSchemaService } from '../../../current-okr-unit-schema.service';
import { CycleState, CycleUnit } from '../../../../shared/model/ui/cycle-unit';
import { OkrDepartment } from '../../../../shared/model/ui/OrganizationalUnit/okr-department';
import { ContextRole } from '../../../../shared/model/ui/context-role';
import { of } from 'rxjs';
import { Configuration } from '../../../../shared/model/ui/configuration';
import { User } from '../../../../shared/model/api/user';
import {UnitType} from "../../../../shared/model/api/OkrUnit/unit-type.enum";

const currentUserServiceMock: any = {
  getCurrentUser$: jest.fn(),
};

const departmentMapperMock: any = {
  putDepartment$: jest.fn(),
};

const configurationManagerServiceMock: any = {
  getTopicSponsorsActivated$: jest.fn(),
};

const matDialogMock: any = {
  open: jest.fn(),
};

const matDialogReferenceMock: any = {
  afterClosed: jest.fn(),
};

const currentOkrUnitSchemaServiceMock: any = {
  updateSchemaTeamRole: jest.fn(),
};

let cycle: CycleUnit;
let department: OkrDepartment;
let currentUserRole: ContextRole;

@Component({
  // eslint-disable-next-line @angular-eslint/component-selector
  selector: 'app-department-team-new-user',
  template: '',
})
class DepartmentTeamNewUserMockComponent {
  @Output() choseUser = new EventEmitter();
  @Input() inputPlaceholderText: string;
}

@Component({
  // eslint-disable-next-line @angular-eslint/component-selector
  selector: 'app-user-minibutton',
  template: '',
})
class UserMinibuttonMockComponent {
  @Input() userId: string;
  @Input() canBeRemoved: boolean;
  @Output() userDeleted = new EventEmitter();
}

describe('DepartmentTabTeamComponent', () => {
  let component: DepartmentTabTeamComponent;
  let fixture: ComponentFixture<DepartmentTabTeamComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [DepartmentTabTeamComponent, DepartmentTeamNewUserMockComponent, UserMinibuttonMockComponent],
      imports: [MaterialTestingModule],
      providers: [
        { provide: CurrentUserService, useValue: currentUserServiceMock },
        { provide: DepartmentMapper, useValue: departmentMapperMock },
        { provide: ConfigurationManagerService, useValue: configurationManagerServiceMock },
        { provide: CurrentOkrUnitSchemaService, useValue: currentOkrUnitSchemaServiceMock },
        { provide: MatDialog, useValue: matDialogMock },
      ],
    })
      .compileComponents();
  }));

  beforeEach(() => {
    cycle = new CycleUnit(1, 'testCycle', [], new Date(), new Date(), CycleState.ACTIVE, true);
    department = {
      id: 2,
      name: 'testDepartment',
      objectives: [],
      parentUnitId: 0,
      label: 'label',
      okrMasterId: null,
      okrTopicSponsorId: null,
      okrMemberIds: [],
      isActive: true,
      isParentUnitABranch: false,
      type: UnitType.DEPARTMENT,
    }    ;
    currentUserRole = new ContextRole();

    configurationManagerServiceMock.getTopicSponsorsActivated$.mockReset();
    configurationManagerServiceMock.getTopicSponsorsActivated$.mockReturnValue(
      of(new Configuration(1, 'topic-sponsors-activated', true, 'boolean')),
    );

    matDialogMock.open.mockReset();
    matDialogMock.open.mockReturnValue(matDialogReferenceMock);

    matDialogReferenceMock.afterClosed.mockReset();
    matDialogReferenceMock.afterClosed.mockReturnValue(of(true));

    departmentMapperMock.putDepartment$.mockReset();
    departmentMapperMock.putDepartment$.mockReturnValue(of(department));

    currentUserServiceMock.getCurrentUser$.mockReset();
    currentUserServiceMock.getCurrentUser$.mockReturnValue(of(new User()));

    currentOkrUnitSchemaServiceMock.updateSchemaTeamRole.mockReset();
  });

  const createComponent: any = (ccycle: CycleUnit, cdepartment: OkrDepartment, role: ContextRole) => {
    fixture = TestBed.createComponent(DepartmentTabTeamComponent);
    component = fixture.componentInstance;

    component.cycle = ccycle;
    component.department = cdepartment;
    component.currentUserRole = role;

    fixture.detectChanges();
  };

  it('should create', () => {
    createComponent(cycle, department, currentUserRole);

    expect(component)
      .toBeTruthy();
  });

  it('ngOnInit should set topicSponsorsActivated$ to true, when topicSponsors are activated', done => {
    createComponent(cycle, department, currentUserRole);

    configurationManagerServiceMock.getTopicSponsorsActivated$.mockReset();
    configurationManagerServiceMock.getTopicSponsorsActivated$.mockReturnValue(
      of(new Configuration(1, 'topic-sponsors-activated', true, 'boolean')),
    );

    component.ngOnInit();
    component.topicSponsorsActivated$.subscribe((activated: boolean) => {
      expect(activated)
        .toBeTruthy();
      done();
    });
  });

  it('ngOnInit should set topicSponsorsActivated$ to false, when topicSponsors are not activated', done => {
    createComponent(cycle, department, currentUserRole);

    configurationManagerServiceMock.getTopicSponsorsActivated$.mockReset();
    configurationManagerServiceMock.getTopicSponsorsActivated$.mockReturnValue(
      of(new Configuration(1, 'topic-sponsors-activated', false, 'boolean')),
    );

    component.ngOnInit();
    component.topicSponsorsActivated$.subscribe((activated: boolean) => {
      expect(activated)
        .toBeFalsy();
      done();
    });
  });

  it('canEditManagers: notAdmin, cycleClosed => false', () => {
    cycle.cycleState = CycleState.CLOSED;
    currentUserRole.isAdmin = false;

    createComponent(cycle, department, currentUserRole);

    component.ngOnInit();

    expect(component.canEditManagers)
      .toBeFalsy();
  });

  it('canEditManagers: notAdmin, cycleOpen => false', () => {
    cycle.cycleState = CycleState.ACTIVE;
    currentUserRole.isAdmin = false;

    createComponent(cycle, department, currentUserRole);

    component.ngOnInit();

    expect(component.canEditManagers)
      .toBeFalsy();
  });

  it('canEditManagers: isAdmin, cycleClosed => false', () => {
    cycle.cycleState = CycleState.CLOSED;
    currentUserRole.isAdmin = true;

    createComponent(cycle, department, currentUserRole);

    component.ngOnInit();

    expect(component.canEditManagers)
      .toBeFalsy();
  });

  it('canEditManagers: isAdmin, cycleOpen => true', () => {
    cycle.cycleState = CycleState.ACTIVE;
    currentUserRole.isAdmin = true;

    createComponent(cycle, department, currentUserRole);

    component.ngOnInit();

    expect(component.canEditManagers)
      .toBeTruthy();
  });

  it('canEditMembers: notManager, cycleClosed => false', () => {
    cycle.cycleState = CycleState.CLOSED;
    currentUserRole.isOKRManager = false;
    currentUserRole.isAdmin = false;

    createComponent(cycle, department, currentUserRole);

    component.ngOnInit();

    expect(component.canEditMembers)
      .toBeFalsy();
  });

  it('canEditMembers: notManager, cycleOpen => false', () => {
    cycle.cycleState = CycleState.ACTIVE;
    currentUserRole.isOKRManager = false;
    currentUserRole.isAdmin = false;

    createComponent(cycle, department, currentUserRole);

    component.ngOnInit();

    expect(component.canEditMembers)
      .toBeFalsy();
  });

  it('canEditMembers: isManager, cycleClosed => false', () => {
    cycle.cycleState = CycleState.CLOSED;
    currentUserRole.isOKRManager = true;

    createComponent(cycle, department, currentUserRole);

    component.ngOnInit();

    expect(component.canEditMembers)
      .toBeFalsy();
  });

  it('canEditMembers: isManager, cycleOpen => true', () => {
    cycle.cycleState = CycleState.ACTIVE;
    currentUserRole.isOKRManager = true;

    createComponent(cycle, department, currentUserRole);

    component.ngOnInit();

    expect(component.canEditMembers)
      .toBeTruthy();
  });

  it('clickedDeleteOKRMaster deletes OKRMaster when dialog is confirmed', done => {
    createComponent(cycle, department, currentUserRole);

    component.department.okrMasterId = 'testOkrMaster';

    component.clickedDeleteOKRMaster();

    setTimeout(() => {
      expect(matDialogReferenceMock.afterClosed)
        .toHaveBeenCalled();
      expect(departmentMapperMock.putDepartment$)
        .toHaveBeenCalled();
      expect(component.department.okrMasterId)
        .toBeNull();
      done();
    }, 200);
  });

  it('clickedDeleteOKRMaster does not delete OKRMaster when dialog is not confirmed', done => {
    createComponent(cycle, department, currentUserRole);

    component.department.okrMasterId = 'testOkrMaster';

    matDialogReferenceMock.afterClosed.mockReturnValue(of(false));

    component.clickedDeleteOKRMaster();

    setTimeout(() => {
      expect(matDialogReferenceMock.afterClosed)
        .toHaveBeenCalled();
      expect(departmentMapperMock.putDepartment$)
        .toHaveBeenCalledTimes(0);
      expect(component.department.okrMasterId)
        .toBe('testOkrMaster');
      done();
    }, 200);
  });

  it('clickedDefineOKRMaster sets new userid', () => {
    createComponent(cycle, department, currentUserRole);

    component.department.okrMasterId = 'testOkrMaster';

    component.clickedDefineOKRMaster(new User('newTestOkrMaster'));

    expect(departmentMapperMock.putDepartment$)
      .toHaveBeenCalled();
    expect(component.department.okrMasterId)
      .toBe('newTestOkrMaster');
  });

  it('clickedDeleteOKRTopicSponsor deletes OKRTopicSponsor when dialog is confirmed', done => {
    createComponent(cycle, department, currentUserRole);

    component.department.okrTopicSponsorId = 'testOkrTopicSponsor';

    component.clickedDeleteOKRTopicSponsor();

    setTimeout(() => {
      expect(matDialogReferenceMock.afterClosed)
        .toHaveBeenCalled();
      expect(departmentMapperMock.putDepartment$)
        .toHaveBeenCalled();
      expect(component.department.okrTopicSponsorId)
        .toBeNull();
      done();
    }, 200);
  });

  it('clickedDeleteOKRTopicSponsor does not delete OKRTopicSponsor when dialog is not confirmed', done => {
    createComponent(cycle, department, currentUserRole);

    component.department.okrTopicSponsorId = 'testOkrTopicSponsor';

    matDialogReferenceMock.afterClosed.mockReturnValue(of(false));

    component.clickedDeleteOKRMaster();

    setTimeout(() => {
      expect(matDialogReferenceMock.afterClosed)
        .toHaveBeenCalled();
      expect(departmentMapperMock.putDepartment$)
        .toHaveBeenCalledTimes(0);
      expect(component.department.okrTopicSponsorId)
        .toBe('testOkrTopicSponsor');
      done();
    }, 200);
  });

  it('clickedDefineOKRTopicSponsor sets new userid', () => {
    createComponent(cycle, department, currentUserRole);

    component.department.okrTopicSponsorId = 'testOkrTopicSponsor';

    component.clickedDefineOKRTopicSponsor(new User('newTestOkrTopicSponsor'));

    expect(departmentMapperMock.putDepartment$)
      .toHaveBeenCalled();
    expect(component.department.okrTopicSponsorId)
      .toBe('newTestOkrTopicSponsor');
  });

  it('clickedDeleteOkrMember deletes OkrMember when dialog is confirmed', done => {
    createComponent(cycle, department, currentUserRole);

    component.department.okrMemberIds = ['testOkrMember'];

    component.clickedDeleteOKRMember('testOkrMember');

    setTimeout(() => {
      expect(matDialogReferenceMock.afterClosed)
        .toHaveBeenCalled();
      expect(departmentMapperMock.putDepartment$)
        .toHaveBeenCalled();
      expect(component.department.okrMemberIds)
        .toEqual([]);
      done();
    }, 200);
  });

  it('clickedDeleteOkrMember does not delete OkrMember when dialog is not confirmed', done => {
    createComponent(cycle, department, currentUserRole);

    component.department.okrMemberIds = ['testOkrMember'];

    matDialogReferenceMock.afterClosed.mockReturnValue(of(false));

    component.clickedDeleteOKRMember('testOkrMember');

    setTimeout(() => {
      expect(matDialogReferenceMock.afterClosed)
        .toHaveBeenCalled();
      expect(departmentMapperMock.putDepartment$)
        .toHaveBeenCalledTimes(0);
      expect(component.department.okrMemberIds)
        .toEqual(['testOkrMember']);
      done();
    }, 200);
  });

  it('clickedDefineOkrMember sets new userid', () => {
    createComponent(cycle, department, currentUserRole);

    component.department.okrMemberIds = ['testOkrMember'];

    component.clickedDefineOKRMember(new User('newTestOkrMember'));

    expect(departmentMapperMock.putDepartment$)
      .toHaveBeenCalled();
    expect(component.department.okrMemberIds)
      .toEqual(['testOkrMember', 'newTestOkrMember']);
  });
// eslint-disable-next-line max-lines
});
