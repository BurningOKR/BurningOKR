import { Injectable } from '@angular/core';
import { ApiHttpService } from './api-http.service';
import { Observable } from 'rxjs';
import { InitState } from '../../shared/model/api/init-state';
import { PostLocalAdminUserData } from '../../shared/model/api/post-local-admin-user-data';
import { OauthClientDetails } from '../../shared/model/api/oauth-client-details';
import { ErrorHandlingFunction } from './api-http-error-handling.service';
import { PostAzureAdminUserData } from '../../shared/model/api/post-azure-admin-user-data';

@Injectable({
  providedIn: 'root'
})
export class InitService {

  constructor(
    private apiHttpService: ApiHttpService,
  ) {
  }

  getInitState$(customErrorHandler?: ErrorHandlingFunction<InitState>): Observable<InitState> {
    return this.apiHttpService.getData$('init', customErrorHandler);
  }

  postLocalAdminUser$(data: PostLocalAdminUserData): Observable<InitState> {
    return this.apiHttpService.postData$('init/admin-account', data);
  }

  postAzureAdminUser$(data: PostAzureAdminUserData): Observable<InitState> {
    return this.apiHttpService.postData$('init/azure-admin-user', data);
  }

  postOauthClientDetails$(data: OauthClientDetails): Observable<InitState> {
    return this.apiHttpService.postData$('init/oauth-server', data);
  }
}
