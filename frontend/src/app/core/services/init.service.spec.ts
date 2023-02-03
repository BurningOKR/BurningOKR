import { TestBed } from '@angular/core/testing';

import { InitService } from './init.service';
import { CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA } from '@angular/core';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ApiHttpService } from './api-http.service';
import { INIT_STATE_NAME, InitState } from '../../shared/model/api/init-state';
import { of } from 'rxjs';

const apiHttpService: any = {
  getData$: jest.fn(),
};

describe('InitService', () => {

  beforeEach(() => TestBed.configureTestingModule(
    {
      declarations: [],
      imports: [
        HttpClientTestingModule,
      ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA],
      providers: [
        InitService,
        { provide: ApiHttpService, useValue: apiHttpService },
      ],
    }));

  beforeEach(() => {
    apiHttpService.getData$.mockReset();
  });

  it('should be created', () => {
    const service: InitService = TestBed.inject(InitService);
    expect(service)
      .toBeTruthy();
  });

  it('isInitialized should return true when subject is true', done => {
    const service: InitService = TestBed.inject(InitService);

    (service as any).initialized$.next(true);

    service.isInitialized$()
      .subscribe((initialized: boolean) => {
        expect(initialized)
          .toBeTruthy();
        done();
      });
  });

  it('isInitialized should not call an http method when subject is true', done => {
    const service: InitService = TestBed.inject(InitService);

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
      runtimeId: 'test',
    };
    apiHttpService.getData$.mockReturnValue(of(initState));

    const service: InitService = TestBed.inject(InitService);

    (service as any).initialized$.next(false);

    service.isInitialized$()
      .subscribe(() => {
        expect(apiHttpService.getData$)
          .toHaveBeenCalled();
        done();
      });
  });

  it('isInitialized should return true, when subject is false and http service is initialized', done => {
    const initState: InitState = {
      initState: INIT_STATE_NAME.INITIALIZED,
      runtimeId: 'test',
    };
    apiHttpService.getData$.mockReturnValue(of(initState));

    const service: InitService = TestBed.inject(InitService);

    (service as any).initialized$.next(false);

    service.isInitialized$()
      .subscribe((value: boolean) => {
        expect(value)
          .toBeTruthy();
        done();
      });
  });

  it('isInitialized should make subject true, when http service is initialized and subject was false', done => {
    const initState: InitState = {
      initState: INIT_STATE_NAME.INITIALIZED,
      runtimeId: 'test',
    };
    apiHttpService.getData$.mockReturnValue(of(initState));

    const service: InitService = TestBed.inject(InitService);

    (service as any).initialized$.next(false);

    service.isInitialized$()
      .subscribe(() => {
        expect((service as any).initialized$.getValue())
          .toBeTruthy();
        done();
      });
  });

  it('isInitialized should return false, when subject is false and http service is not initialized', done => {
    const initState: InitState = {
      initState: INIT_STATE_NAME.CREATE_USER,
      runtimeId: 'test',
    };
    apiHttpService.getData$.mockReturnValue(of(initState));

    const service: InitService = TestBed.inject(InitService);

    (service as any).initialized$.next(false);

    service.isInitialized$()
      .subscribe((value: boolean) => {
        expect(value)
          .toBeFalsy();
        done();
      });
  });
});
