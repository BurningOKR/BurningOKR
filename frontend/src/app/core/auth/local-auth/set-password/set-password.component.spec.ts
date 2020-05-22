import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SetPasswordComponent } from './set-password.component';
import { MatCardModule, MatSnackBar } from '@angular/material';
import { CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA } from '@angular/core';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { i18nMock } from '../../../../shared/mocks/i18n-mock';
import { I18n } from '@ngx-translate/i18n-polyfill';
import { PasswordService } from '../password-service/password.service';
import { PasswordServiceMock } from '../../../../shared/mocks/password-service-mock';

describe('SetPasswordComponent', () => {
  let component: SetPasswordComponent;
  let fixture: ComponentFixture<SetPasswordComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SetPasswordComponent ],
      imports: [
        MatCardModule,
        HttpClientTestingModule,
        RouterTestingModule,
      ],
      providers: [
        {provide: I18n, useValue: i18nMock},
        {provide: PasswordService, useValue: PasswordServiceMock},
        {provide: MatSnackBar, useValue: {}}
      ],
      schemas: [ CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA ],
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SetPasswordComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
