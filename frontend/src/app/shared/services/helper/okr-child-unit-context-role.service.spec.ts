import { TestBed } from '@angular/core/testing';

import { OkrChildUnitRoleService } from './okr-child-unit-role.service';
import { CurrentUserService } from '../../../core/services/current-user.service';
import { of } from 'rxjs';
import { ContextRole } from '../../model/ui/context-role';
import { User } from '../../model/api/user';
import { OkrDepartment } from '../../model/ui/OrganizationalUnit/okr-department';
import { OkrBranch } from '../../model/ui/OrganizationalUnit/okr-branch';
import { UnitType } from '../../model/api/OkrUnit/unit-type.enum';

const currentUserService: any = {
  isCurrentUserAdmin$: jest.fn(),
  getCurrentUser$: jest.fn(),
};

let department: OkrDepartment;
let okrBranch: OkrBranch;
let service: OkrChildUnitRoleService;

describe('OkrChildUnitRoleService', () => {
  beforeEach(() => TestBed.configureTestingModule({
    imports: [],
    providers: [
      { provide: CurrentUserService, useValue: currentUserService },
    ],
  }));

  beforeEach(() => {
    service = TestBed.inject(OkrChildUnitRoleService);

    currentUserService.isCurrentUserAdmin$.mockReset();
    currentUserService.getCurrentUser$.mockReset();

    currentUserService.isCurrentUserAdmin$.mockReturnValue(of(false));
    currentUserService.getCurrentUser$.mockReturnValue(of(new User('test')));

    department = {
      id: 1,
      name: 'testDepartment',
      photo: 'base64',
      objectives: [],
      parentUnitId: 0,
      label: 'department',
      okrMasterId: null,
      okrTopicSponsorId: null,
      okrMemberIds: [],
      isActive: true,
      isParentUnitABranch: false,
      type: UnitType.DEPARTMENT,
    };

    okrBranch = {
      type: UnitType.BRANCH,
      id: 2,
      name: 'testBranch',
      photo: 'base64',
      objectives: [],
      label: 'testLAbel',
      parentUnitId: 0,
      okrChildUnitIds: [],
      isActive: true,
      isParentUnitABranch: false,
    };
  });

  it('should be created', () => {
    expect(service)
      .toBeTruthy();
  });

  it('getRoleWithoutContext not admin, all false', done => {
    currentUserService.isCurrentUserAdmin$.mockReturnValue(of(false));

    service.getRoleWithoutContext$()
      .subscribe((contextRole: ContextRole) => {
        expect(contextRole.isAdmin)
          .toBeFalsy();
        expect(contextRole.isOKRManager)
          .toBeFalsy();
        expect(contextRole.isOKRMember)
          .toBeFalsy();

        done();
      });
  });

  it('getRoleWithoutContext is admin, all false except admin', done => {
    currentUserService.isCurrentUserAdmin$.mockReturnValue(of(true));

    service.getRoleWithoutContext$()
      .subscribe((contextRole: ContextRole) => {
        expect(contextRole.isAdmin)
          .toBeTruthy();
        expect(contextRole.isOKRManager)
          .toBeFalsy();
        expect(contextRole.isOKRMember)
          .toBeFalsy();

        done();
      });
  });

  it('getContextRoleForChildUnits, ChildUnit, user has no rights, all false', done => {
    service.getContextRoleForOkrChildUnit$(department)
      .subscribe((contextRole: ContextRole) => {
        expect(contextRole.isAdmin)
          .toBeFalsy();
        expect(contextRole.isOKRManager)
          .toBeFalsy();
        expect(contextRole.isOKRMember)
          .toBeFalsy();

        done();
      });
  });

  it('getContextRoleForOkrChildUnit$, OkrChildUnitDto, user is Admin, all false except admin', done => {
    currentUserService.isCurrentUserAdmin$.mockReturnValue(of(true));

    service.getContextRoleForOkrChildUnit$(department)
      .subscribe((contextRole: ContextRole) => {
        expect(contextRole.isAdmin)
          .toBeTruthy();
        expect(contextRole.isOKRManager)
          .toBeFalsy();
        expect(contextRole.isOKRMember)
          .toBeFalsy();

        done();
      });
  });

  it('getContextRoleForOkrChildUnit$, OkrChildUnitDto, user is okr master, all false except okrManager', done => {
    department.okrMasterId = 'test';

    service.getContextRoleForOkrChildUnit$(department)
      .subscribe((contextRole: ContextRole) => {
        expect(contextRole.isAdmin)
          .toBeFalsy();
        expect(contextRole.isOKRManager)
          .toBeTruthy();
        expect(contextRole.isOKRMember)
          .toBeFalsy();

        done();
      });
  });

  it(
    'getContextRoleForOkrChildUnit$, OkrChildUnitDto, user is okr topic sponsor, all false except okrManager',
    done => {
      department.okrTopicSponsorId = 'test';

      service.getContextRoleForOkrChildUnit$(department)
        .subscribe((contextRole: ContextRole) => {
          expect(contextRole.isAdmin)
            .toBeFalsy();
          expect(contextRole.isOKRManager)
            .toBeTruthy();
          expect(contextRole.isOKRMember)
            .toBeFalsy();

          done();
        });
    },
  );

  it('getContextRoleForOkrChildUnit$, OkrChildUnitDto, user is okr topic member, all false except okrMember', done => {
    department.okrMemberIds = ['test'];

    service.getContextRoleForOkrChildUnit$(department)
      .subscribe((contextRole: ContextRole) => {
        expect(contextRole.isAdmin)
          .toBeFalsy();
        expect(contextRole.isOKRManager)
          .toBeFalsy();
        expect(contextRole.isOKRMember)
          .toBeTruthy();

        done();
      });
  });

  it('getContextRoleForOkrChildUnit$, OkrChildUnitDto, user is everything, all true', done => {
    currentUserService.isCurrentUserAdmin$.mockReturnValue(of(true));
    department.okrMemberIds = ['test'];
    department.okrTopicSponsorId = 'test';
    department.okrMasterId = 'test';

    service.getContextRoleForOkrChildUnit$(department)
      .subscribe((contextRole: ContextRole) => {
        expect(contextRole.isAdmin)
          .toBeTruthy();
        expect(contextRole.isOKRManager)
          .toBeTruthy();
        expect(contextRole.isOKRMember)
          .toBeTruthy();

        done();
      });
  });

  it('getContextRoleForOkrChildUnit$, OkrBranch, user is Admin, all false except admin', done => {
    currentUserService.isCurrentUserAdmin$.mockReturnValue(of(true));

    service.getContextRoleForOkrChildUnit$(okrBranch)
      .subscribe((contextRole: ContextRole) => {
        expect(contextRole.isAdmin)
          .toBeTruthy();
        expect(contextRole.isOKRManager)
          .toBeFalsy();
        expect(contextRole.isOKRMember)
          .toBeFalsy();

        done();
      });
  });

  it('getContextRoleForOkrChildUnit$, OkrBranch, user has no rights, all false', done => {
    service.getContextRoleForOkrChildUnit$(okrBranch)
      .subscribe((contextRole: ContextRole) => {
        expect(contextRole.isAdmin)
          .toBeFalsy();
        expect(contextRole.isOKRManager)
          .toBeFalsy();
        expect(contextRole.isOKRMember)
          .toBeFalsy();

        done();
      });
  });
});
