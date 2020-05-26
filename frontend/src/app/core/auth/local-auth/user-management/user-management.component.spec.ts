import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UserManagementComponent } from './user-management.component';
import { CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA } from '@angular/core';
import { MatDialog } from '@angular/material';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { LocalUserApiService } from '../../../../shared/services/api/local-user-api.service';
import { OAuthService } from 'angular-oauth2-oidc';
import { MaterialTestingModule } from '../../../../testing/material-testing.module';
import { LoggerTestingModule } from 'ngx-logger/testing';
import { CurrentUserService } from '../../../services/current-user.service';
import { User } from '../../../../shared/model/api/user';
import { of } from 'rxjs';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

describe('UserManagementComponent', () => {

  let component: UserManagementComponent;
  let fixture: ComponentFixture<UserManagementComponent>;

  const oAuthServiceMock: any = {};
  const localUserApiServiceMock: any = {
    getUsers$: jest.fn(),
    getAdminIds$: jest.fn(),
  };
  const currentUserServiceMock: any = {
    getCurrentUser$: jest.fn()
  };

  const currentUserMock: User = {
    id: 'testId',
    givenName: 'testGivenName',
    surname: 'testSurname',
    jobTitle: 'testJobTitle',
    department: 'testDepartment',
    email: 'testEmail',
    photo: 'testPhoto',
    active: true
  };
  const anotherUserMock: User = {
    id: 'anotherTestId',
    givenName: 'anotherTestGivenName',
    surname: 'anotherTestSurname',
    jobTitle: 'anotherTestJobTitle',
    department: 'anotherTestDepartment',
    email: 'anotherTestEmail',
    photo: 'anotherTestPhoto',
    active: true
  };

  beforeEach(async(() => {
    localUserApiServiceMock.getUsers$.mockReset();
    localUserApiServiceMock.getAdminIds$.mockReset();
    currentUserServiceMock.getCurrentUser$.mockReset();

    localUserApiServiceMock.getUsers$.mockReturnValue(of([currentUserMock, anotherUserMock]));
    localUserApiServiceMock.getAdminIds$.mockReturnValue(of([anotherUserMock.id]));
    currentUserServiceMock.getCurrentUser$.mockReturnValue(of(currentUserMock));
    TestBed.configureTestingModule({
      declarations: [UserManagementComponent],
      imports: [
        MaterialTestingModule,
        RouterTestingModule,
        HttpClientTestingModule,
        LoggerTestingModule,
        BrowserAnimationsModule
      ],
      providers: [
        {provide: MatDialog, useValue: {}},
        {provide: LocalUserApiService, useValue: localUserApiServiceMock},
        {provide: OAuthService, useValue: oAuthServiceMock},
        {provide: CurrentUserService, useValue: currentUserServiceMock}
      ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA],
    })
      .compileComponents();
    fixture = TestBed.createComponent(UserManagementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
