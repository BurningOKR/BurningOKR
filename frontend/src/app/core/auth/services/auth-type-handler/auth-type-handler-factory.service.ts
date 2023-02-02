import { Injectable, Injector } from '@angular/core';
import { AuthTypeHandlerBase } from './auth-type-handler-base';
import { OAuthFrontendDetailsService } from '../o-auth-frontend-details.service';
import { map, shareReplay, take } from 'rxjs/operators';
import { Consts } from '../../../../shared/consts';
import { AzureAuthTypeHandlerService } from './azure-auth-type-handler.service';
import { LocalAuthTypeHandlerService } from './local-auth-type-handler.service';
import { DemoAuthTypeHandlerService } from './demo-auth-type-handler.service';
import { environment } from '../../../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class AuthTypeHandlerFactoryService {

  isPlayground: boolean = environment.playground;

  constructor(
    private injector: Injector,
    private oAuthFrontendDetailsService: OAuthFrontendDetailsService,
  ) {
  }

  async getAuthTypeHandler(): Promise<AuthTypeHandlerBase> {
    return this.oAuthFrontendDetailsService.getAuthType$()
      .pipe(
        take(1),
        map(authType => {

          if (authType === Consts.AUTHTYPE_AZURE) {
            return this.injector.get(AzureAuthTypeHandlerService);
          } else if (this.isPlayground) {
            return this.injector.get(DemoAuthTypeHandlerService);
          } else {
            return this.injector.get(LocalAuthTypeHandlerService);
          }
        }),
        shareReplay(),
      )
      .toPromise();
  }
}
