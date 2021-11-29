import { TestBed } from '@angular/core/testing';
import { KeyResultMapper } from './key-result.mapper';
import { KeyResultApiService } from '../api/key-result-api.service';
import { KeyResultDto } from '../../model/api/key-result.dto';
import { Unit } from '../../model/api/unit.enum';
import { Observable, of } from 'rxjs';
import { ViewKeyResult } from '../../model/ui/view-key-result';

const keyResultApiServiceMock: any = {
  getKeyResultsForObjective$: jest.fn(),
  putKeyResult$: jest.fn()
};

const keyResultDto: KeyResultDto = {
  currentValue: 5,
  description: 'test description',
  id: 1,
  noteIds: [1, 2],
  parentObjectiveId: 2,
  startValue: 0,
  targetValue: 10,
  title: 'test key result',
  unit: Unit[Unit.EURO],
  keyResultMilestoneDtos: []
};

const keyResult: ViewKeyResult = new ViewKeyResult(
  0,
  1,
  2,
  3,
  Unit.EURO,
  'result',
  'description',
  11,
  [12345],
  []
  )
;
let service: KeyResultMapper;
describe('KeyResultMapper', () => {
  beforeEach(() => TestBed.configureTestingModule({
    providers: [
      {provide: KeyResultApiService, useValue: keyResultApiServiceMock}
    ]
  }));

  beforeEach(() => {
    keyResultApiServiceMock.getKeyResultsForObjective$.mockReset();
    keyResultApiServiceMock.getKeyResultsForObjective$.mockReturnValueOnce(of([keyResultDto]));
    keyResultApiServiceMock.putKeyResult$.mockReset();
    keyResultApiServiceMock.putKeyResult$.mockReturnValueOnce(of(keyResultDto));

    service = TestBed.inject(KeyResultMapper);
  });

  it('should be created', () => {
    expect(service)
      .toBeTruthy();
  });

  it('should return ViewKeyResult', done => {

    const actual$: Observable<ViewKeyResult[]> = service.getKeyResultsForObjective$(123);

    actual$.subscribe(
      (results: ViewKeyResult[]) => {
        const result: ViewKeyResult = results[0];

        expect(results.length)
          .toEqual(1);
        expect(result)
          .toBeTruthy();
        expect(result.current)
          .toEqual(keyResultDto.currentValue);
        expect(result.description)
          .toEqual(keyResultDto.description);
        expect(result.id)
          .toEqual(keyResultDto.id);
        expect(result.commentIdList)
          .toEqual(keyResultDto.noteIds);
        expect(result.parentObjectiveId)
          .toEqual(keyResultDto.parentObjectiveId);
        expect(result.start)
          .toEqual(keyResultDto.startValue);
        expect(result.end)
          .toEqual(keyResultDto.targetValue);
        expect(result.keyResult)
          .toEqual(keyResultDto.title);
        expect(result.unit)
          .toEqual(Unit[keyResultDto.unit]);
        done();
      }
    );
  });

  it('should return call api with dto', done => {

    const actual$: Observable<ViewKeyResult> = service.putKeyResult$(keyResult);
    actual$.subscribe(() => {
      expect(keyResultApiServiceMock.putKeyResult$.mock.calls.length)
        .toEqual(1);
      const apiCalledObject: KeyResultDto = keyResultApiServiceMock.putKeyResult$.mock.calls[0][0];
      expect(apiCalledObject.startValue)
        .toEqual(keyResult.start);
      expect(apiCalledObject.currentValue)
        .toEqual(keyResult.current);
      expect(apiCalledObject.targetValue)
        .toEqual(keyResult.end);
      expect(apiCalledObject.unit)
        .toEqual('EURO');
      expect(apiCalledObject.title)
        .toEqual(keyResult.keyResult);
      expect(apiCalledObject.description)
        .toEqual(keyResult.description);
      expect(apiCalledObject.noteIds)
        .toEqual(keyResult.commentIdList);
      expect(apiCalledObject.id)
        .toEqual(keyResult.id);
      expect(apiCalledObject.parentObjectiveId)
        .toEqual(keyResult.parentObjectiveId);
      done();
    });
  });
});
