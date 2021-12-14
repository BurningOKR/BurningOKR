import {Component, EventEmitter, Inject, OnInit} from '@angular/core';
import {OkrTopicDraft} from "../../shared/model/ui/OrganizationalUnit/okr-topic-draft/okr-topic-draft";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {SubmittedTopicDraftDetailsFormData} from "../submitted-topic-draft-details/submitted-topic-draft-details.component";
import {TranslateService} from "@ngx-translate/core";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {OkrBranch} from "../../shared/model/ui/OrganizationalUnit/okr-branch";
import {CurrentOkrUnitSchemaService} from "../../okrview/current-okr-unit-schema.service";
import {OkrUnitSchema} from "../../shared/model/ui/okr-unit-schema";
import {Observable} from "rxjs";
import {OkrUnitSchemaMapper} from "../../shared/services/mapper/okr-unit-schema.mapper";
import {CompanyMapper} from "../../shared/services/mapper/company.mapper";
import {CompanyUnit} from "../../shared/model/ui/OrganizationalUnit/company-unit";
import {map} from "rxjs/operators";

@Component({
  selector: 'app-submitted-topic-drafts-convert-to-team',
  templateUrl: './submitted-topic-drafts-convert-to-team.component.html',
  styleUrls: ['./submitted-topic-drafts-convert-to-team.component.scss']
})
export class SubmittedTopicDraftsConvertToTeamComponent implements OnInit {

  title: string;
  topicDraft: OkrTopicDraft;
  chooseStructure: FormGroup;
  okrUnitSchema: OkrUnitSchema[];
  okrCompanySchema$: Observable<CompanyUnit[]>;
  topicDraftDeletedEvent: EventEmitter<any>


  constructor(
    @Inject(MAT_DIALOG_DATA) private formData: (SubmittedTopicDraftDetailsFormData | any),
    private translate : TranslateService,
    private okrCompanyService : CompanyMapper,
    private okrSchemaService: OkrUnitSchemaMapper,
    private dialogRef: MatDialogRef<SubmittedTopicDraftsConvertToTeamComponent>,
  ) { }

  ngOnInit(): void {
    this.topicDraft = this.formData.topicDraft;
    this.topicDraftDeletedEvent = this.formData.topicDraftDeletedEvent;
    this.okrCompanySchema$= this.okrCompanyService.getActiveCompanies$();
    this.chooseStructure = new FormGroup({
      parentUnitId: new FormControl(undefined, [Validators.required])
    });

    this.translate.stream('submitted-topic-draft-convert-to-team.title').subscribe(text =>
      this.title = text
    )
  }

  clickedConvertToTeam() {
    console.log('Ok Button')
    this.dialogRef.close(

    );
    this.topicDraftDeletedEvent.emit();
  }

  private getUnitsfromActiveCompanies() {
    this.okrCompanyService.getActiveCompanies$()

  }

  private getUnitsFrom(companies : CompanyUnit[]) {
    console.log('Zeug soll geholt werden ')
    for(let company of companies){
      console.log(company);
      this.okrSchemaService.getOkrUnitSchemaOfCompany$(company.id).subscribe(
        Units => this.okrUnitSchema.concat(Units)
      );
    }
  }

}
