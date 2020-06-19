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
    oAuthServiceMock.logOut.mockReset();
    routerMock.navigate.mockReset();
    oAuthDetailsMock.getAuthType$.mockReset();

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

  });

  it('should create', () => {
    oAuthDetailsMock.getAuthType$
      .mockReturnValueOnce(of(Consts.AUTHTYPE_LOCAL));
    fixture = TestBed.createComponent(LogoutComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    expect(component)
      .toBeTruthy();
  });

  it('should route on local mode', () => {
    oAuthDetailsMock.getAuthType$
      .mockReturnValueOnce(of(Consts.AUTHTYPE_LOCAL));
    fixture = TestBed.createComponent(LogoutComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    expect(routerMock.navigate)
      .toHaveBeenCalled();
  });

  it('should not route on aad mode', () => {
    oAuthDetailsMock.getAuthType$
      .mockReturnValueOnce(of(Consts.AUTHTYPE_AZURE));
    fixture = TestBed.createComponent(LogoutComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    expect(routerMock.navigate)
      .not
      .toHaveBeenCalled();
  });

})
;
