import { Component, Input, OnInit } from '@angular/core';
import { OkrTopicDraft } from '../../shared/model/ui/OrganizationalUnit/okr-topic-draft/okr-topic-draft';
import { status } from '../../shared/model/ui/OrganizationalUnit/okr-topic-draft/okr-topic-draft-status-enum';

@Component({
  selector: 'app-submitted-topic-draft-card',
  templateUrl: './submitted-topic-draft-card.component.html',
  styleUrls: ['./submitted-topic-draft-card.component.css']
})
export class SubmittedTopicDraftCardComponent implements OnInit {
  @Input()
  topicDraft: OkrTopicDraft;

  statusEnum = status;
  currentStatus: string;

  ngOnInit(): void {
    switch (this.topicDraft.currentStatus) {
      case status.rejected: this.currentStatus = 'status-rejected'; break;
      case status.approved: this.currentStatus = 'status-approved'; break;
      case status.submitted: this.currentStatus = 'status-submitted'; break;
      case status.draft: this.currentStatus = 'status-draft'; break;
      default: this.currentStatus = 'status-draft';
    }
    console.log(this.currentStatus + this.topicDraft.currentStatus);
  }
}
