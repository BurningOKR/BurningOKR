import { inject, TestBed } from '@angular/core/testing';
import { InitService } from '../../services/init.service';
import { Router } from '@angular/router';
import { of } from 'rxjs';
import { InitGuard } from './init.guard';

const routerMock: any = {
  createUrlTree: jest.fn(),
};
const initServiceMock: any = {
  isInitialized$: jest.fn(),
};

describe('InitGuard', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [],
      providers: [
        InitGuard,
        { provide: InitService, useValue: initServiceMock },
        { provide: Router, useValue: routerMock },
      ],
    });
  });

  it('should create', inject([InitGuard], (guard: InitGuard) => {
    expect(guard)
      .toBeTruthy();
  }));

  it('should navigate', inject([InitGuard], (guard: InitGuard) => {
    initServiceMock.isInitialized$.mockReturnValue(of(true));

    const returnValue: any = guard.canActivate(null, null);
    returnValue.subscribe(() => {
      expect(routerMock.createUrlTree)
        .toHaveBeenCalled();
    });
  }));

});
