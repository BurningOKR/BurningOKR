import { TestBed } from '@angular/core/testing';

import { ChartMapperService } from './chart.mapper.service';

describe('ChartMapperService', () => {
  let service: ChartMapperService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ChartMapperService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
