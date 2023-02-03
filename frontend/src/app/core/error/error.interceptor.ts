import { Injectable } from '@angular/core';
import { HttpEvent, HttpHandler, HttpHeaders, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Router } from '@angular/router';
import { NGXLogger } from 'ngx-logger';
import { OAuthService } from 'angular-oauth2-oidc';

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
  constructor(
    private logger: NGXLogger,
    private router: Router,
    private authService: OAuthService,
  ) {
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(request)
      .pipe(catchError(err => {
        if (this.authService.hasValidAccessToken()) {
          this.logger.setCustomHttpHeaders(new HttpHeaders({ Authorization: `Bearer ${this.authService.getAccessToken()}` }));
          this.logger.error(err);
        }

        return throwError(err);
      }));
  }
}
