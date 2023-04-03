import { Injectable } from '@angular/core';
import { HttpEvent, HttpHandler, HttpHeaders, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthenticationService } from '../services/authentication.service';

@Injectable()
export class OauthInterceptor implements HttpInterceptor {

  constructor(private authenticationService: AuthenticationService) {
  }

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    let extendedRequest: HttpRequest<unknown> = request;

    if (this.authenticationService.hasValidAccessToken() && request.url.startsWith('/api')) {
      const reqHeaders: HttpHeaders = request.headers.set(
          'Authorization',
          `Bearer ${this.authenticationService.getAccessToken()}`,
      );
      extendedRequest = request.clone({ headers: reqHeaders });
    }

    return next.handle(extendedRequest);
  }
}
