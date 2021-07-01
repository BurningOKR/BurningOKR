import { Component, Input, OnInit } from '@angular/core';
import { OkrTopicDraft } from '../../shared/model/ui/OrganizationalUnit/okr-topic-draft/okr-topic-draft';

@Component({
  selector: 'app-submitted-topic-draft-cards-wrapper',
  templateUrl: './submitted-topic-draft-cards-wrapper.component.html',
  styleUrls: ['./submitted-topic-draft-cards-wrapper.component.css']
})
export class SubmittedTopicDraftCardsWrapperComponent implements OnInit {

  @Input()
  topicDrafts: OkrTopicDraft[];

  ngOnInit(): void {
    // to implement
  }

  updateEditedTopicDraft(topicDraft: OkrTopicDraft): void {
    const position: number = this.topicDrafts.findIndex((topicDraftsElement: OkrTopicDraft) => topicDraftsElement.id === topicDraft.id);
    this.topicDrafts[position] = topicDraft;
    console.log("Kann das funktionieren bis in den Wrapper???", topicDraft);
    console.log("Wrapper action", this.topicDrafts);
  }
}
