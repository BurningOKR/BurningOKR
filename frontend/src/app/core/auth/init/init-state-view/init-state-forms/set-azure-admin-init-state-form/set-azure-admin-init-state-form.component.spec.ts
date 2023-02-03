import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { SetAzureAdminInitStateFormComponent } from './set-azure-admin-init-state-form.component';
import { SharedModule } from '../../../../../../shared/shared.module';
import { CurrentUserService } from '../../../../../services/current-user.service';
import { AuthenticationService } from '../../../../services/authentication.service';
import { InitService } from '../../../../../services/init.service';
import { FetchingService } from '../../../../../services/fetching.service';
import { of } from 'rxjs';
import { User } from '../../../../../../shared/model/api/user';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { MaterialTestingModule } from '../../../../../../testing/material-testing.module';
import { NGXLogger } from 'ngx-logger';
import { RouterTestingModule } from '@angular/router/testing';
import { INIT_STATE_NAME, InitState } from '../../../../../../shared/model/api/init-state';

const currentUserService: any = {
  getCurrentUser$: jest.fn(),
};

const authenticationService: any = {};

const initService: any = {
  postAzureAdminUser$: jest.fn(),
};

const fetchingService: any = {};

const ngxLogger: any = {};

let user: User;

const initState: InitState = {
  runtimeId: '',
  initState: INIT_STATE_NAME.INITIALIZED,
};

describe('SetAzureAdminInitStateFormComponent', () => {
  let component: SetAzureAdminInitStateFormComponent;
  let fixture: ComponentFixture<SetAzureAdminInitStateFormComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      imports: [
        SharedModule,
        HttpClientTestingModule,
        MaterialTestingModule,
        RouterTestingModule,
      ],
      declarations: [
        SetAzureAdminInitStateFormComponent,
      ],
      providers: [
        { provide: CurrentUserService, useValue: currentUserService },
        { provide: AuthenticationService, useValue: authenticationService },
        { provide: InitService, useValue: initService },
        { provide: FetchingService, useValue: fetchingService },
        { provide: NGXLogger, useValue: ngxLogger },
      ],
    })
      .compileComponents();
  }));

  beforeEach(() => {
    user = {
      id: 'testUser',
      email: 'test@test',
      active: true,
      department: null,
      givenName: 'John',
      jobTitle: '',
      photo: '',
      surname: 'Doe',
    };

    currentUserService.getCurrentUser$.mockReset();
    currentUserService.getCurrentUser$.mockReturnValue(of(user));

    initService.postAzureAdminUser$.mockReset();
    initService.postAzureAdminUser$.mockReturnValue(of(initState));

    fixture = TestBed.createComponent(SetAzureAdminInitStateFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
      .toBeTruthy();
  });

  it('handleSubmitClick should postAzureAdminUser', () => {
    component.handleSubmitClick(user);

    expect(initService.postAzureAdminUser$)
      .toHaveBeenCalled();
  });

  // it('handleLogoutClick should refetch CurrentUserService', () => {
  //   component.handleSubmitClick(user);
  //
  //   setTimeout
  // });
});
