import { Injectable } from '@angular/core';
import {ApiHttpService} from "../../../core/services/api-http.service";
import {TaskId} from "../../model/id-types";
import {Observable} from "rxjs";
import {RevisionDto} from "../../model/api/revision-dto";

@Injectable({
  providedIn: 'root'
})
export class RevisionApiService {

  constructor(private api: ApiHttpService) { }

  getRevisionsForTask$(taskId: TaskId): Observable<RevisionDto[]> {
    return this.api.getData$(`tasks/revisions/${taskId}`);
  }

}
