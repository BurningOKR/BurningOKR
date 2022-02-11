import { Component, Inject, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { TranslateService } from '@ngx-translate/core';
import { Observable } from 'rxjs';
import { map, switchMap, take, tap } from 'rxjs/operators';
import { OkrDepartment } from '../../../shared/model/ui/OrganizationalUnit/okr-department';
import { OkrTopicDescription } from '../../../shared/model/ui/OrganizationalUnit/okr-topic-description';
import { OkrTopicDraft } from '../../../shared/model/ui/OrganizationalUnit/okr-topic-draft/okr-topic-draft';
import { Structure } from '../../../shared/model/ui/OrganizationalUnit/structure';
import { DepartmentMapper } from '../../../shared/services/mapper/department.mapper';
import { StructureMapper } from '../../../shared/services/mapper/structure.mapper';
import { TopicDescriptionMapper } from '../../../shared/services/mapper/topic-description-mapper';
import {
  SubmittedTopicDraftDetailsFormData
} from '../../topic-draft-details-dialogue-component/topic-draft-details-dialogue.component';

@Component({
  selector: 'app-convert-topic-draft-to-team-dialogue',
  templateUrl: './convert-topic-draft-to-team-dialogue.component.html',
  styleUrls: ['./convert-topic-draft-to-team-dialogue.component.scss'],
})
export class ConvertTopicDraftToTeamDialogueComponent implements OnInit {

  title$: Observable<string>;
  saveAndCloseLabel$: Observable<string>;
  companyStructures$: Observable<Structure[]>;
  chooseStructure: FormGroup;
  private topicDraft: OkrTopicDraft;
  private department: OkrDepartment;

  constructor(
    @Inject(MAT_DIALOG_DATA) private formData: (SubmittedTopicDraftDetailsFormData | any),
    private translate: TranslateService,
    private dialogRef: MatDialogRef<ConvertTopicDraftToTeamDialogueComponent>,
    private departmentMapper: DepartmentMapper,
    private structureMapper: StructureMapper,
    private topicDescriptionMapper: TopicDescriptionMapper,
  ) {
  }

  ngOnInit(): void {
    this.companyStructures$ = this.structureMapper.getSchemaOfAllExistingStructures$();

    this.topicDraft = this.formData.topicDraft;
    this.chooseStructure = new FormGroup(
      {
        parentUnitId: new FormControl(undefined, [Validators.required]),
      },
    );

    this.title$ = this.translate.stream('submitted-topic-draft-convert-to-team.title');
    this.saveAndCloseLabel$ = this.translate.stream('submitted-topic-draft-convert-to-team.dialog.save-and-close-label');
  }

  clickedConvertToTeam() {
    this.convertTopicDraftToTeamAndDescription().pipe(take(1))
      .subscribe(
        () =>
          this.dialogRef.close(this.department.id),
      );
  }

  createChildUnit$(): Observable<OkrDepartment> {
    this.addDraftDataToDepartment();

    return this.departmentMapper.postDepartmentForOkrBranch$(
        this.department.parentUnitId, DepartmentMapper.mapDepartmentUnit(this.department),
      )
      .pipe(
        tap(department => this.department = department),
      );
  }

  convertTopicDraftToTeamAndDescription(): Observable<OkrTopicDescription> {
    return this.createChildUnit$().pipe(
      switchMap(department => this.getTopicDescriptionForDepartment(department)),
      switchMap(description => this.putUpdatedTopicDescription(description)),
    );
  }

  addDraftDataToDepartment(): void {
    this.department = new OkrDepartment(
      undefined,
      this.topicDraft.name,
      [],
      this.chooseStructure.get('parentUnitId').value,
      this.translate.instant('user-form.department'),
      this.topicDraft.initiatorId,
      undefined,
      this.topicDraft.startTeam,
      true,
      true,
    );
  }

  addDraftDataToDescription(topicDescription: OkrTopicDescription): OkrTopicDescription {
    topicDescription.beginning = this.topicDraft.beginning;
    topicDescription.delimitation = this.topicDraft.delimitation;
    topicDescription.resources = this.topicDraft.resources;
    topicDescription.dependencies = this.topicDraft.dependencies;
    topicDescription.handoverPlan = this.topicDraft.handoverPlan;
    topicDescription.contributesTo = this.topicDraft.contributesTo;
    topicDescription.description = this.topicDraft.description;

    return topicDescription;
  }

  getTopicDescriptionForDepartment(department: OkrDepartment): Observable<OkrTopicDescription> {
    return this.topicDescriptionMapper.getTopicDescriptionById$(department.id).pipe(
      map(description => this.addDraftDataToDescription(description)),
    );
  }

  putUpdatedTopicDescription(topicDescription: OkrTopicDescription): Observable<OkrTopicDescription> {
    return this.topicDescriptionMapper.putTopicDescription$(this.department.id, topicDescription);
  }
}
