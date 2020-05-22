import { Injectable } from '@angular/core';
import { UserApiService } from '../api/user-api.service';
import { Observable } from 'rxjs/internal/Observable';
import { User } from '../../model/api/user';
import { map } from 'rxjs/operators';
import { BehaviorSubject } from 'rxjs';
import { AdminUser } from '../../model/api/admin-user';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  constructor(private userApiService: UserApiService) {
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
      this.users$ = new BehaviorSubject<User[]>([]);
      this.userApiService
        .getUsers$()
        .subscribe(u => this.users$.next(u));

      return this.users$;
    }
  }

  addAdmin$(adminToAdd: User): Observable<User> {
    return this.userApiService.addAdmin$(
      new AdminUser(adminToAdd.id)
    );
  }

  deleteAdmin$(adminToDeleteId: string): Observable<boolean> {
    return this.userApiService.deleteAdmin$(adminToDeleteId);
  }
}
