import {Component, Inject, Input} from '@angular/core';
import { OkrTopicDraft } from '../../shared/model/ui/OrganizationalUnit/okr-topic-draft/okr-topic-draft';
import { status } from '../../shared/model/ui/OrganizationalUnit/okr-topic-draft/okr-topic-draft-status-enum';
import { User } from '../../shared/model/api/user';
import {NEVER, Observable} from 'rxjs';
import { MatDialogRef } from '@angular/material/dialog';
import { MAT_DIALOG_DATA } from '@angular/material';

@Component({
  selector: 'app-submitted-topic-draft-details',
  templateUrl: './submitted-topic-draft-details.component.html',
  styleUrls: ['./submitted-topic-draft-details.component.css']
})

export interface SubmittedTopicDraftDetailsFormData {
  topicDraft: OkrTopicDraft;
}

export class SubmittedTopicDraftDetailsComponent {

  @Input() topicDraft: OkrTopicDraft;
  enumStatus = status;

  constructor(private dialogRef: MatDialogRef<SubmittedTopicDraftDetailsComponent>,
              @Inject(MAT_DIALOG_DATA) private formData: SubmittedTopicDraftDetailsFormData) {
    this.topicDraft = formData.topicDraft;
  }

  closeDialog(): void {
    this.dialogRef.close(NEVER);
  }
}
