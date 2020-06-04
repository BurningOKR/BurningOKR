import { TestBed } from '@angular/core/testing';

import { SubStructureContextRoleService } from './sub-structure-context-role.service';
import { CurrentUserService } from '../../../core/services/current-user.service';
import { of } from 'rxjs';
import { ContextRole } from '../../model/ui/context-role';
import { User } from '../../model/api/user';
import { DepartmentUnit } from '../../model/ui/OrganizationalUnit/department-unit';

const currentUserService: any = {
  isCurrentUserAdmin$: jest.fn(),
  getCurrentUser$: jest.fn()
};

let department: DepartmentUnit;
let service: SubStructureContextRoleService;

describe('SubStructureContextRoleService', () => {
  beforeEach(() => TestBed.configureTestingModule({
    imports: [],
    providers: [
      { provide: CurrentUserService, useValue: currentUserService }
    ]
  }));

  beforeEach(() => {
    service = TestBed.get(SubStructureContextRoleService);

    currentUserService.isCurrentUserAdmin$.mockReset();
    currentUserService.getCurrentUser$.mockReset();

    currentUserService.isCurrentUserAdmin$.mockReturnValue(of(false));
    currentUserService.getCurrentUser$.mockReturnValue(of(new User('test')));

    department = new DepartmentUnit(
      1,
      'testDepartment',
      [],
      0,
      'department',
      null, null, [],
      true, false);
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

  it('getContextRoleForDepartment, department, user has no rights, all false', done => {
    service.getContextRoleForDepartment$(department)
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

  it('getContextRoleForDepartment, department, user is Admin, all false except admin', done => {
    currentUserService.isCurrentUserAdmin$.mockReturnValue(of(true));

    service.getContextRoleForDepartment$(department)
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

  it('getContextRoleForDepartment, department, user is okr master, all false except okrManager', done => {
    department.okrMasterId = 'test';

    service.getContextRoleForDepartment$(department)
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

  it('getContextRoleForDepartment, department, user is okr topic sponsor, all false except okrManager', done => {
    department.okrTopicSponsorId = 'test';

    service.getContextRoleForDepartment$(department)
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

  it('getContextRoleForDepartment, department, user is okr topic member, all false except okrMember', done => {
    department.okrMemberIds = ['test'];

    service.getContextRoleForDepartment$(department)
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

  it('getContextRoleForDepartment, department, user is everything, all true', done => {
    currentUserService.isCurrentUserAdmin$.mockReturnValue(of(true));
    department.okrMemberIds = ['test'];
    department.okrTopicSponsorId = 'test';
    department.okrMasterId = 'test';

    service.getContextRoleForDepartment$(department)
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
});
