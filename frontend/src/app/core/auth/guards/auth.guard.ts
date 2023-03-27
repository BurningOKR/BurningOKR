import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot } from '@angular/router';
import { OAuthService } from 'angular-oauth2-oidc';

@Injectable({
  providedIn: 'root',
})
export class AuthGuard implements CanActivate {

  constructor(private router: Router, private authService: OAuthService) {

  }

  // TODO fix auth
  canActivate(next: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    console.log(this.authService.getAccessToken());
    if (this.authService.hasValidAccessToken()) {
      console.log('Valid Token');

      return true;
    }
    console.log('Invalid Token');

    this.authService.initLoginFlow();
  }
}
