import { TestBed } from '@angular/core/testing';

import { CurrentNavigationService } from './current-navigation.service';

describe('CurrentNavigationService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: CurrentNavigationService = TestBed.get(CurrentNavigationService);
    expect(service)
      .toBeTruthy();
  });
});
