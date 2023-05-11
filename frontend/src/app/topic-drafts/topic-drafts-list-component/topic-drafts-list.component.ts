import { Component, Input } from '@angular/core';
import { OkrTopicDraft } from '../../shared/model/ui/OrganizationalUnit/okr-topic-draft/okr-topic-draft';

@Component({
  selector: 'app-topic-draft-list',
  templateUrl: './topic-drafts-list.component.html',
  styleUrls: ['./topic-drafts-list.component.scss'],
})
export class TopicDraftsListComponent {
  @Input() topicDrafts: OkrTopicDraft[];

  updateEditedTopicDraft(topicDraft: OkrTopicDraft): void {
    const position: number = this.topicDrafts.findIndex((topicDraftsElement: OkrTopicDraft) => topicDraftsElement.id === topicDraft.id);
    this.topicDrafts[position] = topicDraft;
  }

  removeDeletedTopicDraft(topicDraft: OkrTopicDraft): void {
    const position: number = this.topicDrafts.indexOf(topicDraft);
    this.topicDrafts.splice(position, 1);
  }
}
