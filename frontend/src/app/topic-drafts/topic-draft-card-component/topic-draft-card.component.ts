import { Component, EventEmitter, Input, Output } from '@angular/core';
import { OkrTopicDraft } from '../../shared/model/ui/OrganizationalUnit/okr-topic-draft/okr-topic-draft';
import { status } from '../../shared/model/ui/OrganizationalUnit/okr-topic-draft/okr-topic-draft-status-enum';
import { TopicDraftDetailsDialogueComponent } from '../topic-draft-details-dialogue-component/topic-draft-details-dialogue.component';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-topic-draft-card',
  templateUrl: './topic-draft-card.component.html',
  styleUrls: ['./topic-draft-card.component.css']
})
export class TopicDraftCardComponent {
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
    this.dialog.open(TopicDraftDetailsDialogueComponent, config);
  }

  notifyWrapperOfEditing(topicDraft: OkrTopicDraft): void {
    this.editedTopicDraftEvent.emit(topicDraft);
  }

  notifyWrapperOfDeletion(): void {
    this.topicDraftDeletedEvent.emit(this.topicDraft);
  }
}
