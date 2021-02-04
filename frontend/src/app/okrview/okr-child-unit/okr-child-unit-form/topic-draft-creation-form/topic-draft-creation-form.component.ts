import { Component, Input, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { OkrTopicDraft } from '../../../../shared/model/ui/OrganizationalUnit/okr-topic-draft';
import { TopicDraftMapper } from '../../../../shared/services/mapper/topic-draft-mapper';
import { MatDialogRef } from '@angular/material';
import { I18n } from '@ngx-translate/i18n-polyfill';
import { NEVER } from 'rxjs';

@Component({
  selector: 'app-topic-draft-creation-form',
  templateUrl: './topic-draft-creation-form.component.html',
  styleUrls: ['./topic-draft-creation-form.component.css']
})
export class TopicDraftCreationFormComponent implements OnInit {
  @Input() topicDraft: OkrTopicDraft;
  @Input() companyId?: number;
  @Input() childUnitId?: number;

  topicDraftForm: FormGroup;
  title: string;

  constructor(private topicDraftMapper: TopicDraftMapper,
              private dialogRef: MatDialogRef<TopicDraftCreationFormComponent>,
              private i18n: I18n
  ) { }

  ngOnInit(): void {
    this.topicDraftForm = new FormGroup({
      name: new FormControl('', Validators.maxLength(255)),
      acceptanceCriteria: new FormControl('', Validators.maxLength(1024)),
      contributesTo: new FormControl('', Validators.maxLength(1024)),
      delimitation: new FormControl('', Validators.maxLength(1024)),
      beginning: new FormControl(),
      dependencies: new FormControl('', Validators.maxLength(1024)),
      resources: new FormControl('', Validators.maxLength(1024)),
      handoverPlan: new FormControl('', Validators.maxLength(1024)),
      initiatorId: new FormControl(),
      startTeam: new FormControl([]),
      stakeholders: new FormControl([])
    });

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
    //
    this.dialogRef.close();
  }

  createTopicDraft(topicDraft: OkrTopicDraft): void {
    if (this.companyId) {
      topicDraft.parentUnitId = this.companyId;
      this.dialogRef.close(this.topicDraftMapper
        .postTopicDraftForCompany$(this.companyId, topicDraft));
    } else if (this.childUnitId) {
      topicDraft.parentUnitId = this.childUnitId;
      this.dialogRef.close(this.topicDraftMapper
        .postTopicDraftForOkrBranch$(this.childUnitId, topicDraft));
    }
  }
}
