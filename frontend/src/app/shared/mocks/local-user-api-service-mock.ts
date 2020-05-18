import { Observable } from 'rxjs/internal/Observable';
import { User } from '../model/api/user';
import { UserId } from '../model/id-types';
import { of } from 'rxjs';

export class LocalUserApiServiceMock {
  getUserById$(objectId: string): Observable<User> {
    return of();
  }

  getCurrentUser$(): Observable<User> {
    return of();
  }

  getUsers$(): Observable<User[]> {
    return of();
  }

  createUser$(user: User): Observable<User> {
    return of();
  }

  bulkCreateUsers$(users: User[]): Observable<User[]> {
    return of();
  }

  putUser$(user: User): Observable<User> {
    return of();
  }

  isCurrentUserAdmin$(): Observable<boolean> {
    return of();
  }

  getAdmins$(): Observable<string[]> {
    return of();
  }

  addAdmin$(adminToAdd: User): Observable<User> {
    return of();
  }

  deleteAdmin$(adminToDeleteId: UserId): Observable<boolean> {
    return of();
  }

  deactivateUser$(id: UserId): Observable<boolean> {
    return of();
  }
}
