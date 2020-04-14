import { Injectable, Injector } from '@angular/core';
import { Fetchable, FetchingServiceData } from '../../shared/decorators/fetchable.decorator';
import { TypeOf } from '../../../typings';
import { OAuthService } from 'angular-oauth2-oidc';

@Injectable({
  providedIn: 'root'
})
export class FetchingService {

  static fetchingServices: FetchingServiceData[] = [];

  constructor(private injector: Injector,
              private oAuthService: OAuthService) {
  }

  refetchAll(): void {
    FetchingService.fetchingServices.forEach((data: FetchingServiceData) => {
      if (!data.loginRequired || this.oAuthService.hasValidAccessToken()) {
        const service: Fetchable = this.injector.get(data.service);
        service.fetchData();
      }
    });
  }

  refetchSingle(fetchableService: TypeOf<Fetchable>): void {
    const service: Fetchable = this.injector.get(fetchableService);
    service.fetchData();
  }
}
