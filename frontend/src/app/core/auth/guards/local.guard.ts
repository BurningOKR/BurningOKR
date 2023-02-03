import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { OAuthFrontendDetailsService } from '../services/o-auth-frontend-details.service';
import { map, take } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class LocalGuard implements CanActivate {

  constructor(
    private router: Router,
    private oAuthDetails: OAuthFrontendDetailsService,
  ) {
  }

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot,
  ): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    return this.oAuthDetails.isLocalAuthType$()
      .pipe(
        take(1),
        map(isLocal => {
          if (isLocal) {
            return true;
          } else {
            return this.router.createUrlTree(['']);
          }
        }),
      );
  }
}
