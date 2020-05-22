import { inject, TestBed } from '@angular/core/testing';
import { InitService } from '../../../services/init.service';
import { Router } from '@angular/router';
import { of } from 'rxjs';
import { INIT_STATE_NAME } from '../../../../shared/model/api/init-state';
import { InitGuard } from './init.guard';

const routerMock: any = {
  navigate: jest.fn()
};
const initServiceMock: any = {
  getInitState$: jest.fn()
};

describe('InitGuard', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [],
      providers: [
        InitGuard,
        {provide: InitService, useValue: initServiceMock},
        {provide: Router, useValue: routerMock}
      ]
    });
  });

  it('should create', inject([InitGuard], (guard: InitGuard) => {
    expect(guard)
      .toBeTruthy();
  }));

  it('should navigate', inject([InitGuard], (guard: InitGuard) => {
    initServiceMock.getInitState$.mockReturnValue(of(INIT_STATE_NAME.INITIALIZED));

    const returnValue: any = guard.canActivate(null, null);
    returnValue.subscribe(() => {
        expect(routerMock.navigate)
          .toHaveBeenCalled();
      });
  }));

});
