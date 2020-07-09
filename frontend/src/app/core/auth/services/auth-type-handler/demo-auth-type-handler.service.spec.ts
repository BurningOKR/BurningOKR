import { TestBed } from '@angular/core/testing';

import { DemoAuthTypeHandlerService } from './demo-auth-type-handler.service';
import { OAuthService } from 'angular-oauth2-oidc';
import { Router } from '@angular/router';
import { FetchingService } from '../../../services/fetching.service';

const oAuthService: any = {};

const router: any = {};

const fetchingService: any = {};

describe('DemoAuthTypeHandlerService', () => {
  beforeEach(() => TestBed.configureTestingModule({
    providers: [
      DemoAuthTypeHandlerService,
      { provide: OAuthService, useValue: oAuthService },
      { provide: Router, useValue: router },
      { provide: FetchingService, useValue: fetchingService }
    ]
  }));

  it('should be created', () => {
    const service: DemoAuthTypeHandlerService = TestBed.get(DemoAuthTypeHandlerService);
    expect(service)
      .toBeTruthy();
  });
});
