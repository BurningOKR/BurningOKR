import { Component, EventEmitter, Input, Output } from '@angular/core';
import { OkrTopicDraft } from '../../shared/model/ui/OrganizationalUnit/okr-topic-draft/okr-topic-draft';
import { MatDialog } from '@angular/material';
import { SubmittedTopicDraftEditComponent } from '../submitted-topic-draft-edit/submitted-topic-draft-edit.component';

@Component({
  selector: 'app-submitted-topic-draft-action-button',
  templateUrl: './submitted-topic-draft-action-button.component.html',
  styleUrls: ['./submitted-topic-draft-action-button.component.css']
})
export class SubmittedTopicDraftActionButtonComponent {

  @Input() topicDraft: OkrTopicDraft;

  @Output() clickedEditAction: EventEmitter<void> = new EventEmitter<void>();
  @Output() editedTopicDraftEvent: EventEmitter<OkrTopicDraft> = new EventEmitter<OkrTopicDraft>();
  @Output() clickedDeleteAction: EventEmitter<void> = new EventEmitter<void>();
  @Output() clickedCommentsAction: EventEmitter<void> = new EventEmitter<void>();
  @Output() clickedSubmitAction: EventEmitter<void> = new EventEmitter<void>();
  @Output() clickedWithdrawAction: EventEmitter<void> = new EventEmitter<void>();
  @Output() clickedApprove: EventEmitter<void> = new EventEmitter<void>();
  @Output() clickedDiscardApprovalAction: EventEmitter<void> = new EventEmitter<void>();
  @Output() clickedRefuse: EventEmitter<void>  = new EventEmitter<void>();
  @Output() clickedDiscardRefusalAction: EventEmitter<void> = new EventEmitter<void>();

  constructor(private dialog: MatDialog) { }

  printNotImplemented(): string {
    // TODO: methode Entfernen
    // tslint:disable-next-line: no-console
    console.log('Not Implemented');

    return 'Not Implemented';
  }

  editTopicDraft(): void {
    const data: object = {
      data: {
        topicDraft: this.topicDraft,
        editedTopicDraftEvent: this.editedTopicDraftEvent
      }
    };
    this.dialog.open(SubmittedTopicDraftEditComponent, data);
  }
}
