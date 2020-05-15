import { ErrorHandlingFunction } from '../../core/services/api-http-error-handling.service';
import { INIT_STATE_NAME, InitState } from '../model/api/init-state';
import { Observable, of } from 'rxjs';
import { PostLocalAdminUserData } from '../model/api/post-local-admin-user-data';
import { PostAzureAdminUserData } from '../model/api/post-azure-admin-user-data';
import { OauthClientDetails } from '../model/api/oauth-client-details';

export class InitServiceMock {
  getInitState$(customErrorHandler?: ErrorHandlingFunction<InitState>): Observable<InitState> {
    return of({
      initState: INIT_STATE_NAME.INITIALIZED,
      runtimeId: 'string'
    });
  }

  postLocalAdminUser$(data: PostLocalAdminUserData): Observable<InitState> {
    return of();
  }

  postAzureAdminUser$(data: PostAzureAdminUserData): Observable<InitState> {
    return of();
  }

  postOauthClientDetails$(data: OauthClientDetails): Observable<InitState> {
    return of();
  }
}
