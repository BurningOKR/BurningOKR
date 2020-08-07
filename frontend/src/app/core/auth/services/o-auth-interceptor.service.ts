import { Injectable, Injector } from '@angular/core';
import { HttpEvent, HttpHandler, HttpHeaders, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthConfig, OAuthResourceServerErrorHandler, OAuthStorage } from 'angular-oauth2-oidc';
import { catchError, switchMap, take } from 'rxjs/operators';
import { OAuthFrontendDetailsService } from './o-auth-frontend-details.service';

@Injectable()
export class OAuthInterceptorService implements HttpInterceptor {

  constructor(private authStorage: OAuthStorage,
              private errorHandler: OAuthResourceServerErrorHandler,
              private injector: Injector) {

  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (req.url === '/api/oAuthFrontendDetails') {
      return next.handle(req)
        .pipe(catchError(error => this.errorHandler.handleError(error)));
    }

    const oauthFrontendDetails: OAuthFrontendDetailsService = this.injector.get(OAuthFrontendDetailsService);

    return oauthFrontendDetails.getAuthConfig$()
      .pipe(
        take(1),
        switchMap(authConfig => {
          req.url.toLowerCase();

          let headers: HttpHeaders;
          if (!!req.headers.get('Authorization')) {
            headers = this.setBasicAuthorizationHeader(req, authConfig);
          } else {
            headers = this.setBearerAuthorizationHeader(req, authConfig);
          }

          return next.handle(req.clone({headers}))
            .pipe(catchError(error => this.errorHandler.handleError(error)));
        })
      );
  }

  private setBasicAuthorizationHeader(req: HttpRequest<any>, authConfig: AuthConfig): HttpHeaders {
    const authorizationString: string = btoa(`${authConfig.clientId}:${authConfig.dummyClientSecret}`);
    const header: string = `Basic ${authorizationString}`;

    return req.headers.set('Authorization', header);
  }

  private setBearerAuthorizationHeader(req: HttpRequest<any>, authConfig: AuthConfig): HttpHeaders {
    const token: string = localStorage.getItem('access_token');
    const header: string = `Bearer ${token}`;

    return req.headers.set('Authorization', header);
  }

}
