import { Component, Inject, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Observable } from 'rxjs/internal/Observable';
import { OkrTopicDescription } from '../../../../../shared/model/ui/OrganizationalUnit/okr-topic-description';
import { TopicDescriptionMapper } from '../../../../../shared/services/mapper/topic-description-mapper';
import { DepartmentId } from '../../../../../shared/model/id-types';
import { TranslateService } from '@ngx-translate/core';

interface OkrTopicDescriptionFormData {
  departmentId: DepartmentId;
  okrTopicDescription: OkrTopicDescription;
}

@Component({
  selector: 'app-department-description-edit-form',
  templateUrl: './department-description-edit-form.component.html',
  styleUrls: ['./department-description-edit-form.component.scss'],
})
export class DepartmentDescriptionEditFormComponent implements OnInit {
  descriptionForm: FormGroup;
  title$: Observable<string>;

  constructor(
    private okrTopicDescriptionService: TopicDescriptionMapper,
    private dialogRef: MatDialogRef<DepartmentDescriptionEditFormComponent>,
    private translate: TranslateService,
    @Inject(MAT_DIALOG_DATA) private formData: OkrTopicDescriptionFormData,
  ) {
  }

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
      stakeholders: new FormControl([]),
    });

    if (this.formData.okrTopicDescription) {
      this.descriptionForm.patchValue(this.formData.okrTopicDescription);
    }

    this.title$ = this.translate.stream('department-description-edit-form.edit-description');
  }

  saveDescription(): void {
    const okrTopicDescription: OkrTopicDescription = this.descriptionForm.getRawValue();
    this.okrTopicDescriptionService.putTopicDescription$(this.formData.departmentId, okrTopicDescription)
      .subscribe(() => this.dialogRef.close());
  }
}
