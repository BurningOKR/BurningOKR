import { ApiHttpService } from '../../../core/services/api-http.service';
import { OkrUnitId } from '../../model/id-types';
import { Observable } from 'rxjs';
import { OkrTopicDraftDto } from '../../model/api/OkrUnit/okr-topic-draft.dto';

export class TopicDraftApiService {
  constructor(private api: ApiHttpService) {
  }

  // TODO P.B. 28.01.2021 Implement needed methods. Change path when backend is done
  getTopicDraftById$(id: OkrUnitId): Observable<OkrTopicDraftDto> {
    return this.api.getData$('TODO');
  }
}
