import { Component, EventEmitter, Inject, OnInit, ViewEncapsulation } from '@angular/core';
import { OkrTopicDraft } from '../../shared/model/ui/OrganizationalUnit/okr-topic-draft/okr-topic-draft';
import { status } from '../../shared/model/ui/OrganizationalUnit/okr-topic-draft/okr-topic-draft-status-enum';
import { NEVER } from 'rxjs';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { FormControl, FormGroup } from '@angular/forms';
import { OkrChildUnitRoleService } from '../../shared/services/helper/okr-child-unit-role.service';
import { CurrentUserService } from '../../core/services/current-user.service';
import { TopicDraftEditDialogueComponent } from '../topic-draft-edit-dialogue-component/topic-draft-edit-dialogue.component';
import { TopicDraftPermissionService } from '../topic-draft-permission.service';
import { TopicDraftStatusService } from '../topic-draft-status.service';

export interface SubmittedTopicDraftDetailsFormData {
  topicDraft: OkrTopicDraft;
}

@Component({
  selector: 'app-topic-draft-details-dialogue',
  templateUrl: './topic-draft-details-dialogue.component.html',
  styleUrls: ['./topic-draft-details-dialogue.component.scss'],
  encapsulation: ViewEncapsulation.None,
})

export class TopicDraftDetailsDialogueComponent implements OnInit {

  editedTopicDraftEvent: EventEmitter<OkrTopicDraft>;
  enumStatus = status;
  topicDraft: OkrTopicDraft;
  submittedTopicDraftDetailsForm: FormGroup;
  canEdit: boolean;

  constructor(
    private dialogRef: MatDialogRef<TopicDraftDetailsDialogueComponent>,
    private okrChildUnitRoleService: OkrChildUnitRoleService,
    private currentUserService: CurrentUserService,
    private dialog: MatDialog,
    private topicDraftPermissionService: TopicDraftPermissionService,
    @Inject(MAT_DIALOG_DATA) private formData: (SubmittedTopicDraftDetailsFormData | any),
  ) {
  }

  ngOnInit(): void {
    this.topicDraft = this.formData.topicDraft;
    this.editedTopicDraftEvent = this.formData.editedTopicDraftEvent;
    this.submittedTopicDraftDetailsForm = new FormGroup({
        name: new FormControl(this.topicDraft.name),
        currentStatus: new FormControl(this.topicDraft.currentStatus),
        beginning: new FormControl(this.topicDraft.beginning.toLocaleDateString()),
        initiator: new FormControl(this.topicDraft.initiator),
        contributesTo: new FormControl(this.topicDraft.contributesTo),
        handoverPlan: new FormControl(this.topicDraft.handoverPlan),
        dependencies: new FormControl(this.topicDraft.dependencies),
        delimitation: new FormControl(this.topicDraft.delimitation),
        resources: new FormControl(this.topicDraft.resources),
        description: new FormControl(this.topicDraft.description),
      },
    );
    this.canEdit = this.topicDraftPermissionService.hasCurrentUserEditingPermissions(this.topicDraft)
      && TopicDraftStatusService.isTopicDraftInSubmissionStage(this.topicDraft);
  }

  editDialog(): void {
    this.closeDialog();
    const data: object = {
      data: {
        topicDraft: this.topicDraft,
        editedTopicDraftEvent: this.editedTopicDraftEvent,
      },
    };
    this.dialog.open(TopicDraftEditDialogueComponent, data);
  }

  closeDialog(): void {
    this.dialogRef.close(NEVER);
  }
}
