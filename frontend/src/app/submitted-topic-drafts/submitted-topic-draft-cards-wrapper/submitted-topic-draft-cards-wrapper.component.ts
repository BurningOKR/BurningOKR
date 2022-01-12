import { Component, Input } from '@angular/core';
import { OkrTopicDraft } from '../../shared/model/ui/OrganizationalUnit/okr-topic-draft/okr-topic-draft';

@Component({
  selector: 'app-submitted-topic-draft-cards-wrapper',
  templateUrl: './submitted-topic-draft-cards-wrapper.component.html',
  styleUrls: ['./submitted-topic-draft-cards-wrapper.component.css'],
})
export class SubmittedTopicDraftCardsWrapperComponent {
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
