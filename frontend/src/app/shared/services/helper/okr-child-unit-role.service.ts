import { Injectable } from '@angular/core';
import { map, take } from 'rxjs/operators';
import { CurrentUserService } from '../../../core/services/current-user.service';
import { OkrDepartment } from '../../model/ui/OrganizationalUnit/okr-department';
import { User } from '../../model/api/user';
import { combineLatest, Observable } from 'rxjs';
import { ContextRole } from '../../model/ui/context-role';
import { OkrChildUnit } from '../../model/ui/OrganizationalUnit/okr-child-unit';
import 'linq4js';

@Injectable({
  providedIn: 'root'
})
export class OkrChildUnitRoleService {
  constructor(private currentUserService: CurrentUserService) {}

  getContextRoleForOkrChildUnit$(okrChildUnit: OkrChildUnit): Observable<ContextRole> {
    return combineLatest([
      this.currentUserService.isCurrentUserAdmin$(),
      this.currentUserService.getCurrentUser$()
    ])
      .pipe(
        take(1),
        map(([isAdmin, currentUser]: [boolean, User]) => {
          const role: ContextRole = new ContextRole();

          role.isAdmin = isAdmin;
          if (okrChildUnit instanceof OkrDepartment) {
            role.isOKRManager = okrChildUnit.okrMasterId === currentUser.id || okrChildUnit.okrTopicSponsorId === currentUser.id;
            role.isOKRMember = okrChildUnit.okrMemberIds.Contains(currentUser.id);
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
