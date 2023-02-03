import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { AuthenticationService } from '../../services/authentication.service';

@Injectable({
  providedIn: 'root',
})
export class NotLoggedInGuard implements CanActivate {
  constructor(
    private authenticationService: AuthenticationService,
    private router: Router,
  ) {
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean | UrlTree {
    if (this.authenticationService.hasValidAccessToken()) {
      return this.router.createUrlTree(['/']);
    } else {
      return true;
    }
  }
}
