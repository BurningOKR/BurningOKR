import { TestBed } from '@angular/core/testing';

import { OidcConfigurationService } from './oidc-configuration.service';

describe('OidcConfigurationService', () => {
  let service: OidcConfigurationService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OidcConfigurationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
