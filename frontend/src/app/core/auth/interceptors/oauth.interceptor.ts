import { Injectable, Injector } from '@angular/core';
import { HttpEvent, HttpHandler, HttpHeaders, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthenticationService } from '../services/authentication.service';

@Injectable()
export class OauthInterceptor implements HttpInterceptor {

  constructor(private injector: Injector) {
  }

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    let extendedRequest: HttpRequest<unknown> = request;
    const authenticationService: AuthenticationService = this.injector.get(AuthenticationService);

    if (authenticationService.hasValidAccessToken() && request.url.startsWith('/api')) {
      const reqHeaders: HttpHeaders = request.headers.set(
          'Authorization',
          `Bearer ${authenticationService.getAccessToken()}`,
      );
      extendedRequest = request.clone({ headers: reqHeaders });
    }

    return next.handle(extendedRequest);
  }
}
