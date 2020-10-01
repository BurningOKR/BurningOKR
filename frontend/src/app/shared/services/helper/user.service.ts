import { Injectable } from '@angular/core';
import { UserApiService } from '../api/user-api.service';
import { Observable } from 'rxjs/internal/Observable';
import { User } from '../../model/api/user';
import { map, take } from 'rxjs/operators';
import { BehaviorSubject } from 'rxjs';
import { AdminUser } from '../../model/api/admin-user';
import { IUserService } from './i-user-service';

@Injectable({
  providedIn: 'root'
})
export class UserService implements IUserService {
  constructor(protected userApiService: UserApiService) {
  }

  private users$: BehaviorSubject<User[]>;

  getUserById$(objectId: string): Observable<User> {
    if (this.users$) {
      return this.users$.pipe(
        map((users: User[]) => {
          const index: number = users.findIndex(u => u.id === objectId);

          return index !== -1 ? users[index] : undefined;
        })
      );
    } else {
      return this.userApiService.getUserById$(objectId)
        .pipe(
          map((user: User) => {
            return user;
          })
        );
    }
  }

  getAllUsers$(): Observable<User[]> {
    if (this.users$) {
      return this.users$.asObservable();
    } else {
      this.updateUserCache();

      return this.users$;
    }
  }

  updateUserCache(): void {
    if (!this.users$) {
      this.users$ = new BehaviorSubject<User[]>([]);
    }

    this.userApiService
      .getUsers$()
      .pipe(take(1))
      .subscribe(u => this.users$.next(u));
  }

  addAdmin$(adminToAdd: User): Observable<User> {
    return this.userApiService.addAdmin$(
      new AdminUser(adminToAdd.id)
    );
  }

  deleteAdmin$(adminToDeleteId: string): Observable<boolean> {
    return this.userApiService.deleteAdmin$(adminToDeleteId);
  }

  getUsers$(): Observable<User[]> {
    return this.userApiService.getUsers$();
  }

  getAdminIds$(): Observable<string[]> {
    return this.userApiService.getAdminIds$();
  }
}
