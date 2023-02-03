import { Injectable } from '@angular/core';
import { ApiHttpService } from './api-http.service';
import { BehaviorSubject, Observable } from 'rxjs';
import { INIT_STATE_NAME, InitState } from '../../shared/model/api/init-state';
import { PostLocalAdminUserData } from '../../shared/model/api/post-local-admin-user-data';
import { OauthClientDetails } from '../../shared/model/api/oauth-client-details';
import { ErrorHandlingFunction } from './api-http-error-handling.service';
import { PostAzureAdminUserData } from '../../shared/model/api/post-azure-admin-user-data';
import { map, take, tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class InitService {

  private initialized$: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);

  constructor(
    private apiHttpService: ApiHttpService,
  ) {
  }

  isInitialized$(): Observable<boolean> {
    if (!this.initialized$.getValue()) {
      return this.getInitState$()
        .pipe(
          map((value: InitState) => {
            return value.initState === INIT_STATE_NAME.INITIALIZED;
          }),
          tap((value: boolean) => {
            if (value === true) {
              this.initialized$.next(true);
            }
          }),
          take(1),
        );
    } else {
      return this.initialized$.asObservable();
    }
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
