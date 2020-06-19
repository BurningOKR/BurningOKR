import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { KeyResultDto } from '../../model/api/key-result.dto';
import { ViewKeyResult } from '../../model/ui/view-key-result';
import { KeyResultApiService } from '../api/key-result-api.service';
import { ObjectiveId } from '../../model/id-types';
import { Unit } from '../../model/api/unit.enum';

@Injectable({
  providedIn: 'root'
})
export class KeyResultMapper {
  constructor(private keyResultApiService: KeyResultApiService) {
  }

  private static getEnumKeyByEnumValue(myEnum: object, enumValue: string): string {
    const keys: string[] = Object.keys(myEnum)
      .filter(x => myEnum[x] === enumValue);

    return keys.length > 0 ? keys[0] : null ;
  }

  private static mapToKeyResultDTO(viewKeyResult: ViewKeyResult): KeyResultDto {
    return {
      id: viewKeyResult.id,
      startValue: viewKeyResult.start,
      currentValue: viewKeyResult.current,
      targetValue: viewKeyResult.end,
      unit: KeyResultMapper.getEnumKeyByEnumValue(Unit, viewKeyResult.unit) as keyof Unit,
      title: viewKeyResult.keyResult,
      description: viewKeyResult.description,
      noteIds: viewKeyResult.commentIdList,
      parentObjectiveId: viewKeyResult.parentObjectiveId
    };
  }

  private static mapToViewKeyResult(keyResult: KeyResultDto): ViewKeyResult {
    return new ViewKeyResult(
      keyResult.id,
      keyResult.startValue,
      keyResult.currentValue,
      keyResult.targetValue,
      Unit[keyResult.unit],
      keyResult.title,
      keyResult.description,
      keyResult.parentObjectiveId,
      keyResult.noteIds
    );
  }

  getKeyResultsForObjective$(objectiveId: ObjectiveId): Observable<ViewKeyResult[]> {
    return this.keyResultApiService.getKeyResultsForObjective$(objectiveId)
      .pipe(
        map((keyResultList: KeyResultDto[]) => {
          return keyResultList.map(KeyResultMapper.mapToViewKeyResult);
        })
      );
  }

  putKeyResult$(viewKeyResult: ViewKeyResult): Observable<ViewKeyResult> {
    return this.keyResultApiService
      .putKeyResult$(KeyResultMapper.mapToKeyResultDTO(viewKeyResult), viewKeyResult.id)
      .pipe(map(KeyResultMapper.mapToViewKeyResult));
  }

  postKeyResult$(objectiveId: ObjectiveId, keyResultView: ViewKeyResult): Observable<ViewKeyResult> {
    return this.keyResultApiService
      .postKeyResult$(objectiveId, KeyResultMapper.mapToKeyResultDTO(keyResultView))
      .pipe(map(KeyResultMapper.mapToViewKeyResult));
  }

  getKeyResultById$(id: ObjectiveId): Observable<ViewKeyResult> {
    return this.keyResultApiService.getKeyResultById$(id)
      .pipe(map(KeyResultMapper.mapToViewKeyResult));
  }

  deleteKeyResult$(keyResultId: ObjectiveId): Observable<boolean> {
    return this.keyResultApiService.deleteKeyResult$(keyResultId);
  }
}
