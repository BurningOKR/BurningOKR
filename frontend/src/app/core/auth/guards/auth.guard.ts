import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { OAuthService } from 'angular-oauth2-oidc';
import { AuthenticationService } from '../services/authentication.service';

@Injectable({
  providedIn: 'root',
})
export class AuthGuard implements CanActivate {

  constructor(private router: Router, private oauthService: OAuthService, private authService: AuthenticationService) {

  }

  async canActivate(next: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise<boolean | UrlTree> {

    if (this.authService.isInitialized() || await this.authService.waitForInitializationToFinish()) {
      const access: string | boolean = await this.authService.login(state.url);

      if (typeof access === 'string') {
        return this.router.parseUrl(access);
      } else {
        return access;
      }
    }

  }
}
