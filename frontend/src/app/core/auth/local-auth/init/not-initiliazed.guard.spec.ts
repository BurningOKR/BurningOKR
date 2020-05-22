import { TestBed, inject } from '@angular/core/testing';

import { NotInitiliazedGuard } from './not-initiliazed.guard';
import { RouterTestingModule } from '@angular/router/testing';
import { InitService } from '../../../services/init.service';
import { InitServiceMock } from '../../../../shared/mocks/init-service-mock';

describe('NotInitiliazedGuard', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
      ],
      declarations: [],
      providers: [
        NotInitiliazedGuard,
        {provide: InitService, useValue: InitServiceMock}
        ]
    });
  });

  it('should ...', inject([NotInitiliazedGuard], (guard: NotInitiliazedGuard) => {
    expect(guard)
      .toBeTruthy();
  }));
});
