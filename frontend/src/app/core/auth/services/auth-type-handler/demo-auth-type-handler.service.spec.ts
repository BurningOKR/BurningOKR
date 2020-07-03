import { TestBed } from '@angular/core/testing';

import { DemoAuthTypeHandlerService } from './demo-auth-type-handler.service';

describe('DemoAuthTypeHandlerService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: DemoAuthTypeHandlerService = TestBed.get(DemoAuthTypeHandlerService);
    expect(service)
      .toBeTruthy();
  });
});
