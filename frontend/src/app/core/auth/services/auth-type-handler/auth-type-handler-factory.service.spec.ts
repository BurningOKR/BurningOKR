import { TestBed } from '@angular/core/testing';

import { AuthTypeHandlerFactoryService } from './auth-type-handler-factory.service';
import { Injector } from '@angular/core';
import { OAuthFrontendDetailsService } from '../o-auth-frontend-details.service';

const injectorMock: any = {};
const oAuthFrontendDetailsServiceMock: any = {};

describe('AuthTypeHandlerFactoryService', () => {
  beforeEach(() => TestBed.configureTestingModule({
    providers: [
      {provide: Injector, useValue: injectorMock},
      {provide: OAuthFrontendDetailsService, useValue: oAuthFrontendDetailsServiceMock}
    ]
  }));

  it('should be created', () => {
    const service: AuthTypeHandlerFactoryService = TestBed.get(AuthTypeHandlerFactoryService);
    expect(service)
      .toBeTruthy();
  });
});
