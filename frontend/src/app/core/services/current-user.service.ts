import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { UserApiService } from '../../shared/services/api/user-api.service';
import { shareReplay } from 'rxjs/operators';
import { OAuthService } from 'angular-oauth2-oidc';
import { User } from '../../shared/model/api/user';
import { Fetchable } from '../../shared/decorators/fetchable.decorator';

@Fetchable()
@Injectable({
  providedIn: 'root'
})
export class CurrentUserService implements Fetchable {
  private isAdmin$: Observable<boolean>;

  constructor(private oAuthService: OAuthService,
              private userApiService: UserApiService) {
  }

  getCurrentUser(): Observable<User> {
    return this.userApiService.getCurrentUser();
  }

  isCurrentUserAdmin(): Observable<boolean> {
    return this.isAdmin$;
  }

  fetchData(): void {
    this.isAdmin$ = this.userApiService.isCurrentUserAdmin$()
      .pipe(shareReplay(1));
  }
}
