import { TestBed } from '@angular/core/testing';

import { ChartOptionsBuilderService } from './chart-options-builder.service';

describe('ChartOptionsBuilderService', () => {
  let service: ChartOptionsBuilderService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ChartOptionsBuilderService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
