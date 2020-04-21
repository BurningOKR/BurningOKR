import { TestBed } from '@angular/core/testing';

import { CurrentDepartmentStructureService } from './current-department-structure.service';

describe('DepartmentStructureServiceService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: CurrentDepartmentStructureService = TestBed.get(CurrentDepartmentStructureService);
    expect(service)
      .toBeTruthy();
  });
});
