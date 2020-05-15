import { inject, TestBed } from '@angular/core/testing';

import { LocalGuard } from './local.guard';
import { RouterTestingModule } from '@angular/router/testing';
import { OAuthFrontendDetailsService } from '../services/o-auth-frontend-details.service';
import { OAuthFrontendDetailsServiceMock } from '../../../shared/mocks/o-auth-frontend-details-service-mock';

describe('LocalAuthGuard', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule],
      providers: [
        LocalGuard,
        {provide: OAuthFrontendDetailsService, useValue: OAuthFrontendDetailsServiceMock}
      ]
    });
  });

  it('should ...', inject([LocalGuard], (guard: LocalGuard) => {
    expect(guard)
      .toBeTruthy();
  }));
});
