import { TestBed } from '@angular/core/testing';
import { CurrentOkrUnitSchemaService } from './current-okr-unit-schema.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA } from '@angular/core';
import { OkrUnitSchemaMapper } from '../shared/services/mapper/okr-unit-schema.mapper';

describe('CurrentOkrUnitSchemaService', () => {
  const untSchemaMapperMock: any = {};

  beforeEach(() => TestBed.configureTestingModule(
    {
      declarations: [],
      imports: [
        HttpClientTestingModule,
      ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA],
      providers: [
        {provide: OkrUnitSchemaMapper, use: untSchemaMapperMock}
      ]
    }));

  it('should be created', () => {
    const service: CurrentOkrUnitSchemaService = TestBed.get(CurrentOkrUnitSchemaService);
    expect(service)
      .toBeTruthy();
  });
});
