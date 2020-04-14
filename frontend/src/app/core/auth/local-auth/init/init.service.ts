import { Injectable } from '@angular/core';
import { ApiHttpService } from '../../../services/api-http.service';
import { Observable } from 'rxjs';
import { InitState } from './init-state';
import { PostAdminUserData } from '../../../../shared/model/api/post-admin-user-data';
import { OauthClientDetails } from '../../../../shared/model/api/oauth-client-details';
import { ErrorHandlingFunction } from '../../../services/api-http-error-handling.service';

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

  postAdminUser$(data: PostAdminUserData): Observable<InitState> {
    return this.apiHttpService.postData$('init/admin-account', data);
  }

  postOauthClientDetails$(data: OauthClientDetails): Observable<InitState> {
    return this.apiHttpService.postData$('init/oauth-server', data);
  }
}
