import {Component, Inject, OnDestroy, OnInit} from '@angular/core';
import {OkrTopicDraft} from '../../shared/model/ui/OrganizationalUnit/okr-topic-draft/okr-topic-draft';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {SubmittedTopicDraftDetailsFormData} from '../submitted-topic-draft-details/submitted-topic-draft-details.component';
import {TranslateService} from '@ngx-translate/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {Observable, Subscription} from 'rxjs';
import {DepartmentMapper} from '../../shared/services/mapper/department.mapper';
import {OkrDepartment} from '../../shared/model/ui/OrganizationalUnit/okr-department';
import {Structure} from '../../shared/model/ui/OrganizationalUnit/structure';
import {StructureMapper} from '../../shared/services/mapper/structure.mapper';
import {OkrTopicDescription} from '../../shared/model/ui/OrganizationalUnit/okr-topic-description';
import {TopicDescriptionMapper} from '../../shared/services/mapper/topic-description-mapper';
import {map, switchMap, tap} from 'rxjs/operators';

@Component({
  selector: 'app-submitted-topic-drafts-convert-to-team',
  templateUrl: './convert-submitted-topic-draft-to-team.component.html',
  styleUrls: ['./convert-submitted-topic-draft-to-team.component.scss']
})
export class ConvertSubmittedTopicDraftToTeamComponent implements OnInit, OnDestroy {

  title$: Observable<string>;
  saveAndCloseLabel$: Observable<string>;
  companyStructures$: Observable<Structure[]>;
  chooseStructure: FormGroup;
  private topicDraft: OkrTopicDraft;
  private subscriptions: Subscription[] = [];
  private department: OkrDepartment;

  constructor(
    @Inject(MAT_DIALOG_DATA) private formData: (SubmittedTopicDraftDetailsFormData | any),
    private translate: TranslateService,
    private dialogRef: MatDialogRef<ConvertSubmittedTopicDraftToTeamComponent>,
    private departmentMapper: DepartmentMapper,
    private structureMapper: StructureMapper,
    private topicDescriptionMapper: TopicDescriptionMapper
  ) { }

  ngOnInit(): void {
    this.companyStructures$ = this.structureMapper.getSchemaOfAllExistingStructures$();

    this.topicDraft = this.formData.topicDraft;
    this.chooseStructure = new FormGroup(
      {
        parentUnitId: new FormControl(undefined, [Validators.required])
      }
    );

    this.title$ = this.translate.stream('submitted-topic-draft-convert-to-team.title');
    this.saveAndCloseLabel$ = this.translate.stream('submitted-topic-draft-convert-to-team.dialog.save-and-close-label');
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }

  clickedConvertToTeam() {
    this.subscriptions.push(
      this.convertTopicDraftToTeamAndDescription()
        .subscribe(
        () =>
          this.dialogRef.close(this.department.id)
        )
    );
  }

  createChildUnit$(): Observable<OkrDepartment> {
    this.addDraftDataToDepartment();

    return this.departmentMapper.postDepartmentForOkrBranch$(
      this.department.parentUnitId, DepartmentMapper.mapDepartmentUnit(this.department)
    )
    .pipe(
      tap(department => this.department = department)
    );
  }

  convertTopicDraftToTeamAndDescription(): Observable<OkrTopicDescription>{
      return this.createChildUnit$().pipe(
        switchMap(department => this.getTopicDescriptionForDepartment(department)),
        switchMap(description => this.putUpdatedTopicDescription(description)),
      );
  }

  addDraftDataToDepartment(): void{
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
      true
    );
  }

  addDraftDataToDescription(topicDescription: OkrTopicDescription): OkrTopicDescription{
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
      map(description => this.addDraftDataToDescription(description))
    );
  }

  putUpdatedTopicDescription(topicDescription: OkrTopicDescription): Observable<OkrTopicDescription>{
   return this.topicDescriptionMapper.putTopicDescription$(this.department.id, topicDescription);
  }
}
