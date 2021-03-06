import { Component, Inject, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { OkrTopicDraft } from '../../../../shared/model/ui/OrganizationalUnit/okr-topic-draft/okr-topic-draft';
import { TopicDraftMapper } from '../../../../shared/services/mapper/topic-draft-mapper';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';
import { I18n } from '@ngx-translate/i18n-polyfill';
import { NEVER } from 'rxjs';
import { status } from '../../../../shared/model/ui/OrganizationalUnit/okr-topic-draft/okr-topic-draft-status-enum';

interface TopicDraftCreationFormData {
  topicDraft?: OkrTopicDraft;
  companyId?: number;
  unitId?: number;
}

@Component({
  selector: 'app-topic-draft-creation-form',
  templateUrl: './topic-draft-creation-form.component.html',
  styleUrls: ['./topic-draft-creation-form.component.css']
})
export class TopicDraftCreationFormComponent implements OnInit {
  topicDraftForm: FormGroup;
  title: string;

  constructor(private topicDraftMapper: TopicDraftMapper,
              private dialogRef: MatDialogRef<TopicDraftCreationFormComponent>,
              private i18n: I18n,
              @Inject(MAT_DIALOG_DATA) private formData: TopicDraftCreationFormData
  ) { }

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
      initiatorId: new FormControl('', [Validators.required]),
      startTeam: new FormControl([]),
      stakeholders: new FormControl([])
    });

    if (this.formData.topicDraft) {
      this.topicDraftForm.patchValue(this.formData.topicDraft);
    }

    this.title = this.i18n({
      id: 'component_topicDraftCreationForm_headline',
      description: 'Title of the OkrTopicDraftCreation dialog',
      value: 'Themenentwurf erstellen'
    });
  }

  closeDialog(): void {
    this.dialogRef.close(NEVER);
  }

  saveTopicDraft(): void {
    const topicDraft: OkrTopicDraft = this.topicDraftForm.getRawValue();
    this.createTopicDraft(topicDraft);
  }

  createTopicDraft(topicDraft: OkrTopicDraft): void {
    topicDraft.currentStatus = status.submitted; // ToDo (C.K. 27.04.21) test
    if (this.formData.companyId) {
      topicDraft.okrParentUnitId = this.formData.companyId;
      this.dialogRef.close(this.topicDraftMapper
        .postTopicDraftForCompany$(this.formData.companyId, topicDraft));
    } else if (this.formData.unitId) {
      topicDraft.okrParentUnitId = this.formData.unitId;
      this.dialogRef.close(this.topicDraftMapper
        .postTopicDraftForOkrBranch$(this.formData.unitId, topicDraft));
    }
  }
}
