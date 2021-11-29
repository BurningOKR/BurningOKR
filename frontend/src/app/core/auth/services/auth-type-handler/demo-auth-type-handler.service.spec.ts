import { TestBed } from '@angular/core/testing';

import { DemoAuthTypeHandlerService } from './demo-auth-type-handler.service';
import { OAuthService } from 'angular-oauth2-oidc';
import { Router } from '@angular/router';
import { FetchingService } from '../../../services/fetching.service';

const oAuthService: any = {
  getRefreshToken: jest.fn(),
  refreshToken: jest.fn(),
  fetchTokenUsingPasswordFlow: jest.fn(),
  getAccessTokenExpiration: jest.fn()
};

const router: any = {
  navigate: jest.fn()
};

const fetchingService: any = {
  refetchAll: jest.fn()
};

describe('DemoAuthTypeHandlerService', () => {
  beforeEach(() => TestBed.configureTestingModule({
    providers: [
      DemoAuthTypeHandlerService,
      { provide: OAuthService, useValue: oAuthService },
      { provide: Router, useValue: router },
      { provide: FetchingService, useValue: fetchingService }
    ]
  }));

  beforeEach(() => {
    oAuthService.getRefreshToken.mockReset();
    oAuthService.getRefreshToken.mockReturnValue('token');
    oAuthService.refreshToken.mockReset();
    oAuthService.fetchTokenUsingPasswordFlow.mockReset();
    oAuthService.fetchTokenUsingPasswordFlow.mockReturnValue(new Promise<void>(resolve => resolve()));
    oAuthService.getAccessTokenExpiration.mockReset();
    oAuthService.getAccessTokenExpiration.mockReturnValue(0);

    router.navigate.mockReset();

    fetchingService.refetchAll.mockReset();
  });

  it('should be created', () => {
    const service: DemoAuthTypeHandlerService = TestBed.get(DemoAuthTypeHandlerService);
    expect(service)
      .toBeTruthy();
  });

  it('startLoginProcedure should return true when there is a refreshToken', done => {
    oAuthService.getRefreshToken.mockReturnValue('token');

    const service: DemoAuthTypeHandlerService = TestBed.get(DemoAuthTypeHandlerService);

    service.startLoginProcedure()
      .then((value: boolean) => {
        expect(value)
          .toBeTruthy();
        done();
      });
  });

  it('startLoginProcedure should call refreshToken when there is a token', done => {
    oAuthService.getRefreshToken.mockReturnValue('token');

    const service: DemoAuthTypeHandlerService = TestBed.get(DemoAuthTypeHandlerService);

    service.startLoginProcedure()
      .then(() => {
        expect(oAuthService.refreshToken)
          .toHaveBeenCalled();
        done();
      });
  });

  it('startLoginProcedure should login when there is no token', done => {
    oAuthService.getRefreshToken.mockReturnValue(null);

    const service: DemoAuthTypeHandlerService = TestBed.get(DemoAuthTypeHandlerService);

    service.startLoginProcedure()
      .then(() => {
        expect(oAuthService.fetchTokenUsingPasswordFlow)
          .toHaveBeenCalled();
        done();
      });
  });

  it('startLoginProcedure should login with standard user when there is no token', done => {
    oAuthService.getRefreshToken.mockReturnValue(null);

    const service: DemoAuthTypeHandlerService = TestBed.get(DemoAuthTypeHandlerService);

    service.startLoginProcedure()
      .then(() => {
        expect(oAuthService.fetchTokenUsingPasswordFlow)
          .toHaveBeenCalledWith('iwant@burningokr', 'Passwort');
        done();
      });
  });
});
