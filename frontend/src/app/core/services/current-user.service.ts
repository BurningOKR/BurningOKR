import { Injectable } from '@angular/core';
import { ReplaySubject, Observable } from 'rxjs';
import { UserApiService } from '../../shared/services/api/user-api.service';
import { take } from 'rxjs/operators';
import { OAuthService } from 'angular-oauth2-oidc';
import { User } from '../../shared/model/api/user';
import { Fetchable } from '../../shared/decorators/fetchable.decorator';

@Fetchable()
@Injectable({
  providedIn: 'root'
})
export class CurrentUserService implements Fetchable {
  private isAdmin$: ReplaySubject<boolean> = new ReplaySubject<boolean>(1);
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
  }
}
