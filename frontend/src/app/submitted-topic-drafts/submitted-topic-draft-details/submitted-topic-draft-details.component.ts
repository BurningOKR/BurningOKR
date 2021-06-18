import { Component, Inject, Input, OnInit } from '@angular/core';
import { OkrTopicDraft } from '../../shared/model/ui/OrganizationalUnit/okr-topic-draft/okr-topic-draft';
import { status } from '../../shared/model/ui/OrganizationalUnit/okr-topic-draft/okr-topic-draft-status-enum';
import { User } from '../../shared/model/api/user';
import { NEVER, Observable } from 'rxjs';
import { MatDialogRef } from '@angular/material/dialog';
import { MAT_DIALOG_DATA } from '@angular/material';
import { FormControl, FormGroup } from '@angular/forms';
import { ContextRole } from '../../shared/model/ui/context-role';
import { OkrChildUnitRoleService } from '../../shared/services/helper/okr-child-unit-role.service';
import {switchMap} from "rxjs/operators";

export interface SubmittedTopicDraftDetailsFormData {
  topicDraft: OkrTopicDraft;
}

@Component({
  selector: 'app-submitted-topic-draft-details',
  templateUrl: './submitted-topic-draft-details.component.html',
  styleUrls: ['./submitted-topic-draft-details.component.css']
})

export class SubmittedTopicDraftDetailsComponent implements OnInit {

  @Input() topicDraft: OkrTopicDraft;
  @Input() submittedTopicDraftDetailsForm: FormGroup;
  @Input() enumStatus = status;
  @Input() currentUserRole: ContextRole;

  canEdit: boolean;

  constructor(private dialogRef: MatDialogRef<SubmittedTopicDraftDetailsComponent>,
              @Inject(MAT_DIALOG_DATA) private formData: SubmittedTopicDraftDetailsFormData,
              private okrChildUnitRoleService: OkrChildUnitRoleService) {
    this.topicDraft = formData.topicDraft;
    this.submittedTopicDraftDetailsForm = new FormGroup({
        name: new FormControl(this.topicDraft.name),
        currentStatus: new FormControl(this.topicDraft.currentStatus),
        beginning: new FormControl(this.topicDraft.beginning.toLocaleDateString()),
        initiator: new FormControl(this.topicDraft.initiator),
        contributesTo: new FormControl(this.topicDraft.contributesTo),
        handoverPlan: new FormControl(this.topicDraft.handoverPlan),
        dependencies: new FormControl(this.topicDraft.dependencies),
        resources: new FormControl(this.topicDraft.resources)
      }
    );
  }

  closeDialog(): void {
    this.dialogRef.close(NEVER);
  }

  ngOnInit(): void {
    const currentUserRole$: Observable<ContextRole> = this.okrChildUnitRoleService.getRoleWithoutContext$();
    currentUserRole$.subscribe(
      (contextRole: ContextRole)  => {
        this.currentUserRole = contextRole;
        this.canEdit = contextRole.isAtleastAdmin();
        console.log("CanEdit " + this.canEdit);
      }
    );
  }
}
