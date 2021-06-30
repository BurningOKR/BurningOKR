import { Component, Inject, Input, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { OkrTopicDraft } from '../../shared/model/ui/OrganizationalUnit/okr-topic-draft/okr-topic-draft';
import { SubmittedTopicDraftDetailsFormData } from '../submitted-topic-draft-details/submitted-topic-draft-details.component';
import { MAT_DIALOG_DATA } from '@angular/material';
import { TopicDraftMapper } from '../../shared/services/mapper/topic-draft-mapper';

@Component({
  selector: 'app-submitted-topic-draft-edit',
  templateUrl: './submitted-topic-draft-edit.component.html',
  styleUrls: ['./submitted-topic-draft-edit.component.css']
})
export class SubmittedTopicDraftEditComponent implements OnInit {

  topicDraft: OkrTopicDraft;
  topicDraftForm: FormGroup;
  title: string;
  minBegin: Date;

  constructor(
    private topicDraftMapper: TopicDraftMapper,
    @Inject(MAT_DIALOG_DATA) private formData: (SubmittedTopicDraftDetailsFormData | any)
  ) { }

  ngOnInit(): void {
    this.topicDraft = this.formData.topicDraft;
    this.minBegin = this.topicDraft.beginning;
    this.topicDraftForm = new FormGroup({
      name: new FormControl(this.topicDraft.name, [Validators.maxLength(255), Validators.required]),
      acceptanceCriteria: new FormControl(this.topicDraft.acceptanceCriteria, Validators.maxLength(1024)),
      contributesTo: new FormControl(this.topicDraft.contributesTo, Validators.maxLength(1024)),
      delimitation: new FormControl(this.topicDraft.delimitation, Validators.maxLength(1024)),
      beginning: new FormControl(this.topicDraft.beginning, [Validators.required]),
      dependencies: new FormControl(this.topicDraft.dependencies, Validators.maxLength(1024)),
      resources: new FormControl(this.topicDraft.resources, Validators.maxLength(1024)),
      handoverPlan: new FormControl(this.topicDraft.handoverPlan, Validators.maxLength(1024)),
      initiatorId: new FormControl(this.topicDraft.initiatorId, [Validators.required]),
      startTeam: new FormControl(this.topicDraft.startTeam),
      stakeholders: new FormControl(this.topicDraft.stakeholders)
    });

    if (this.formData.topicDraft) {
      this.topicDraftForm.patchValue(this.formData.topicDraft);
    }

    this.title = 'Themenentwurf bearbeiten';
  }

  saveTopicDraft(): void {
    const oldTopicDraft: OkrTopicDraft = this.topicDraft;
    console.log(this.topicDraft);
    const formTopicDraft: OkrTopicDraft = this.topicDraftForm.getRawValue();
    const updatedTopicDraft: OkrTopicDraft = {...oldTopicDraft, ...formTopicDraft};
    console.log(updatedTopicDraft);
    this.topicDraftMapper.updateTopicDraft$(updatedTopicDraft)
      .subscribe();
  }

}
