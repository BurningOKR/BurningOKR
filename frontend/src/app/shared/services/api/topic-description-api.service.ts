import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiHttpService } from '../../../core/services/api-http.service';
import { DepartmentId } from '../../model/id-types';
import { OkrTopicDescriptionDto } from '../../model/api/OkrUnit/okr-topic-description.dto';

@Injectable({
  providedIn: 'root'
})
export class TopicDescriptionApiService {
  constructor(private api: ApiHttpService) {}

  getTopicDescriptionById$(departmentId: DepartmentId): Observable<OkrTopicDescriptionDto> {
    return this.api.getData$<OkrTopicDescriptionDto>(`/departments/${departmentId}/topicdescription`);
  }
  putTopicDescription$(departmentId: DepartmentId , descriptionDto:
    OkrTopicDescriptionDto): Observable<OkrTopicDescriptionDto> {
    return this.api.putData$(`/departments/${departmentId}/topicdescription`, descriptionDto);
  }
}
