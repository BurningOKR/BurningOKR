// istanbul ignore file
import { Injectable } from '@angular/core';
import { ApiHttpService } from '../../../core/services/api-http.service';
import { Observable } from 'rxjs/internal/Observable';
import { AdminUser } from '../../model/api/admin-user';
import { User } from '../../model/api/user';
import { UserId } from '../../model/id-types';
import { of } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserApiService {
  constructor(private api: ApiHttpService) {}

  getUserById$(objectId: string): Observable<User> {
    return this.api.getData$(`users/${objectId}`);
  }

  getCurrentUser$(): Observable<User> {
    return this.api.getData$(`users/current`);
  }

  getUsers$(): Observable<User[]> {
    return this.api.getData$(`users`);
  }

  createLocalUser$(user: User): Observable<User> {
    return this.api.postData$('users', user);
  }

  isCurrentUserAdmin$(): Observable<boolean> {
    return this.api.getData$('admins/self');
  }

  // TODO JZ 12.07.2021 implement API Enpoint
  isCurrentUserAuditor$(): Observable<boolean> {
    return of(true);

    return this.api.getData$('auditors/self');
  }

  getAdminIds$(): Observable<string[]> {
    return this.api.getData$('admins');
  }

  addAdmin$(adminToAdd: AdminUser): Observable<User> {
    return this.api.postData$(`admins`, adminToAdd);
  }

  deleteAdmin$(adminToDeleteId: UserId): Observable<boolean> {
    return this.api.deleteData$(`admins/${adminToDeleteId}`);
  }
}
