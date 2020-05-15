import { TestBed } from '@angular/core/testing';

import { SetOauthClientDetailsFormComponent } from './set-oauth-client-details-form.component';
import { CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA } from '@angular/core';
import { FormBuilder, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { InitService } from '../../../../../../services/init.service';
import { OAuthFrontendDetailsService } from '../../../../../services/o-auth-frontend-details.service';
import { MatSnackBar } from '@angular/material';
import { I18n } from '@ngx-translate/i18n-polyfill';
import { InitServiceMock } from '../../../../../../../shared/mocks/init-service-mock';
import { OAuthFrontendDetailsServiceMock } from '../../../../../../../shared/mocks/o-auth-frontend-details-service-mock';
import { i18nMock } from '../../../../../../../shared/mocks/i18n-mock';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';

describe('SetOauthClientDetailsFormComponent', () => {
  let component: any;
  let fixture: any;

  const formBuilder: FormBuilder = new FormBuilder();
  const initServiceMock: InitServiceMock = new InitServiceMock();
  const oAuthFrontendDetailsServiceMock: OAuthFrontendDetailsServiceMock = new OAuthFrontendDetailsServiceMock();

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        ReactiveFormsModule,
        FormsModule,
        RouterTestingModule,
      ],
      schemas: [ CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA ],
      declarations: [ SetOauthClientDetailsFormComponent ],
      providers: [
        {provide: FormBuilder, useValue: formBuilder},
        {provide: InitService, useValue: initServiceMock},
        {provide: OAuthFrontendDetailsService, useValue: oAuthFrontendDetailsServiceMock},
        {provide: MatSnackBar, useValue: {}},
        {provide: I18n, useValue: i18nMock}
      ]
    })
    .compileComponents();
    fixture = TestBed.createComponent(SetOauthClientDetailsFormComponent);
    component = fixture.debugElement.componentInstance;
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
