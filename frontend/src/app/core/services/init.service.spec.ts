import { TestBed } from '@angular/core/testing';

import { InitService } from './init.service';
import { CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA } from '@angular/core';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ApiHttpService } from './api-http.service';
import { INIT_STATE_NAME, InitState } from '../../shared/model/api/init-state';
import { of } from 'rxjs';

const apiHttpService: any = {
  getData$: jest.fn()
};

describe('InitService', () => {

  beforeEach(() => TestBed.configureTestingModule(
    {
      declarations: [],
      imports: [
        HttpClientTestingModule,
      ],
      schemas: [ CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA ],
      providers: [
        InitService,
        {provide: ApiHttpService, useValue: apiHttpService}
      ]
    }));

  beforeEach(() => {
    apiHttpService.getData$.mockReset();
  });

  it('should be created', () => {
    const service: InitService = TestBed.get(InitService);
    expect(service)
      .toBeTruthy();
  });

  it('isInitialized should return true when subject is true', done => {
    const service: InitService = TestBed.get(InitService);

    (service as any).initialized$.next(true);

    service.isInitialized$()
      .subscribe((initialized: boolean) => {
        expect(initialized)
          .toBeTruthy();
        done();
      });
  });

  it('isInitialized should not call an http method when subject is true', done => {
    const service: InitService = TestBed.get(InitService);

    (service as any).initialized$.next(true);

    service.isInitialized$()
      .subscribe(() => {
        expect(apiHttpService.getData$)
          .toHaveBeenCalledTimes(0);
        done();
      });
  });

  it('isInitialized should call http service when subject is false', done => {
    const initState: InitState = {
      initState: INIT_STATE_NAME.INITIALIZED,
      runtimeId: 'test'
    };
    apiHttpService.getData$.mockReturnValue(of(initState));

    const service: InitService = TestBed.get(InitService);

    (service as any).initialized$.next(false);

    service.isInitialized$()
      .subscribe(() => {
        expect(apiHttpService.getData$)
          .toHaveBeenCalled();
        done();
      });
  });
});
