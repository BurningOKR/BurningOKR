import { TestBed } from '@angular/core/testing';

import { CurrentNavigationService } from './current-navigation.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA } from '@angular/core';
import { CurrentOkrUnitSchemaService } from './current-okr-unit-schema.service';

describe('CurrentNavigationService', () => {
  const currentUnitSchemaServiceMock: any = {};

  beforeEach(() => TestBed.configureTestingModule({
    declarations: [],
    imports: [
      HttpClientTestingModule,
    ],
    schemas: [ CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA ],
    providers: [
      { provide: CurrentOkrUnitSchemaService, useValue: currentUnitSchemaServiceMock }
    ]
  }));

  it('should be created', () => {
    const service: CurrentNavigationService = TestBed.get(CurrentNavigationService);
    expect(service)
      .toBeTruthy();
  });
});
