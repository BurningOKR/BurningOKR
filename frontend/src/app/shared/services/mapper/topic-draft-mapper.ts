import { Injectable } from '@angular/core';
import { TopicDraftApiService } from '../api/topic-draft-api.service';
import { OkrTopicDraftDto } from '../../model/api/OkrUnit/okr-topic-draft.dto';
import { OkrTopicDraft } from '../../model/ui/OrganizationalUnit/okr-topic-draft';
import { CompanyId, OkrUnitId } from '../../model/id-types';
import { Observable } from 'rxjs/internal/Observable';
import { map } from 'rxjs/internal/operators';

@Injectable({
  providedIn: 'root'
})
export class TopicDraftMapper {
  constructor(private topicDraftApiService: TopicDraftApiService) {
  }

  static mapTopicDraftDto(topicDraft: OkrTopicDraftDto): OkrTopicDraft {
    return new OkrTopicDraft(
      topicDraft.okrUnitId,
      topicDraft.name,
      topicDraft.label,
      topicDraft.objectiveIds,
      topicDraft.parentUnitId,
      topicDraft.isActive,
      topicDraft.isParentUnitABranch,
      topicDraft.initiatorId,
      topicDraft.startTeam,
      topicDraft.stakeholders,
      topicDraft.acceptanceCriteria,
      topicDraft.contributesTo,
      topicDraft.delimitation,
      topicDraft.beginning,
      topicDraft.dependencies,
      topicDraft.resources,
      topicDraft.handoverPlan
    );
  }

  postTopicDraftForCompany$(companyId: CompanyId, topicDraft: OkrTopicDraftDto): Observable<OkrTopicDraft> {
    return this.topicDraftApiService.postTopicDraftForCompany$(companyId, topicDraft)
      .pipe(map(TopicDraftMapper.mapTopicDraftDto));
  }

  postTopicDraftForOkrBranch$(branchId: OkrUnitId, topicDraft: OkrTopicDraftDto): Observable<OkrTopicDraft> {
    return this.topicDraftApiService.postTopicDraftForOkrBranch$(branchId, topicDraft)
      .pipe(map(TopicDraftMapper.mapTopicDraftDto));
  }
}
