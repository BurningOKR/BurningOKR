import { Injectable } from '@angular/core';
import { map, take } from 'rxjs/operators';
import { CurrentUserService } from '../../../core/services/current-user.service';
import { DepartmentUnit } from '../../model/ui/OrganizationalUnit/department-unit';
import { User } from '../../model/api/user';
import { combineLatest, Observable } from 'rxjs';

export class ContextRole {
  isAdmin = false;
  isOKRManager = false;
  isOKRMember = false;

  isAtleastAdmin(): boolean {
    return this.isAdmin;
  }

  isAtleastOKRManager(): boolean {
    return this.isAdmin || this.isOKRManager;
  }

  isAtleastOKRMember(): boolean {
    return this.isAdmin || this.isOKRManager || this.isOKRMember;
  }
}

@Injectable({
  providedIn: 'root'
})
export class DepartmentContextRoleService {
  constructor(private currentUserService: CurrentUserService) {}

  getContextRoleForDepartment$(department: DepartmentUnit): Observable<ContextRole> {
    return combineLatest([
      this.currentUserService.isCurrentUserAdmin$(),
      this.currentUserService.getCurrentUser$()
    ])
      .pipe(
        take(1),
        map(([isAdmin, currentUser]: [boolean, User]) => {
          const role: ContextRole = new ContextRole();

          role.isAdmin = isAdmin;
          role.isOKRManager = department.okrMasterId === currentUser.id || department.okrTopicSponsorId === currentUser.id;
          role.isOKRMember = department.okrMemberIds.Contains(currentUser.id);

          return role;
        })
      );
  }

  getRoleWithoutContext$(): Observable<ContextRole> {
    return this.currentUserService
      .isCurrentUserAdmin$()
      .pipe(
        take(1),
        map((isAdmin: boolean) => {
          const role: ContextRole = new ContextRole();
          role.isAdmin = isAdmin;

          return role;
        })
      );
  }
}
