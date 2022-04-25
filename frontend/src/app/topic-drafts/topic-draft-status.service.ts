import { Injectable } from '@angular/core';
import { OkrTopicDraft } from '../shared/model/ui/OrganizationalUnit/okr-topic-draft/okr-topic-draft';
import { status } from '../shared/model/ui/OrganizationalUnit/okr-topic-draft/okr-topic-draft-status-enum';
import { take } from 'rxjs/operators';
import { TopicDraftMapper } from '../shared/services/mapper/topic-draft-mapper';

@Injectable({
  providedIn: 'root'
})
export class TopicDraftStatusService {

  constructor(private topicDraftMapper: TopicDraftMapper) {}

  static isTopicDraftConvertableToTeam(topicDraft: OkrTopicDraft): boolean {
    return topicDraft.currentStatus === status.approved;
  }

  static isTopicDraftInSubmissionStage(topicDraft: OkrTopicDraft): boolean {
    return topicDraft.currentStatus === status.draft || topicDraft.currentStatus === status.submitted;
  }

  static isTopicDraftInApprovingStage(topicDraft: OkrTopicDraft): boolean {
    return topicDraft.currentStatus !== status.draft;
  }

  static isTopicDraftApprovedOrRejected(topicDraft: OkrTopicDraft): boolean {
    return topicDraft.currentStatus === status.approved || topicDraft.currentStatus === status.rejected;
  }

  changeTopicDraftStatusTo(topicDraft: OkrTopicDraft, newStatus: status): void {
    if (newStatus !== null && newStatus !== undefined) {
      topicDraft.currentStatus = newStatus;
      this.topicDraftMapper.updateTopicDraftStatus$(topicDraft).pipe(take(1))
        .subscribe();
    }
  }
}
