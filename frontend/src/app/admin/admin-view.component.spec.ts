import { TestBed } from '@angular/core/testing';
import { CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA } from '@angular/core';
import { Router } from '@angular/router';
import { AdminViewComponent } from './admin-view.component';
import { UserService } from '../shared/services/helper/user.service';
import { CurrentUserService } from '../core/services/current-user.service';
import { MatDialogModule } from '@angular/material/dialog';
import { User } from '../shared/model/api/user';
import { of } from 'rxjs';
import 'linq4js';
import { TrackByPropertyPipe } from './pipes/track-by-property.pipe';
import { AdminUserIdsPipe } from './pipes/admin-user-ids.pipe';
import { MaterialTestingModule } from '../testing/material-testing.module';

describe('AdminViewComponent', () => {
  let component: any;
  let fixture: any;
  const userService: any = {
    getCurrentUser$: jest.fn(),
    getAdminIds$: jest.fn(),
    getUsers$: jest.fn()
  };
  const currentUserService: any = {
    getCurrentUser$: jest.fn()
  };
  const router: any = {};
  const i18n: any = {};

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

  const adminList: string[] = ['testId'];

  beforeEach(() => {
    userService.getCurrentUser$.mockReturnValue(of(currentUserMock));
    userService.getAdminIds$.mockReturnValue(of([adminList]));
    userService.getUsers$.mockReturnValue(of([currentUserMock, anotherUserMock]));
    currentUserService.getCurrentUser$.mockReturnValue(of(currentUserMock));

    TestBed.configureTestingModule({
      declarations: [AdminViewComponent, TrackByPropertyPipe, AdminUserIdsPipe],
      schemas: [CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA],
      imports: [ MatDialogModule, MaterialTestingModule ],
      providers: [
        {provide: UserService, useValue: userService},
        {
          provide: CurrentUserService,
          useValue: currentUserService
        },
        {provide: Router, useValue: router},
      ],
    })
      .compileComponents();
    fixture = TestBed.createComponent(AdminViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });
});
