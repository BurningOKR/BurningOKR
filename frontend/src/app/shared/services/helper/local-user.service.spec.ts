import { TestBed } from '@angular/core/testing';

import { LocalUserService } from './local-user.service';
import { LocalUserApiService } from '../api/local-user-api.service';
import { UserService } from './user.service';
import { FetchingService } from '../../../core/services/fetching.service';

const localUserApiServiceMock: any = {};
const userServiceMock: any = {};
const fetchingServiceMock: any = {};

describe('LocalUserService', () => {
  beforeEach(() => TestBed.configureTestingModule({
    providers: [
      { provide: LocalUserApiService, useValue: localUserApiServiceMock },
      { provide: UserService, useValue: userServiceMock },
      { provide: FetchingService, useValue: fetchingServiceMock }
    ]
  }));

  it('should be created', () => {
    const service: LocalUserService = TestBed.inject(LocalUserService);
    expect(service)
      .toBeTruthy();
  });
});
