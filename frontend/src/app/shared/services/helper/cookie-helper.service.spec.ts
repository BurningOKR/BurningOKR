import { TestBed } from '@angular/core/testing';

import { CookieHelperService } from './cookie-helper.service';

describe('CookieHelperService', () => {
  let service: CookieHelperService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CookieHelperService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
