// istanbul ignore file
import { Injectable } from '@angular/core';
import { ApiHttpService } from '../../../core/services/api-http.service';
import { Observable } from 'rxjs/internal/Observable';
import { User } from '../../model/api/user';
import { UserId } from '../../model/id-types';

@Injectable({
  providedIn: 'root'
})
export class LocalUserApiService {

  constructor(private api: ApiHttpService) {
  }

  getUserById$(objectId: string): Observable<User> {
    return this.api.getData$(`local-users/${objectId}`);
  }

  getCurrentUser$(): Observable<User> {
    return this.api.getData$(`users/current`);
  }

  getUsers$(): Observable<User[]> {
    return this.api.getData$(`local-users`);
  }

  createUser$(user: User): Observable<User> {
    return this.api.postData$('local-users', user);
  }

  bulkCreateUsers$(users: User[]): Observable<User[]> {
    return this.api.postData$('local-users/bulk', users);
  }

  putUser$(user: User): Observable<User> {
    return this.api.putData$(`local-users/${user.id}`, user);
  }

  isCurrentUserAdmin$(): Observable<boolean> {
    return this.api.getData$('admins/self');
  }

  getAdminIds$(): Observable<string[]> {
    return this.api.getData$('admins');
  }

  addAdmin$(adminToAdd: User): Observable<User> {
    return this.api.postData$(`admins`, adminToAdd);
  }

  deleteAdmin$(adminToDeleteId: UserId): Observable<boolean> {
    return this.api.deleteData$(`admins/${adminToDeleteId}`);
  }

  deactivateUser$(id: UserId): Observable<boolean> {
    return this.api.deleteData$(`local-users/${id}`);
  }
}
