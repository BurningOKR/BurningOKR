import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UserDialogComponent } from './user-dialog.component';
import { FormBuilder } from '@angular/forms';
import { CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef, MatSnackBar } from '@angular/material';
import { PasswordService } from '../../../password-service/password.service';
import { PasswordServiceMock } from '../../../../../../shared/mocks/password-service-mock';
import { i18nMock } from '../../../../../../shared/mocks/i18n-mock';
import { I18n } from '@ngx-translate/i18n-polyfill';
import { UserDialogData } from '../user-dialog-data';
import { LocalUserService } from '../../../../../../shared/services/helper/local-user.service';
import { CurrentUserService } from '../../../../../services/current-user.service';
import { of } from 'rxjs';
import { User } from '../../../../../../shared/model/api/user';
import 'linq4js';

describe('UserDialogComponent', () => {
  const formBuilder: FormBuilder = new FormBuilder();
  const formData: UserDialogData = {
    title: 'test_title',
  };

  const localUserServiceMock: any = {
    getUsers$: jest.fn()
  };

  const currentUserServiceMock: any = {
    getCurrentUser$: jest.fn(),
    isCurrentUserAdmin$: jest.fn()
  };

  let component: UserDialogComponent;
  let fixture: ComponentFixture<UserDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UserDialogComponent ],
      providers: [
        {provide: MatDialogRef, useValue: {}},
        {provide: FormBuilder, useValue: formBuilder},
        {provide: MAT_DIALOG_DATA, useValue: formData},
        {provide: LocalUserService, useValue: localUserServiceMock},
        {provide: CurrentUserService, useValue: currentUserServiceMock},
        {provide: PasswordService, useValue: PasswordServiceMock},
        {provide: MatSnackBar, useValue: {}},
        {provide: I18n, useValue: i18nMock},
      ],
      schemas: [
        NO_ERRORS_SCHEMA, CUSTOM_ELEMENTS_SCHEMA,
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    currentUserServiceMock.getCurrentUser$.mockReset();
    currentUserServiceMock.getCurrentUser$.mockReturnValue(of(new User()));
    currentUserServiceMock.isCurrentUserAdmin$.mockReset();
    currentUserServiceMock.isCurrentUserAdmin$.mockReturnValue(of(true));

    localUserServiceMock.getUsers$.mockReset();
    localUserServiceMock.getUsers$.mockReturnValue(of([]));

    fixture = TestBed.createComponent(UserDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
