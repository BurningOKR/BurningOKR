import { ApiHttpService } from '../../../core/services/api-http.service';
import { Observable } from 'rxjs';
import { OkrTopicDraftDto } from '../../model/api/OkrUnit/okr-topic-draft.dto';
import { Injectable } from '@angular/core';
import { OkrDepartment } from '../../model/ui/OrganizationalUnit/okr-department';

@Injectable({
  providedIn: 'root'
})

export class TopicDraftApiService {
  constructor(private api: ApiHttpService) {
  }

  postTopicDraft$(topicDraftDto: OkrTopicDraftDto): Observable<OkrTopicDraftDto> {
    return this.api.postData$('/topicDrafts/create', topicDraftDto);
  }

  updateTopicDraft$(topicDraftDto: OkrTopicDraftDto): Observable<void> {
    return this.api.putData$<void>(`topicDrafts/${topicDraftDto.id}`, topicDraftDto);
  }

  updateTopicDraftStatus$(topicDraftDto: OkrTopicDraftDto): Observable<void> {
    return this.api.putData$<void>(`topicDrafts/status/${topicDraftDto.id}`, topicDraftDto);
  }

  getAllTopicDrafts$(): Observable<OkrTopicDraftDto[]> {
    return this.api.getData$('topicDrafts');
  }

  deleteTopicDraft$(topicDraftId: number): Observable<boolean> {
    return this.api.deleteData$(`topicDraft/${topicDraftId}`);
  }

  convertTopicDraftToTeam$(topicDraftId: number, okrUnitId: number): Observable<OkrDepartment> {
    return this.api.getData$(`topicDrafts?topicDraftId=${topicDraftId}&okrUnitId=${okrUnitId}`);
  }
}
