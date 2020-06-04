import { Injectable } from '@angular/core';
import { map, take } from 'rxjs/operators';
import { CurrentUserService } from '../../../core/services/current-user.service';
import { DepartmentUnit } from '../../model/ui/OrganizationalUnit/department-unit';
import { User } from '../../model/api/user';
import { combineLatest, Observable } from 'rxjs';
import { ContextRole } from '../../model/ui/context-role';
import { SubStructure } from '../../model/ui/OrganizationalUnit/sub-structure';

@Injectable({
  providedIn: 'root'
})
export class SubStructureContextRoleService {
  constructor(private currentUserService: CurrentUserService) {}

  getContextRoleForSubStructure$(subStructure: SubStructure): Observable<ContextRole> {
    return combineLatest([
      this.currentUserService.isCurrentUserAdmin$(),
      this.currentUserService.getCurrentUser$()
    ])
      .pipe(
        take(1),
        map(([isAdmin, currentUser]: [boolean, User]) => {
          const role: ContextRole = new ContextRole();

          role.isAdmin = isAdmin;
          if (subStructure instanceof DepartmentUnit) {
            role.isOKRManager = subStructure.okrMasterId === currentUser.id || subStructure.okrTopicSponsorId === currentUser.id;
            role.isOKRMember = subStructure.okrMemberIds.Contains(currentUser.id);
          }

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
