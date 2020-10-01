import { TestBed } from '@angular/core/testing';

import { LocalUserService } from './local-user.service';
import { LocalUserApiService } from '../api/local-user-api.service';
import { UserService } from './user.service';

const localUserApiServiceMock: any = {};
const userServiceMock: any = {};

describe('LocalUserService', () => {
  beforeEach(() => TestBed.configureTestingModule({
    providers: [
      { provide: LocalUserApiService, useValue: localUserApiServiceMock },
      { provide: UserService, useValue: userServiceMock }
    ]
  }));

  it('should be created', () => {
    const service: LocalUserService = TestBed.get(LocalUserService);
    expect(service)
      .toBeTruthy();
  });
});
