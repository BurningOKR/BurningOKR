import { Injectable } from '@angular/core';
import { Observable, ReplaySubject } from 'rxjs';
import { UserApiService } from '../../shared/services/api/user-api.service';
import { take } from 'rxjs/operators';
import { OAuthService } from 'angular-oauth2-oidc';
import { User } from '../../shared/model/api/user';
import { Fetchable } from '../../shared/decorators/fetchable.decorator';
import { map } from 'rxjs/internal/operators';
import { UserId } from '../../shared/model/id-types';

@Fetchable()
@Injectable({
  providedIn: 'root',
})
export class CurrentUserService implements Fetchable {
  private isAdmin$: ReplaySubject<boolean> = new ReplaySubject<boolean>(1);
  private currentUser$: ReplaySubject<User> = new ReplaySubject<User>(1);

  constructor(
    private oAuthService: OAuthService,
    private userApiService: UserApiService,
  ) {
  }

  getCurrentUser$(): Observable<User> {
    return this.currentUser$.asObservable();
  }

  getCurrentUserId$(): Observable<UserId> {
    return this.getCurrentUser$()
      .pipe(
        map((user: User) => {
          return user.id;
        }),
      );
  }

  isCurrentUserAdmin$(): Observable<boolean> {
    return this.currentUser$.pipe(
      map(user => user.admin),
    );
  }

  fetchData(): void {
    this.userApiService.getCurrentUser$()
      .pipe(take(1))
      .subscribe((receivedUser: User) => {
        this.currentUser$.next(receivedUser);
        this.isAdmin$.next(receivedUser.admin);
      });
  }
}
