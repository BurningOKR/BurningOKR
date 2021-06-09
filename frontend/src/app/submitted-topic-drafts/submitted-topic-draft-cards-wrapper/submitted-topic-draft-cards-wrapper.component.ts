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

}
