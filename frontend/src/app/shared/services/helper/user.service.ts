import { Injectable } from '@angular/core';
import { UserApiService } from '../api/user-api.service';
import { Observable } from 'rxjs/internal/Observable';
import { User } from '../../model/api/user';
import { filter, map, take } from 'rxjs/operators';
import { BehaviorSubject, forkJoin } from 'rxjs';
import { AdminUser } from '../../model/api/admin-user';
import { IUserService } from './i-user-service';

@Injectable({
  providedIn: 'root'
})
export class UserService implements IUserService {

  private users$: BehaviorSubject<User[]>;

  constructor(private userApiService: UserApiService) {
  }

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

  getAllActiveUsers$(): Observable<User[]> {
    return this.getAllUsers$().pipe(map( users => users.filter(user => user.active)));
  }


  updateUserCache(): void {
    if (!this.users$) {
      this.users$ = new BehaviorSubject<User[]>([]);
    }
    forkJoin({
      activeUsers: this.userApiService.getActiveUsers$(),
      users: this.userApiService.getUsers$()
    }).pipe(map (({ activeUsers, users}) => {
        users.forEach(user => user.active = !!activeUsers.find(activeUser => activeUser.id === user.id))
        return users;
    }))
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
