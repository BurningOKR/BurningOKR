import { TestBed } from '@angular/core/testing';

import { PasswordService } from './password.service';
import { ApiHttpService } from '../../../services/api-http.service';
import { ApiHttpServiceMock } from '../../../../shared/mocks/api-http-service-mock';

describe('PasswordService', () => {
  beforeEach(() => TestBed.configureTestingModule({
    providers: [
      {provide: ApiHttpService, useValue: ApiHttpServiceMock}
      ]
  }));

  it('should be created', () => {
    const service: PasswordService = TestBed.get(PasswordService);
    expect(service)
      .toBeTruthy();
  });
});
