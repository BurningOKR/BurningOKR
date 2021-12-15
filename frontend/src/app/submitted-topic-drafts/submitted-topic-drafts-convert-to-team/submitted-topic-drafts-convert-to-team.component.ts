import {Component, EventEmitter, Inject, OnInit} from '@angular/core';
import {OkrTopicDraft} from "../../shared/model/ui/OrganizationalUnit/okr-topic-draft/okr-topic-draft";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {SubmittedTopicDraftDetailsFormData} from "../submitted-topic-draft-details/submitted-topic-draft-details.component";
import {TranslateService} from "@ngx-translate/core";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {OkrUnitSchema} from "../../shared/model/ui/okr-unit-schema";
import {Observable, of} from "rxjs";
import {OkrUnitSchemaMapper} from "../../shared/services/mapper/okr-unit-schema.mapper";
import {CompanyMapper} from "../../shared/services/mapper/company.mapper";
import {CompanyUnit} from "../../shared/model/ui/OrganizationalUnit/company-unit";
import {TopicDraftMapper} from "../../shared/services/mapper/topic-draft-mapper";
import {DepartmentMapper} from "../../shared/services/mapper/department.mapper";
import {OkrDepartment} from "../../shared/model/ui/OrganizationalUnit/okr-department";
import {OkrChildUnit} from "../../shared/model/ui/OrganizationalUnit/okr-child-unit";
import {UnitType} from "../../shared/model/api/OkrUnit/unit-type.enum";
import {OkrBranch} from "../../shared/model/ui/OrganizationalUnit/okr-branch";
import {concatMap, map, mergeAll, mergeMap, reduce, switchMap, take, tap} from "rxjs/operators";
import {CompanyUnitStructure} from "../../shared/model/ui/OrganizationalUnit/company-unit-structure";

@Component({
  selector: 'app-submitted-topic-drafts-convert-to-team',
  templateUrl: './submitted-topic-drafts-convert-to-team.component.html',
  styleUrls: ['./submitted-topic-drafts-convert-to-team.component.scss']
})
export class SubmittedTopicDraftsConvertToTeamComponent implements OnInit {

  title: string;
  topicDraft: OkrTopicDraft;
  chooseStructure: FormGroup;
  okrUnitSchema$: Observable<OkrUnitSchema[]>;
  okrCompanySchema$: Observable<CompanyUnit[]>;
  okrCompanyStructures: CompanyUnitStructure[];


  constructor(
    @Inject(MAT_DIALOG_DATA) private formData: (SubmittedTopicDraftDetailsFormData | any),
    private translate : TranslateService,
    private okrCompanyService : CompanyMapper,
    private okrSchemaService: OkrUnitSchemaMapper,
    private dialogRef: MatDialogRef<SubmittedTopicDraftsConvertToTeamComponent>,
    private departmentMapper: DepartmentMapper,
  ) { }

  ngOnInit(): void {
    this.topicDraft = this.formData.topicDraft;
    this.okrCompanySchema$= this.okrCompanyService.getActiveCompanies$();

    this.getCompanyStructure()

    this.okrUnitSchema$= this.okrCompanyService.getActiveCompanies$().pipe(
      map ( companies =>
        concatMap(company =>
          this.okrSchemaService.getOkrUnitSchemaOfCompany$(company.id)
        )
      ),
    );

    this.okrUnitSchema$.subscribe(stuff => console.log(stuff))
    this.okrCompanySchema$.subscribe(
      stuff => this.getOkrUnitsForCompany(stuff)
    )

    this.chooseStructure = new FormGroup({
      parentUnitId: new FormControl(undefined, [Validators.required])
    });

    this.translate.stream('submitted-topic-draft-convert-to-team.title').subscribe(text =>
      this.title = text
    )
  }

  private appendChildUnitsToCompanies(companyUnits: CompanyUnit[]): CompanyUnitStructure[]{
    (CompanyUnitStructure)companyUnits
      .forEach(
        function (value){
          this.getOkrUnitsForCompany(value)
        }
      )
  }

  private getOkrUnitsForCompany(companyUnit: CompanyUnit): CompanyUnit {
    companyUnit.
  }

  clickedConvertToTeam() {
    this.createChildUnit()
    this.dialogRef.close(
      true
    );
  }

  createChildUnit(): void {
    const okrDepartment: OkrDepartment = {
      id: undefined,
      parentUnitId: this.chooseStructure.get('parentUnitId').value,
      objectives: [],
      name: this.topicDraft.name,
      label: this.translate.instant('user-form.department'),
      isActive: true,
      isParentUnitABranch: false,
      okrMasterId: undefined,
      okrMemberIds: undefined,
      okrTopicSponsorId: undefined
    };
    this.departmentMapper.postDepartmentForOkrBranch$(okrDepartment.parentUnitId, DepartmentMapper.mapDepartmentUnit(okrDepartment))

  }

  private getCompanyStructure() {
    this.okrCompanyStructures = this.okrCompanyService.getActiveCompanies$().pipe(
      map(companies =>
        this.appendChildUnitsToCompanies(companies)
      )
    );

  }
}
