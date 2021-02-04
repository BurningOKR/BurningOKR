import { ApiHttpService } from '../../../core/services/api-http.service';
import { CompanyId, OkrUnitId } from '../../model/id-types';
import { Observable } from 'rxjs';
import { OkrTopicDraftDto } from '../../model/api/OkrUnit/okr-topic-draft.dto';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})

export class TopicDraftApiService {
  constructor(private api: ApiHttpService) {
  }

  // TODO P.B. 28.01.2021 Implement needed methods. Change path when backend is done

  postTopicDraftForCompany$(companyId: CompanyId, topicDraftDto: OkrTopicDraftDto): Observable<OkrTopicDraftDto> {
    return this.api.postData$('TODO', topicDraftDto);
  }

  postTopicDraftForOkrBranch$(unitId: OkrUnitId, topicDraftDto: OkrTopicDraftDto): Observable<OkrTopicDraftDto> {
    return this.api.postData$('TODO', topicDraftDto);
  }
}
