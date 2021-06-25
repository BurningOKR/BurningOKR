import { Component, Input } from '@angular/core';
import { OkrTopicDraft } from '../../shared/model/ui/OrganizationalUnit/okr-topic-draft/okr-topic-draft';
import { status } from '../../shared/model/ui/OrganizationalUnit/okr-topic-draft/okr-topic-draft-status-enum';
import { SubmittedTopicDraftDetailsComponent } from '../submitted-topic-draft-details/submitted-topic-draft-details.component';
import { MatDialog } from '@angular/material';

@Component({
  selector: 'app-submitted-topic-draft-card',
  templateUrl: './submitted-topic-draft-card.component.html',
  styleUrls: ['./submitted-topic-draft-card.component.css']
})
export class SubmittedTopicDraftCardComponent {
  @Input()
  topicDraft: OkrTopicDraft;

  enumStatus = status;

  constructor(private dialog: MatDialog) { }

  viewTopicDraft(): void {
    const data: object = {
      data: {
        topicDraft: this.topicDraft
      }
    };
    this.dialog.open(SubmittedTopicDraftDetailsComponent, data);
  }
}
