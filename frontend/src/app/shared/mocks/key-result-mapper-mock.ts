import { ObjectiveId } from '../model/id-types';
import { Observable, of } from 'rxjs';
import { ViewKeyResult } from '../model/ui/view-key-result';

export class KeyResultMapperMock {
  getKeyResultsForObjective$(objectiveId: ObjectiveId): Observable<ViewKeyResult[]> {
    return of();
  }

  putKeyResult$(viewKeyResult: ViewKeyResult): Observable<ViewKeyResult> {
    return of();
  }

  postKeyResult$(objectiveId: ObjectiveId, keyResultView: ViewKeyResult): Observable<ViewKeyResult> {
    return of();
  }

  getKeyResultById$(id: ObjectiveId): Observable<ViewKeyResult> {
    return of();
  }

  deleteKeyResult$(keyResultId: ObjectiveId): Observable<boolean> {
    return of();
  }
}
