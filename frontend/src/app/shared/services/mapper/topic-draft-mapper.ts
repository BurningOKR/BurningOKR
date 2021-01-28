import { Injectable } from '@angular/core';
import { TopicDraftApiService } from '../api/topic-draft-api.service';
import { OkrTopicDraftDto } from '../../model/api/OkrUnit/okr-topic-draft.dto';
import { OkrTopicDraft } from '../../model/ui/OrganizationalUnit/okr-topic-draft';
import { CompanyId, OkrUnitId } from '../../model/id-types';
import { Observable } from 'rxjs/internal/Observable';
import { map } from 'rxjs/internal/operators';
import { UnitType } from '../../model/api/OkrUnit/unit-type.enum';

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

  static mapTopicDraft(topicDraft: OkrTopicDraft): OkrTopicDraftDto {
    const topicDraftDto: OkrTopicDraftDto = new OkrTopicDraftDto();

    topicDraftDto.__okrUnitType = UnitType.TOPIC_DRAFT;
    topicDraftDto.okrUnitId = topicDraft.id;
    topicDraftDto.name = topicDraft.name;
    topicDraftDto.label = topicDraft.label;
    topicDraftDto.objectiveIds = topicDraft.objectives;
    topicDraftDto.parentUnitId = topicDraft.parentUnitId;
    topicDraftDto.isActive = topicDraft.isActive;
    topicDraftDto.isParentUnitABranch = topicDraft.isParentUnitABranch;
    topicDraftDto.initiatorId = topicDraft.initiatorId;
    topicDraftDto.startTeam = topicDraft.startTeam;
    topicDraftDto.stakeholders = topicDraft.stakeholders;
    topicDraftDto.acceptanceCriteria = topicDraft.acceptanceCriteria;
    topicDraftDto.contributesTo = topicDraft.contributesTo;
    topicDraftDto.delimitation = topicDraft.delimitation;
    topicDraftDto.beginning = topicDraft.beginning;
    topicDraftDto.dependencies = topicDraft.dependencies;
    topicDraftDto.resources = topicDraft.resources;
    topicDraftDto.handoverPlan = topicDraft.handoverPlan;

    return topicDraftDto;
  }

  postTopicDraftForCompany$(companyId: CompanyId, topicDraft: OkrTopicDraft): Observable<OkrTopicDraft> {
    return this.topicDraftApiService.postTopicDraftForCompany$(companyId, TopicDraftMapper.mapTopicDraft(topicDraft))
      .pipe(map(TopicDraftMapper.mapTopicDraftDto));
  }

  postTopicDraftForOkrBranch$(branchId: OkrUnitId, topicDraft: OkrTopicDraft): Observable<OkrTopicDraft> {
    return this.topicDraftApiService.postTopicDraftForOkrBranch$(branchId, TopicDraftMapper.mapTopicDraft(topicDraft))
      .pipe(map(TopicDraftMapper.mapTopicDraftDto));
  }
}
