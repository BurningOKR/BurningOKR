import { TestBed } from '@angular/core/testing';
import { CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA } from '@angular/core';
import { LogoutComponent } from './logout.component';
import { OAuthService } from 'angular-oauth2-oidc';
import { Router } from '@angular/router';
import { OAuthFrontendDetailsService } from '../../services/o-auth-frontend-details.service';
import { of } from 'rxjs';
import { Consts } from '../../../../shared/consts';

describe('LogoutComponent', () => {
  let component: any;
  let fixture: any;

  const oAuthServiceMock: any = {
    logOut: jest.fn(),
  };
  const routerMock: any = {
    navigate: jest.fn(),
  };
  const oAuthDetailsMock: any = {
    getAuthType$: jest.fn()
  };

  beforeEach(() => {
    oAuthDetailsMock.getAuthType$
      .mockReturnValueOnce(of(Consts.AUTHTYPE_LOCAL))
      .mockReturnValueOnce(of(Consts.AUTHTYPE_AZURE));

    TestBed.configureTestingModule({
      declarations: [LogoutComponent],
      schemas: [CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA],
      providers: [
        {provide: OAuthService, useValue: oAuthServiceMock},
        {provide: Router, useValue: routerMock},
        {
          provide: OAuthFrontendDetailsService,
          useValue: oAuthDetailsMock
        },
      ]
    })
      .compileComponents();
    fixture = TestBed.createComponent(LogoutComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });

  it('should route on local mode', () => {

    expect(routerMock.navigate)
      .toHaveBeenCalled();
  });

  // Todo dturnschek 19.05.2020; Test for aad Return
  // Don't know how to mock aad return for a single test, since we test ngOnInit, which is
  // called right after .compileComponent()
});
