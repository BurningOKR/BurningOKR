import { Component, Inject, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { OkrTopicDescription } from '../../../../../shared/model/ui/OrganizationalUnit/okr-topic-description';
import { TopicDescriptionMapper } from '../../../../../shared/services/mapper/topic-description-mapper';
import { DepartmentId } from '../../../../../shared/model/id-types';
import { Subscription } from 'rxjs';
import { TranslateService } from '@ngx-translate/core';

interface OkrTopicDescriptionFormData {
  departmentId: DepartmentId;
  okrTopicDescription: OkrTopicDescription;
}

@Component({
  selector: 'app-department-description-edit-form',
  templateUrl: './department-description-edit-form.component.html',
  styleUrls: ['./department-description-edit-form.component.css']
})
export class DepartmentDescriptionEditFormComponent implements OnInit, OnDestroy {

  subscriptions: Subscription[] = [];
  descriptionForm: FormGroup;
  title: string;

  constructor(private okrTopicDescriptionService: TopicDescriptionMapper,
              private dialogRef: MatDialogRef<DepartmentDescriptionEditFormComponent>,
              private translate: TranslateService,
              @Inject(MAT_DIALOG_DATA) private formData: OkrTopicDescriptionFormData) { }

  ngOnInit(): void {
    this.descriptionForm = new FormGroup({
      name: new FormControl('', Validators.maxLength(255)),
      description: new FormControl('', Validators.maxLength(1024)),
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

    if (this.formData.okrTopicDescription) {
      this.descriptionForm.patchValue(this.formData.okrTopicDescription);
    }

    this.subscriptions.push(this.translate.stream('department-description-edit-form.edit-description').subscribe(text => {
      this.title = text;
    }));
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }

  saveDescription(): void {
    const okrTopicDescription: OkrTopicDescription = this.descriptionForm.getRawValue();
    this.okrTopicDescriptionService.putTopicDescription$(this.formData.departmentId, okrTopicDescription)
      .subscribe(() => this.dialogRef.close());
  }
}
