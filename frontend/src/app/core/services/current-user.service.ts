import { Injectable } from '@angular/core';
import { ReplaySubject, Observable, of } from 'rxjs';
import { UserApiService } from '../../shared/services/api/user-api.service';
import { shareReplay, switchMap, take } from 'rxjs/operators';
import { OAuthService } from 'angular-oauth2-oidc';
import { User } from '../../shared/model/api/user';
import { Fetchable } from '../../shared/decorators/fetchable.decorator';
import { map } from 'rxjs/internal/operators';

@Fetchable()
@Injectable({
  providedIn: 'root'
})
export class CurrentUserService implements Fetchable {
  private isAdmin$: ReplaySubject<boolean> = new ReplaySubject<boolean>(1);
  private isAuditor$: ReplaySubject<boolean> = new ReplaySubject<boolean>(1);
  private currentUser$: ReplaySubject<User> = new ReplaySubject<User>(1);

  constructor(private oAuthService: OAuthService,
              private userApiService: UserApiService) {
  }

  getCurrentUser$(): Observable<User> {
    return this.currentUser$.asObservable();
  }

  isCurrentUserAdmin$(): Observable<boolean> {
    return this.isAdmin$.asObservable();
  }

  isCurrentUserAuditor$(): Observable<boolean> {
    return this.isAuditor$.asObservable();
  }

  isCurrentUserCreator$(initiatorId: string): Observable<boolean> {
    return this.getCurrentUser$()
      .pipe(
        switchMap((currentUser: User) => {
          return of(currentUser.id === initiatorId);
        })
      );
  }

  isCurrentUserAdminOrCreator$(initiatorId: string): Observable<boolean> {
    return this.isCurrentUserCreator$(initiatorId)
      .pipe(
        switchMap((isCreator: boolean) => {
          if (isCreator) {
            return of(isCreator);
          } else {
            return this.isCurrentUserAdmin$();
          }
        })
      );
  }

  isCurrentUserAdminOrAuditor$(): Observable<boolean> {
    return this.isCurrentUserAdmin$()
      .pipe(
        switchMap((isAdmin: boolean) => {
          if (isAdmin) {
            return of(isAdmin);
          } else {
            return this.isCurrentUserAuditor$();
          }
        })
      );
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
