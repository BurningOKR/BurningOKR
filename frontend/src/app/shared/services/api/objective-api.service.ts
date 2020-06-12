// istanbul ignore file
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiHttpService } from '../../../core/services/api-http.service';
import { CompanyId, ObjectiveId, OkrUnitId } from '../../model/id-types';
import { ObjectiveDto } from '../../model/api/objective.dto';

@Injectable({
  providedIn: 'root'
})
export class ObjectiveApiService {
  constructor(private api: ApiHttpService) {
  }

  getObjectiveById$(id: ObjectiveId): Observable<ObjectiveDto> {
    return this.api.getData$(`objectives/${id}`);
  }

  getObjectivesForDepartment$(departmentId: OkrUnitId): Observable<ObjectiveDto[]> {
    return this.api.getData$(`departments/${departmentId}/objectives`);
  }

  getObjectivesForUnit$(unitId: OkrUnitId): Observable<ObjectiveDto[]> {
    return this.api.getData$(`units/${unitId}/objectives`);
  }

  postObjectiveForDepartment$(departmentId: OkrUnitId, objective: ObjectiveDto): Observable<ObjectiveDto> {
    return this.api.postData$(`departments/${departmentId}/objectives`, objective);
  }

  postObjectiveForUnit$(unitId: OkrUnitId, objective: ObjectiveDto): Observable<ObjectiveDto> {
    return this.api.postData$(`units/${unitId}/objectives`, objective);
  }

  putObjective$(objective: ObjectiveDto, objectiveId: ObjectiveId): Observable<ObjectiveDto> {
    return this.api.putData$(`objectives/${objectiveId}`, objective);
  }

  putObjectiveKeyResultSequence$(objectiveId: ObjectiveId, sequenceList: number[]): Observable<number[]> {
    return this.api.putData$(`objective/${objectiveId}/keyresultsequence`, sequenceList);
  }

  deleteObjective$(objectiveId: ObjectiveId): Observable<boolean> {
    return this.api.deleteData$(`objectives/${objectiveId}`);
  }
}
