import { Component, Input, OnInit } from '@angular/core';
import { OkrTopicDraft } from '../../shared/model/ui/OrganizationalUnit/okr-topic-draft/okr-topic-draft';

@Component({
  selector: 'app-submitted-topic-draft-card',
  templateUrl: './submitted-topic-draft-card.component.html',
  styleUrls: ['./submitted-topic-draft-card.component.css']
})
export class SubmittedTopicDraftCardComponent implements OnInit {
  isFolded: boolean;
  foldIcon: string;

  @Input()
  topicDraft: OkrTopicDraft;

  foldButtonClicked(): void {
    this.isFolded = !this.isFolded;
    this.isFolded ? this.foldIcon = 'arrow_drop_up' : this.foldIcon = 'arrow_drop_down';
  }

  ngOnInit(): void {
    this.foldIcon = 'arrow_drop_down';
    this.isFolded = false;
  }
}
