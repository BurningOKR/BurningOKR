import { TestBed } from '@angular/core/testing';

import { DashboardMapperService } from './dashboard.mapper.service';

describe('Dashboard.MapperService', () => {
  let service: DashboardMapperService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DashboardMapperService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
