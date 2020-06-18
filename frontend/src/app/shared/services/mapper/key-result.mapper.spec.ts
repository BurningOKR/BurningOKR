import { TestBed } from '@angular/core/testing';
import { KeyResultMapper } from './key-result.mapper';
import { KeyResultApiService } from '../api/key-result-api.service';
import { KeyResultDto } from '../../model/api/key-result.dto';
import { Unit } from '../../model/api/unit.enum';

const keyResultApiServiceMock: any = {
  getKeyResultsForObjective$: jest.fn()
};

const keyResult: KeyResultDto = {
  currentValue: 5,
  description: 'test description',
  id: 1,
  noteIds: [1, 2],
  parentObjectiveId: 2,
  startValue: 0,
  targetValue: 10,
  title: 'test key result',
  unit: Unit.EURO
};

let service: KeyResultMapper;
describe('KeyResultMapper', () => {
  beforeEach(() => TestBed.configureTestingModule({
    providers: [
      { provide: KeyResultApiService, useValue: keyResultApiServiceMock }
    ]
  }));

  beforeEach(() => {
    keyResultApiServiceMock.getKeyResultsForObjective$.mockReset();

    service = TestBed.get(KeyResultMapper);
  });

  it('should be created', () => {
    expect(service)
      .toBeTruthy();
  });
});
