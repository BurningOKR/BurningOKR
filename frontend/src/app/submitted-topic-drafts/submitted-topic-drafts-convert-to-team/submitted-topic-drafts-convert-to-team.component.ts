import {Component, Inject, OnDestroy, OnInit} from '@angular/core';
import {OkrTopicDraft} from "../../shared/model/ui/OrganizationalUnit/okr-topic-draft/okr-topic-draft";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {SubmittedTopicDraftDetailsFormData} from "../submitted-topic-draft-details/submitted-topic-draft-details.component";
import {TranslateService} from "@ngx-translate/core";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {Observable, Subscription} from "rxjs";
import {DepartmentMapper} from "../../shared/services/mapper/department.mapper";
import {OkrDepartment} from "../../shared/model/ui/OrganizationalUnit/okr-department";
import {Structure} from "../../shared/model/ui/OrganizationalUnit/structure";
import {StructureMapper} from "../../shared/services/mapper/structure.mapper";
import {OkrTopicDescription} from "../../shared/model/ui/OrganizationalUnit/okr-topic-description";
import {TopicDescriptionMapper} from "../../shared/services/mapper/topic-description-mapper";
import {map, switchMap, tap} from "rxjs/operators";

@Component({
  selector: 'app-submitted-topic-drafts-convert-to-team',
  templateUrl: './submitted-topic-drafts-convert-to-team.component.html',
  styleUrls: ['./submitted-topic-drafts-convert-to-team.component.scss']
})
export class SubmittedTopicDraftsConvertToTeamComponent implements OnInit, OnDestroy {

  public title: string;
  public saveAndCloseLabel: string;
  public companyStructures$: Observable<Structure[]>;
  public chooseStructure: FormGroup;
  private topicDraft: OkrTopicDraft;
  private subscriptions: Subscription[] = [];
  private department : OkrDepartment;

  constructor(
    @Inject(MAT_DIALOG_DATA) private formData: (SubmittedTopicDraftDetailsFormData | any),
    private translate : TranslateService,
    private dialogRef: MatDialogRef<SubmittedTopicDraftsConvertToTeamComponent>,
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

    this.subscriptions.push(this.translate.stream('submitted-topic-draft-convert-to-team.title').subscribe(text =>
      this.title = text
    ));
    this.subscriptions.push(this.translate.stream('submitted-topic-draft-convert-to-team.dialog.save-and-close-label').subscribe(text =>
      this.saveAndCloseLabel = text
    ));
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }

  clickedConvertToTeam() {
    this.convertTopicDraftToTeamAndDescription()
    this.dialogRef.close(
      true
    );
  }
  //Noch Abfrage ob Struktur ein Unternehmen ist noch benötigt!
  // -> Company hat keine Parent-Id

  createChildUnit$(): Observable<OkrDepartment> {
    this.addDraftDataToDepartment();

    return this.departmentMapper.postDepartmentForOkrBranch$(
      this.department.parentUnitId, DepartmentMapper.mapDepartmentUnit(this.department)
    )
    .pipe(
      tap(department => this.department = department)
    )
  }

  convertTopicDraftToTeamAndDescription(): void{
    this.subscriptions.push(
      this.createChildUnit$().pipe(
        switchMap(department => this.getTopicDescriptionForDepartment(department)),
        switchMap( description => this.putUpdatedTopicDescription(description)),
      ).subscribe()
    )
  }

  addDraftDataToDepartment(): void{
    this.department = {
      id: undefined,
      parentUnitId: this.chooseStructure.get('parentUnitId').value,
      objectives: [],
      name: this.topicDraft.name,
      label: this.translate.instant('user-form.department'),
      isActive: true,
      isParentUnitABranch: true,
      okrMasterId: this.topicDraft.initiatorId,
      okrMemberIds: this.topicDraft.startTeam,
      okrTopicSponsorId: undefined
    };
  }

  addDraftDataToDescription(topicDescription: OkrTopicDescription): OkrTopicDescription{
    topicDescription.beginning = this.topicDraft.beginning;
    topicDescription.delimitation = this.topicDraft.delimitation;
    topicDescription.resources = this.topicDraft.resources;
    topicDescription.dependencies = this.topicDraft.dependencies;
    topicDescription.handoverPlan = this.topicDraft.handoverPlan;
    topicDescription.contributesTo = this.topicDraft.contributesTo;
    topicDescription.description = this.topicDraft.description;
    console.log(topicDescription);
    return topicDescription;
  }

  getTopicDescriptionForDepartment(department: OkrDepartment):Observable<OkrTopicDescription> {
    return this.topicDescriptionMapper.getTopicDescriptionById$(department.id).pipe(
      map(description => this.addDraftDataToDescription(description))
    )
  }

  putUpdatedTopicDescription(topicDescription: OkrTopicDescription): Observable<OkrTopicDescription>{
    console.log(topicDescription)
    return this.topicDescriptionMapper.putTopicDescription$(this.department.id, topicDescription);
  }
}
