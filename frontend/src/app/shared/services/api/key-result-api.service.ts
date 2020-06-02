// istanbul ignore file
import { Injectable } from '@angular/core';
import { ApiHttpService } from '../../../core/services/api-http.service';
import { Observable } from 'rxjs';
import { KeyResultId, ObjectiveId } from '../../model/id-types';
import { KeyResultDto } from '../../model/api/key-result.dto';

@Injectable({
  providedIn: 'root'
})
export class KeyResultApiService {

  constructor(private api: ApiHttpService) {
  }

  getKeyResultById$(id: KeyResultId): Observable<KeyResultDto> {
    return this.api.getData$(`keyresults/${id}`);
  }

  getKeyResultsForObjective$(id: ObjectiveId): Observable<KeyResultDto[]> {
    return this.api.getData$(`objectives/${id}/keyresults`);
  }

  putKeyResult$(keyResult: KeyResultDto, keyResultId: KeyResultId): Observable<KeyResultDto> {
    return this.api.putData$(`keyresults/${keyResultId}`, keyResult);
  }

  postKeyResult$(objectiveId: ObjectiveId, keyResult: KeyResultDto): Observable<KeyResultDto> {
    return this.api.postData$(`objectives/${objectiveId}/keyresults`, keyResult);
  }

  deleteKeyResult$(keyResultId: KeyResultId): Observable<boolean> {
    return this.api.deleteData$(`keyresults/${keyResultId}`);
  }
}
