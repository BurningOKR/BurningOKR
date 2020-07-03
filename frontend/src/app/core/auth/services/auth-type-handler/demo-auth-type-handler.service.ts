import { Injectable } from '@angular/core';
import { LocalAuthTypeHandlerService } from './local-auth-type-handler.service';

@Injectable({
  providedIn: 'root'
})
export class DemoAuthTypeHandlerService extends LocalAuthTypeHandlerService {
  private EMAIL: string = 'iwant@burningokr';
  private PASSWORD: string = 'Passwort';

  async startLoginProcedure(): Promise<boolean> {
    return this.getRefreshToken()
      .then(() => {
        return true;
      })
      .catch(() => {
        this.login(this.EMAIL, this.PASSWORD);

        return false;
      });
  }
}
