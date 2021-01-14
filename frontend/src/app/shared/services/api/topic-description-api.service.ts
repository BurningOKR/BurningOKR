import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiHttpService } from '../../../core/services/api-http.service';
import { TopicDescriptionId } from '../../model/id-types';
import { OkrTopicDescriptionDto } from '../../model/api/OkrUnit/okr-topic-description.dto';

@Injectable({
  providedIn: 'root'
})
export class TopicDescriptionApiService {
  constructor(private api: ApiHttpService) {}

  getTopicDescriptionById$(descriptionId: TopicDescriptionId): Observable<OkrTopicDescriptionDto> {
    return this.api.getData$<OkrTopicDescriptionDto>(`/departments/${descriptionId}/topicdescription`);
  }
  putTopicDescription$(descriptionId: TopicDescriptionId , descriptionDto:
    OkrTopicDescriptionDto): Observable<OkrTopicDescriptionDto> {
    return this.api.putData$(`/departments/${descriptionId}/topicdescription`, descriptionDto);
  }
}
