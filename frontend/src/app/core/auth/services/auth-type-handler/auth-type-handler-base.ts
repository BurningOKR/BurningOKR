import { AuthenticationService } from '../authentication.service';

export interface AuthTypeHandlerBase {
  startLoginProcedure(authenticationService: AuthenticationService): Promise<boolean>;
  setupSilentRefresh(authenticationService: AuthenticationService): void;
}
