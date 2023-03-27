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
    // if (this.authService.hasValidAccessToken()) {
    return true;
    //}

    // return this.authService.redirectToLoginProvider();
  }
}
