import { TestBed } from '@angular/core/testing';

import { CurrentDepartmentStructureService } from './current-department-structure.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA } from '@angular/core';
import { ApiHttpService } from '../core/services/api-http.service';
import { DepartmentStructureMapper } from '../shared/services/mapper/department-structure.mapper';
import { DepartmentStructureMapperServiceMock } from '../shared/mocks/department-structure-mapper-service-mock';

describe('DepartmentStructureServiceService', () => {
  const departmentStructureMapperServiceMock: DepartmentStructureMapperServiceMock
  = new DepartmentStructureMapperServiceMock();

  beforeEach(() => TestBed.configureTestingModule(
    {
      declarations: [],
      imports: [
        HttpClientTestingModule,
      ],
      schemas: [ CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA ],
      providers: [
        {provide: DepartmentStructureMapper, use: departmentStructureMapperServiceMock}
      ]
    }));

  it('should be created', () => {
    const service: CurrentDepartmentStructureService = TestBed.get(CurrentDepartmentStructureService);
    expect(service)
      .toBeTruthy();
  });
});
