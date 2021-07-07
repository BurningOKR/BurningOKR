import { ApiHttpService } from '../../../core/services/api-http.service';
import { CompanyId, OkrUnitId, TopicDraftId } from '../../model/id-types';
import { Observable } from 'rxjs';
import { OkrTopicDraftDto } from '../../model/api/OkrUnit/okr-topic-draft.dto';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})

export class TopicDraftApiService {
  constructor(private api: ApiHttpService) {
  }

  postTopicDraftForCompany$(companyId: CompanyId, topicDraftDto: OkrTopicDraftDto): Observable<OkrTopicDraftDto> {
    return this.api.postData$(`companies/${companyId}/topicdraft`, topicDraftDto);
  }

  postTopicDraftForOkrBranch$(unitId: OkrUnitId, topicDraftDto: OkrTopicDraftDto): Observable<OkrTopicDraftDto> {
    return this.api.postData$(`branch/${unitId}/topicdraft`, topicDraftDto);
  }

  updateTopicDraft$(topicDraftDto: OkrTopicDraftDto): Observable<void> {
    return this.api.putData$<void>(`topicDrafts/${topicDraftDto.id}`, topicDraftDto);
  }

  updateTopicDraftStatus$(topicDraftDto: OkrTopicDraftDto): Observable<void> {
    console.log("API-Service", topicDraftDto);
    return this.api.putData$<void>(`topicDrafts/status/${topicDraftDto.id}`, topicDraftDto);
  }

  getAllTopicDrafts$(): Observable<OkrTopicDraftDto[]> {
    return this.api.getData$(`topicDrafts/all`);
  }

  deleteTopicDraft$(topicDraftId: number): Observable<boolean> {
    return this.api.deleteData$(`topicDraft/${topicDraftId}`);
  }
}
