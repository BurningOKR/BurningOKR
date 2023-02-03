import { Injectable } from '@angular/core';
import { UserService } from './user.service';
import { LocalUserApiService } from '../api/local-user-api.service';
import { IUserService } from './i-user-service';
import { User } from '../../model/api/user';
import { Observable } from 'rxjs';
import { finalize, take } from 'rxjs/operators';
import { UserId } from '../../model/id-types';
import { FetchingService } from '../../../core/services/fetching.service';
import { CurrentUserService } from '../../../core/services/current-user.service';

@Injectable({
  providedIn: 'root',
})
export class LocalUserService implements IUserService {

  constructor(
    private localUserApiService: LocalUserApiService,
    private userService: UserService,
    private fetchingService: FetchingService,
  ) {
  }

  addAdmin$(adminToAdd: User): Observable<User> {
    return this.userService.addAdmin$(adminToAdd);
  }

  deleteAdmin$(adminToDeleteId: string): Observable<boolean> {
    return this.userService.deleteAdmin$(adminToDeleteId);
  }

  getAdminIds$(): Observable<string[]> {
    return this.userService.getAdminIds$();
  }

  getUserById$(objectId: string): Observable<User> {
    return this.localUserApiService.getUserById$(objectId);
  }

  getUsers$(): Observable<User[]> {
    return this.localUserApiService.getUsers$();
  }

  createUser$(user: User): Observable<User> {
    return this.localUserApiService.createUser$(user)
      .pipe(
        take(1),
        finalize(() => this.updateServices()),
      );
  }

  bulkCreateUsers$(users: User[]): Observable<User[]> {
    return this.localUserApiService.bulkCreateUsers$(users)
      .pipe(
        take(1),
        finalize(() => this.updateServices()),
      );
  }

  updateUser$(user: User): Observable<User> {
    return this.localUserApiService.putUser$(user)
      .pipe(
        take(1),
        finalize(() => this.updateServices()),
      );
  }

  deactivateUser$(id: UserId): Observable<boolean> {
    return this.localUserApiService.deactivateUser$(id)
      .pipe(
        take(1),
        finalize(() => this.updateServices()),
      );
  }

  private updateServices(): void {
    this.userService.updateUserCache();
    this.fetchingService.refetchSingle(CurrentUserService);
  }
}
