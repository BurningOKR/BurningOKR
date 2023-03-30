import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot } from '@angular/router';
import { AuthenticationService } from '../services/authentication.service';

@Injectable({
  providedIn: 'root',
})
export class AuthGuard implements CanActivate {

  constructor(private router: Router, private authService: AuthenticationService) {

  }

  // TODO fix auth
  canActivate(next: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    console.log(`canActivate - Token: ${this.authService.getAccessToken()}`);
    if (this.authService.getAccessToken() != null && this.authService.hasValidAccessToken()) {
      console.log('canActivate: Valid Token');

      return true;
    }
    console.log('canActivate: Invalid Token');

    // this.authService.initLoginFlow();
    this.authService.login();

    return true;
  }
}
