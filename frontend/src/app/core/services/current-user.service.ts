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
  private isAuditor$: ReplaySubject<boolean> = new ReplaySubject<boolean>(1);
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
    return this.isAdmin$.asObservable();
  }

  isCurrentUserAuditor$(): Observable<boolean> {
    return this.isAuditor$.asObservable();
  }

  fetchData(): void {
    this.userApiService.getCurrentUser$()
      .pipe(take(1))
      .subscribe((reveived: User) => {
        this.currentUser$.next(reveived);
      });
    this.userApiService.isCurrentUserAdmin$()
      .pipe(take(1))
      .subscribe((isAdmin: boolean) => {
        this.isAdmin$.next(isAdmin);
      });
    this.userApiService.isCurrentUserAuditor$()
      .pipe(take(1))
      .subscribe((isAuditor: boolean) => {
        this.isAuditor$.next(isAuditor);
      });
  }
}
