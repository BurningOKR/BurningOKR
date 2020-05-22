import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UserDialogComponent } from './user-dialog.component';
import { FormBuilder } from '@angular/forms';
import { CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef, MatSnackBar } from '@angular/material';
import { LocalUserApiService } from '../../../../../../shared/services/api/local-user-api.service';
import { PasswordService } from '../../../password-service/password.service';
import { PasswordServiceMock } from '../../../../../../shared/mocks/password-service-mock';
import { i18nMock } from '../../../../../../shared/mocks/i18n-mock';
import { I18n } from '@ngx-translate/i18n-polyfill';
import { LocalUserApiServiceMock } from '../../../../../../shared/mocks/local-user-api-service-mock';
import { UserDialogData } from '../user-dialog-data';

describe('UserDialogComponent', () => {
  const formBuilder: FormBuilder = new FormBuilder();
  const formData: UserDialogData = {
    title: 'test_title',
  };
  const localUserApiServiceMock: LocalUserApiServiceMock = new LocalUserApiServiceMock();

  let component: UserDialogComponent;
  let fixture: ComponentFixture<UserDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UserDialogComponent ],
      providers: [
        {provide: MatDialogRef, useValue: {}},
        {provide: FormBuilder, useValue: formBuilder},
        {provide: MAT_DIALOG_DATA, useValue: formData},
        {provide: LocalUserApiService, useValue: localUserApiServiceMock},
        {provide: PasswordService, useValue: PasswordServiceMock},
        {provide: MatSnackBar, useValue: {}},
        {provide: I18n, useValue: i18nMock},
      ],
      schemas: [
        NO_ERRORS_SCHEMA, CUSTOM_ELEMENTS_SCHEMA,
      ]
    })
    .compileComponents();
    fixture = TestBed.createComponent(UserDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
