import { TestBed } from '@angular/core/testing';

import { CurrentCycleService } from './current-cycle.service';
import { CycleMapperMock } from '../shared/mocks/cycle-mapper-mock';
import { CycleMapper } from '../shared/services/mapper/cycle.mapper';
import { CurrentCycleServiceMock } from '../shared/mocks/current-cycle-service-mock';

describe('CurrentCycleService', () => {
  beforeEach(() => TestBed.configureTestingModule({
    providers: [
      { provide: CurrentCycleService, useValue: CurrentCycleServiceMock},
      { provide: CycleMapper, useValue: CycleMapperMock},
    ]
  }));

  it('should be created', () => {
    const service: CurrentCycleService = TestBed.get(CurrentCycleService);
    expect(service)
      .toBeTruthy();
  });
});
