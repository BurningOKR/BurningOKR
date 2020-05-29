import { TestBed } from '@angular/core/testing';

import { CurrentNavigationService } from './current-navigation.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA } from '@angular/core';
import { CurrentStructureSchemeService } from './current-structure-scheme.service';
import { CurrentstructureSchemaServiceMock } from '../shared/mocks/current-department-structure-service-mock';

describe('CurrentNavigationService', () => {
  const currentstructureSchemaServiceMock: CurrentstructureSchemaServiceMock = new CurrentstructureSchemaServiceMock();

  beforeEach(() => TestBed.configureTestingModule({
    declarations: [],
    imports: [
      HttpClientTestingModule,
    ],
    schemas: [ CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA ],
    providers: [
      { provide: CurrentStructureSchemeService, useValue: currentstructureSchemaServiceMock }
    ]
  }));

  it('should be created', () => {
    const service: CurrentNavigationService = TestBed.get(CurrentNavigationService);
    expect(service)
      .toBeTruthy();
  });
});
