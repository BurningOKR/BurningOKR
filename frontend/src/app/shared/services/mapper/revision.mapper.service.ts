import { Injectable } from '@angular/core';
import {RevisionApiService} from '../api/revision-api.service';
import {Observable} from 'rxjs';
import {TaskId} from '../../model/id-types';
import {RevisionDto} from '../../model/api/revision-dto';

@Injectable({
  providedIn: 'root'
})
export class RevisionMapperService {

  constructor(private revisionApiService: RevisionApiService) { }
  getRevisionsForTask$(taskId: TaskId): Observable<RevisionDto[]> {
    return this.revisionApiService.getRevisionsForTask$(taskId);
  }

}
