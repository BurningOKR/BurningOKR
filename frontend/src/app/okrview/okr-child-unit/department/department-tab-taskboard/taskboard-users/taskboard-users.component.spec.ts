import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TaskboardUsersComponent } from './taskboard-users.component';
import { AvatarComponent } from 'ngx-avatars';
import { OAuthModule } from 'angular-oauth2-oidc';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { CurrentUserService } from '../../../../../core/services/current-user.service';
import { UserService } from '../../../../../shared/services/helper/user.service';
import { of } from 'rxjs';

describe('TaskboardUsersComponent', () => {
  let component: TaskboardUsersComponent;
  let fixture: ComponentFixture<TaskboardUsersComponent>;

  const currentUserServiceMock: any = {
    getCurrentUserId$: jest.fn(),
    isCurrentUserAdmin$: jest.fn(),
  };

  const localUserServiceMock: any = {
    getUsers$: jest.fn(),
    getAdminIds$: jest.fn(),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [TaskboardUsersComponent, AvatarComponent],
      imports: [OAuthModule.forRoot(), HttpClientTestingModule],
      providers: [
        { provide: CurrentUserService, useValue: currentUserServiceMock },
        { provide: UserService, useValue: localUserServiceMock },
      ],
    })
      .compileComponents();
  });

  beforeEach(() => {
    currentUserServiceMock.getCurrentUserId$.mockReturnValue(of('1'));

    fixture = TestBed.createComponent(TaskboardUsersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
