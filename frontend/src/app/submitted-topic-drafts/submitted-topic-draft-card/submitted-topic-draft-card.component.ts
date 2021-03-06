import { Component, EventEmitter, Input, Output } from '@angular/core';
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
  @Input() topicDraft: OkrTopicDraft;
  @Output() topicDraftDeletedEvent = new EventEmitter<OkrTopicDraft>();
  @Output() editedTopicDraftEvent: EventEmitter<OkrTopicDraft> = new EventEmitter<OkrTopicDraft>();

  enumStatus = status;

  constructor(private dialog: MatDialog) { }

  viewTopicDraft(): void {
    const config: object = {
      data: {
        topicDraft: this.topicDraft,
        editedTopicDraftEvent: this.editedTopicDraftEvent,
      },
      width: '80vw'
    };
    this.dialog.open(SubmittedTopicDraftDetailsComponent, config);
  }

  notifyWrapperOfEditing(topicDraft: OkrTopicDraft): void {
    this.editedTopicDraftEvent.emit(topicDraft);
  }

  notifyWrapperOfDeletion(): void {
    this.topicDraftDeletedEvent.emit(this.topicDraft);
  }
}
