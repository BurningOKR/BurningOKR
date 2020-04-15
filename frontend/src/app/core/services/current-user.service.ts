import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
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
  private isAdmin$: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);

  constructor(private oAuthService: OAuthService,
              private userApiService: UserApiService) {
  }

  getCurrentUser(): Observable<User> {
    return this.userApiService.getCurrentUser();
  }

  isCurrentUserAdmin(): Observable<boolean> {
    return this.isAdmin$.asObservable();
  }

  fetchData(): void {
    this.userApiService.isCurrentUserAdmin$()
      .pipe(take(1))
      .subscribe((isAdmin: boolean) => {
        this.isAdmin$.next(isAdmin);
      });
  }
}
