import { TestBed } from '@angular/core/testing';

import { DateMapper } from './date.mapper';

describe('DateMapper', () => {
  let service: DateMapper;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DateMapper);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
