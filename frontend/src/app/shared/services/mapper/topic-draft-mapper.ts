import { Injectable } from '@angular/core';
import { TopicDraftApiService } from '../api/topic-draft-api.service';
import { OkrTopicDraftDto } from '../../model/api/OkrUnit/okr-topic-draft.dto';
import { OkrTopicDraft } from '../../model/ui/OrganizationalUnit/okr-topic-draft/okr-topic-draft';
import { Observable } from 'rxjs/internal/Observable';
import { map } from 'rxjs/internal/operators';
import { DateMapper } from './date.mapper';

@Injectable({
  providedIn: 'root',
})
export class TopicDraftMapper {
  constructor(
    private topicDraftApiService: TopicDraftApiService,
  ) {
  }

  mapTopicDraftDto(topicDraft: OkrTopicDraftDto): OkrTopicDraft {
    return new OkrTopicDraft(
      topicDraft.okrParentUnitId,
      topicDraft.currentStatus,
      topicDraft.initiator,
      topicDraft.id,
      topicDraft.name,
      topicDraft.initiatorId,
      topicDraft.startTeam,
      topicDraft.stakeholders,
      topicDraft.description,
      topicDraft.contributesTo,
      topicDraft.delimitation,
      DateMapper.mapDateStringToDate(topicDraft.beginning),
      topicDraft.dependencies,
      topicDraft.resources,
      topicDraft.handoverPlan,
    );
  }

  mapTopicDraft(topicDraft: OkrTopicDraft): OkrTopicDraftDto {
    const topicDraftDto: OkrTopicDraftDto = new OkrTopicDraftDto();

    topicDraftDto.id = topicDraft.id;
    topicDraftDto.okrParentUnitId = topicDraft.okrParentUnitId;
    topicDraftDto.currentStatus = topicDraft.currentStatus;
    topicDraftDto.name = topicDraft.name;
    topicDraftDto.initiatorId = topicDraft.initiatorId;
    topicDraftDto.startTeam = topicDraft.startTeam;
    topicDraftDto.stakeholders = topicDraft.stakeholders;
    topicDraftDto.description = topicDraft.description;
    topicDraftDto.contributesTo = topicDraft.contributesTo;
    topicDraftDto.delimitation = topicDraft.delimitation;
    topicDraftDto.beginning = DateMapper.mapDateToDateString(topicDraft.beginning);
    topicDraftDto.dependencies = topicDraft.dependencies;
    topicDraftDto.resources = topicDraft.resources;
    topicDraftDto.handoverPlan = topicDraft.handoverPlan;

    return topicDraftDto;
  }

  postTopicDraft$(topicDraft: OkrTopicDraft): Observable<OkrTopicDraft> {
    console.log(topicDraft);

    return this.topicDraftApiService.postTopicDraft$(this.mapTopicDraft(topicDraft))
      .pipe(map(this.mapTopicDraftDto));
  }

  updateTopicDraft$(topicDraft: OkrTopicDraft): Observable<void> {
    return this.topicDraftApiService.updateTopicDraft$(this.mapTopicDraft(topicDraft));
  }

  updateTopicDraftStatus$(topicDraft: OkrTopicDraft): Observable<void> {
    return this.topicDraftApiService.updateTopicDraftStatus$(this.mapTopicDraft(topicDraft));
  }

  getAllTopicDrafts$(): Observable<OkrTopicDraft[]> {
    return this.topicDraftApiService.getAllTopicDrafts$()
      .pipe(
        map((topicDraftDtos: OkrTopicDraftDto[]) => {
          return topicDraftDtos.map(this.mapTopicDraftDto);
        }),
      );
  }

  deleteTopicDraft$(topicDraftId: number): Observable<boolean> {
    return this.topicDraftApiService.deleteTopicDraft$(topicDraftId);
  }
}
