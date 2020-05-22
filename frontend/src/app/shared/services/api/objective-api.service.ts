// istanbul ignore file
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiHttpService } from '../../../core/services/api-http.service';
import { CompanyId, DepartmentId, ObjectiveId } from '../../model/id-types';
import { ObjectiveDto } from '../../model/api/objective.dto';

@Injectable({
  providedIn: 'root'
})
export class ObjectiveApiService {
  constructor(private api: ApiHttpService) {}

  getObjectiveById$(id: ObjectiveId): Observable<ObjectiveDto> {
    return this.api.getData$(`objectives/${id}`);
  }

  getObjectivesForCompany$(companyId: CompanyId): Observable<ObjectiveDto[]> {
    return this.api.getData$(`companies/${companyId}/objectives`);
  }

  getObjectivesForDepartment$(departmentId: DepartmentId): Observable<ObjectiveDto[]> {
    return this.api.getData$(`departments/${departmentId}/objectives`);
  }

  postObjectiveForCompany$(companyId: CompanyId, objective: ObjectiveDto): Observable<ObjectiveDto> {
    return this.api.postData$(`companies/${companyId}/objectives`, objective);
  }

  postObjectiveForDepartment$(departmentId: DepartmentId, objective: ObjectiveDto): Observable<ObjectiveDto> {
    return this.api.postData$(`departments/${departmentId}/objectives`, objective);
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
