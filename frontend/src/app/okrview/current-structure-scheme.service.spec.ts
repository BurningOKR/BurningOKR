import { TestBed } from '@angular/core/testing';

import { CurrentStructureSchemeService } from './current-structure-scheme.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA } from '@angular/core';
import { ApiHttpService } from '../core/services/api-http.service';
import { StructureSchemaMapper } from '../shared/services/mapper/structure-schema-mapper.service';
import { StructureSchemaMapperServiceMock } from '../shared/mocks/department-structure-mapper-service-mock';

describe('structureSchemaServiceService', () => {
  const structureSchemaMapperServiceMock: StructureSchemaMapperServiceMock
  = new StructureSchemaMapperServiceMock();

  beforeEach(() => TestBed.configureTestingModule(
    {
      declarations: [],
      imports: [
        HttpClientTestingModule,
      ],
      schemas: [ CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA ],
      providers: [
        {provide: StructureSchemaMapper, use: structureSchemaMapperServiceMock}
      ]
    }));

  it('should be created', () => {
    const service: CurrentStructureSchemeService = TestBed.get(CurrentStructureSchemeService);
    expect(service)
      .toBeTruthy();
  });
});
