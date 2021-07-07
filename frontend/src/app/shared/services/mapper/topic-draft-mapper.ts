import { Injectable } from '@angular/core';
import { TopicDraftApiService } from '../api/topic-draft-api.service';
import { OkrTopicDraftDto } from '../../model/api/OkrUnit/okr-topic-draft.dto';
import { OkrTopicDraft } from '../../model/ui/OrganizationalUnit/okr-topic-draft/okr-topic-draft';
import { CompanyId, OkrUnitId, TopicDraftId } from '../../model/id-types';
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
            topicDraft.okrParentUnitId,
            topicDraft.currentStatus,
            topicDraft.initiator,
            topicDraft.id,
            topicDraft.name,
            topicDraft.initiatorId,
            topicDraft.startTeam,
            topicDraft.stakeholders,
            topicDraft.acceptanceCriteria,
            topicDraft.contributesTo,
            topicDraft.delimitation,
            topicDraft.beginning ?
                new Date(topicDraft.beginning[0], topicDraft.beginning[1] - 1, topicDraft.beginning[2]) :
                null,
            topicDraft.dependencies,
            topicDraft.resources,
            topicDraft.handoverPlan
        );
    }

    static mapTopicDraft(topicDraft: OkrTopicDraft): OkrTopicDraftDto {
        const topicDraftDto: OkrTopicDraftDto = new OkrTopicDraftDto();

        topicDraftDto.id = topicDraft.id;
        topicDraftDto.okrParentUnitId = topicDraft.okrParentUnitId;
        topicDraftDto.currentStatus = topicDraft.currentStatus;
        topicDraftDto.name = topicDraft.name;
        topicDraftDto.initiatorId = topicDraft.initiatorId;
        topicDraftDto.startTeam = topicDraft.startTeam;
        topicDraftDto.stakeholders = topicDraft.stakeholders;
        topicDraftDto.acceptanceCriteria = topicDraft.acceptanceCriteria;
        topicDraftDto.contributesTo = topicDraft.contributesTo;
        topicDraftDto.delimitation = topicDraft.delimitation;
        topicDraftDto.beginning = topicDraft.beginning ? [
            Number(topicDraft.beginning.getFullYear()),
            Number(topicDraft.beginning.getMonth()) + 1,
            Number(topicDraft.beginning.getDate())
        ] : null;
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

    updateTopicDraft$(topicDraft: OkrTopicDraft): Observable<void> {
      return this.topicDraftApiService.updateTopicDraft$(TopicDraftMapper.mapTopicDraft(topicDraft));
    }

    updateTopicDraftStatus$(topicDraft: OkrTopicDraft): Observable<void> {
      return this.topicDraftApiService.updateTopicDraftStatus$(TopicDraftMapper.mapTopicDraft(topicDraft));
    }

    getAllTopicDrafts$(): Observable<OkrTopicDraft[]> {
        return this.topicDraftApiService.getAllTopicDrafts$()
            .pipe(
                map((topicDraftDtos: OkrTopicDraftDto[]) => {
                    return topicDraftDtos.map(TopicDraftMapper.mapTopicDraftDto);
                })
            );
    }

    deleteTopicDraft$(topicDraftId: number): Observable<boolean> {
        return this.topicDraftApiService.deleteTopicDraft$(topicDraftId);
    }
}
