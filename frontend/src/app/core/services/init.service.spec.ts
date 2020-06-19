import { TestBed } from '@angular/core/testing';

import { InitService } from './init.service';
import { CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA } from '@angular/core';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ApiHttpService } from './api-http.service';
import { ApiHttpServiceMock } from '../../shared/mocks/api-http-service-mock';

describe('InitService', () => {
  const apiHttpServiceMock: ApiHttpServiceMock = new ApiHttpServiceMock();

  beforeEach(() => TestBed.configureTestingModule(
    {
      declarations: [],
      imports: [
        HttpClientTestingModule,
      ],
      schemas: [ CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA ],
      providers: [
        {provide: ApiHttpService, use: apiHttpServiceMock}
      ]
    }));

  it('should be created', () => {
    const service: InitService = TestBed.get(InitService);
    expect(service)
      .toBeTruthy();
  });
});
