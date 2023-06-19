import { TestBed } from '@angular/core/testing';

import { OauthInterceptor } from './oauth.interceptor';

describe('OauthInterceptor', () => {
  beforeEach(() => TestBed.configureTestingModule({
    providers: [
      OauthInterceptor,
    ],
  }));

  it('should be created', () => {
    const interceptor: OauthInterceptor = TestBed.inject(OauthInterceptor);
    expect(interceptor).toBeTruthy();
  });
});
