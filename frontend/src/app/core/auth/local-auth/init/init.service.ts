import { Injectable } from '@angular/core';
import { ApiHttpService } from '../../../services/api-http.service';
import { Observable } from 'rxjs';
import { InitState } from './init-state';
import { PostAdminUserData } from '../../../../shared/model/api/post-admin-user-data';
import { OauthClientDetails } from '../../../../shared/model/api/oauth-client-details';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Consts } from '../../../../shared/consts';

@Injectable({
  providedIn: 'root'
})
export class InitService {

  constructor(
    private apiHttpService: ApiHttpService,
    private httpClient: HttpClient // TODO: delete me once error handling was changed, to not show snackbars /TG, 30.03.2020
  ) {
  }

  getInitState$(): Observable<InitState> {
    return this.apiHttpService.getData$('init');
  }

  getInitStateAndBypassErrorHandling$(): Observable<InitState> { // TODO: delete me once error handling was changed, to not show snackbars /TG, 30.03.2020
    return this.httpClient.get<InitState>(`${Consts.API_URL}init`, {
      headers: new HttpHeaders({})
    });
  }

  postAdminUser$(data: PostAdminUserData): Observable<InitState> {
    return this.apiHttpService.postData$('init/admin-account', data);
  }

  postOauthClientDetails$(data: OauthClientDetails): Observable<InitState> {
    return this.apiHttpService.postData$('init/oauth-server', data);
  }
}
