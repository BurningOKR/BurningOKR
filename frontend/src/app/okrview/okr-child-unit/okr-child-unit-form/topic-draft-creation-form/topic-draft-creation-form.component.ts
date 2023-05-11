import { Component, Inject, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { TranslateService } from '@ngx-translate/core';
import { NEVER } from 'rxjs';
import { take } from 'rxjs/operators';
import { CurrentUserService } from '../../../../core/services/current-user.service';
import { UserId } from '../../../../shared/model/id-types';
import { OkrTopicDraft } from '../../../../shared/model/ui/OrganizationalUnit/okr-topic-draft/okr-topic-draft';
import { TopicDraftMapper } from '../../../../shared/services/mapper/topic-draft-mapper';

interface TopicDraftCreationFormData {
  topicDraft?: OkrTopicDraft;
  companyId?: number;
  unitId?: number;
}

@Component({
  selector: 'app-topic-draft-creation-form',
  templateUrl: './topic-draft-creation-form.component.html',
  styleUrls: ['./topic-draft-creation-form.component.scss'],
})
export class TopicDraftCreationFormComponent implements OnInit {
  topicDraftForm: FormGroup;
  title: string;

  constructor(
    private topicDraftMapper: TopicDraftMapper,
    private dialogRef: MatDialogRef<TopicDraftCreationFormComponent>,
    private translate: TranslateService,
    private currentUserService: CurrentUserService,
    @Inject(MAT_DIALOG_DATA) private formData: TopicDraftCreationFormData,
  ) {
  }

  ngOnInit(): void {
    this.topicDraftForm = new FormGroup({
      name: new FormControl('', [Validators.maxLength(255), Validators.required]),
      description: new FormControl('', Validators.maxLength(1024)),
      contributesTo: new FormControl('', Validators.maxLength(1024)),
      delimitation: new FormControl('', Validators.maxLength(1024)),
      beginning: new FormControl('', [Validators.required]),
      dependencies: new FormControl('', Validators.maxLength(1024)),
      resources: new FormControl('', Validators.maxLength(1024)),
      handoverPlan: new FormControl('', Validators.maxLength(1024)),
      initiatorId: new FormControl(this.getCurrentUserId(), [Validators.required]),
      startTeam: new FormControl([]),
      stakeholders: new FormControl([]),
    });

    if (this.formData.topicDraft) {
      this.topicDraftForm.patchValue(this.formData.topicDraft);
    }

    this.title = this.translate.instant('topic-draft-creation-form.dialog-title');
  }

  getCurrentUserId(): UserId {
    let currentUserId: UserId;
    this.currentUserService
      .getCurrentUserId$().pipe(take(1))
      .subscribe((userId: UserId) => currentUserId = userId);

    return currentUserId;
  }

  closeDialog(): void {
    this.dialogRef.close(NEVER);
  }

  saveTopicDraft(): void {
    const topicDraft: OkrTopicDraft = this.topicDraftForm.getRawValue();
    this.createTopicDraft(topicDraft);
  }

  createTopicDraft(topicDraft: OkrTopicDraft): void {
    this.dialogRef.close(this.topicDraftMapper
      .postTopicDraft$(topicDraft));
  }
}
