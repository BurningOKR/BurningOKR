import { TestBed } from '@angular/core/testing';

import { InitService } from '../../../services/init.service';

describe('InitService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: InitService = TestBed.get(InitService);
    expect(service)
      .toBeTruthy();
  });
});
