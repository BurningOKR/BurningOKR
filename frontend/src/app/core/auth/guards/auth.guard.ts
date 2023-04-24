import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { OAuthService } from 'angular-oauth2-oidc';

@Injectable({
  providedIn: 'root',
})
export class AuthGuard implements CanActivate {

  constructor(private router: Router, private oauthService: OAuthService) {

  }

  async canActivate(next: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise<boolean | UrlTree> {
    if (this.oauthService.hasValidAccessToken()) {
      return true;
    }

    return this.oauthService.loadDiscoveryDocumentAndTryLogin().then(() => {
      const loggedIn: boolean = this.oauthService.hasValidAccessToken() && this.oauthService.hasValidIdToken();

      if (!loggedIn) {
        // store target path
        localStorage.setItem('login_redirect', state.url);
        // redirect to idp
        this.oauthService.initCodeFlow();

        return false;
      }
      // retrieve target path
      const redirect: string = localStorage.getItem('login_redirect');
      localStorage.removeItem('login_redirect');

      // redirect to target path or load content
      return (redirect != null && this.router.parseUrl(redirect)) || true;
    })
      .catch(() => {
        return false;
      });

  }
}
