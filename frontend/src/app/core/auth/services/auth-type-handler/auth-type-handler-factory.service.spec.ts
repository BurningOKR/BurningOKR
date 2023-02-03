import { TestBed } from '@angular/core/testing';

import { AuthTypeHandlerFactoryService } from './auth-type-handler-factory.service';
import { Injector } from '@angular/core';
import { OAuthFrontendDetailsService } from '../o-auth-frontend-details.service';
import { Consts } from '../../../../shared/consts';
import { of } from 'rxjs';
import { AuthTypeHandlerBase } from './auth-type-handler-base';
import { AzureAuthTypeHandlerService } from './azure-auth-type-handler.service';
import { LocalAuthTypeHandlerService } from './local-auth-type-handler.service';

const injectorMock: any = {
  get: jest.fn(),
};
const oAuthFrontendDetailsServiceMock: any = {
  getAuthType$: jest.fn(),
};
const azureAuthTypeHandlerServiceMock: any = {};
const localAuthTypeHandlerServiceMock: any = {};
let service: AuthTypeHandlerFactoryService;

describe('AuthTypeHandlerFactoryService', () => {
  beforeEach(() => TestBed.configureTestingModule({
    providers: [
      { provide: Injector, useValue: injectorMock },
      { provide: OAuthFrontendDetailsService, useValue: oAuthFrontendDetailsServiceMock },
      { provide: AzureAuthTypeHandlerService, useValue: azureAuthTypeHandlerServiceMock },
      { provide: LocalAuthTypeHandlerService, useValue: localAuthTypeHandlerServiceMock },
    ],
  }));

  beforeEach(() => {
    service = TestBed.inject(AuthTypeHandlerFactoryService);

    oAuthFrontendDetailsServiceMock.getAuthType$.mockReset();
    injectorMock.get.mockReset();
  });

  it('should be created', () => {
    expect(service)
      .toBeTruthy();
  });

  it('should get AzureAuthTypeHandlerService', async () => {
    oAuthFrontendDetailsServiceMock.getAuthType$.mockReturnValue(of(Consts.AUTHTYPE_AZURE));
    injectorMock.get.mockReturnValue(azureAuthTypeHandlerServiceMock);

    const authTypeHandlerBase: AuthTypeHandlerBase = await service.getAuthTypeHandler();
    expect(authTypeHandlerBase)
      .toEqual(azureAuthTypeHandlerServiceMock);
  });

  it('should get LocalAuthTypeHandlerService', async () => {
    oAuthFrontendDetailsServiceMock.getAuthType$.mockReturnValue(of(Consts.AUTHTYPE_LOCAL));
    injectorMock.get.mockReturnValue(localAuthTypeHandlerServiceMock);

    const authTypeHandlerBase: AuthTypeHandlerBase = await service.getAuthTypeHandler();
    expect(authTypeHandlerBase)
      .toEqual(localAuthTypeHandlerServiceMock);
  });
});
