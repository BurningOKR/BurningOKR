import { Component, EventEmitter, Inject, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { take } from 'rxjs/operators';
import { OkrTopicDraft } from '../../shared/model/ui/OrganizationalUnit/okr-topic-draft/okr-topic-draft';
import { TopicDraftMapper } from '../../shared/services/mapper/topic-draft-mapper';
import {
  SubmittedTopicDraftDetailsFormData
} from '../topic-draft-details-dialogue-component/topic-draft-details-dialogue.component';

@Component({
  selector: 'app-topic-draft-edit-dialogue',
  templateUrl: './topic-draft-edit-dialogue.component.html',
  styleUrls: ['./topic-draft-edit-dialogue.component.css'],
})
export class TopicDraftEditDialogueComponent implements OnInit {

  topicDraft: OkrTopicDraft;
  topicDraftForm: FormGroup;
  minBegin: Date;
  editedTopicDraftEvent: EventEmitter<OkrTopicDraft>;

  constructor(
    private dialogRef: MatDialogRef<TopicDraftEditDialogueComponent>,
    private topicDraftMapper: TopicDraftMapper,
    @Inject(MAT_DIALOG_DATA) private formData: (SubmittedTopicDraftDetailsFormData | any),
  ) {
  }

  ngOnInit(): void {
    this.topicDraft = this.formData.topicDraft;
    this.editedTopicDraftEvent = this.formData.editedTopicDraftEvent;
    this.minBegin = this.topicDraft.beginning;
    this.topicDraftForm = new FormGroup({
      name: new FormControl(this.topicDraft.name, [Validators.maxLength(255), Validators.required]),
      description: new FormControl(this.topicDraft.description, Validators.maxLength(1024)),
      contributesTo: new FormControl(this.topicDraft.contributesTo, Validators.maxLength(1024)),
      delimitation: new FormControl(this.topicDraft.delimitation, Validators.maxLength(1024)),
      beginning: new FormControl(this.topicDraft.beginning, [Validators.required]),
      dependencies: new FormControl(this.topicDraft.dependencies, Validators.maxLength(1024)),
      resources: new FormControl(this.topicDraft.resources, Validators.maxLength(1024)),
      handoverPlan: new FormControl(this.topicDraft.handoverPlan, Validators.maxLength(1024)),
      initiatorId: new FormControl(this.topicDraft.initiatorId, [Validators.required]),
      startTeam: new FormControl(this.topicDraft.startTeam),
      stakeholders: new FormControl(this.topicDraft.stakeholders),
    });

    if (this.formData.topicDraft) {
      this.topicDraftForm.patchValue(this.formData.topicDraft);
    }
  }

  saveTopicDraft(): void {
    const oldTopicDraft: OkrTopicDraft = this.topicDraft;
    const formTopicDraft: OkrTopicDraft = this.topicDraftForm.getRawValue();
    const updatedTopicDraft: OkrTopicDraft = { ...oldTopicDraft, ...formTopicDraft };
    this.dialogRef.close(
      this.topicDraftMapper.updateTopicDraft$(updatedTopicDraft).pipe(take(1))
        .subscribe(),
    );
    this.editedTopicDraftEvent.emit(updatedTopicDraft);
  }

}
