import { Component, Inject, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { TranslateService } from '@ngx-translate/core';
import { Observable } from 'rxjs';
import { take } from 'rxjs/operators';
import { OkrTopicDraft } from '../../../shared/model/ui/OrganizationalUnit/okr-topic-draft/okr-topic-draft';
import { Structure } from '../../../shared/model/ui/OrganizationalUnit/structure';
import { StructureMapper } from '../../../shared/services/mapper/structure.mapper';
import {
  SubmittedTopicDraftDetailsFormData
} from '../../topic-draft-details-dialogue-component/topic-draft-details-dialogue.component';
import { ConvertTopicDraftToTeamService } from '../convert-topic-draft-to-team.service';

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

  constructor(
    @Inject(MAT_DIALOG_DATA) private formData: (SubmittedTopicDraftDetailsFormData | any),
    private translate: TranslateService,
    private dialogRef: MatDialogRef<ConvertTopicDraftToTeamDialogueComponent>,
    private structureMapper: StructureMapper,
    private convertTopicDraftToTeamService: ConvertTopicDraftToTeamService) { }

  ngOnInit(): void {
    this.companyStructures$ = this.structureMapper.getSchemaOfAllExistingStructures$();

    this.topicDraft = this.formData.topicDraft;
    this.chooseStructure = new FormGroup(
      {
        parentUnitId: new FormControl(undefined, [Validators.required]),
      },
    );

    this.title$ = this.translate.stream('convert-topic-draft-to-team.title');
    this.saveAndCloseLabel$ = this.translate.stream('convert-topic-draft-to-team.dialog.save-and-close-label');

    this.convertTopicDraftToTeamService.getSelectedUnit$()
      .subscribe(substructure => this.chooseStructure.controls.parentUnitId.setValue(substructure.id));
  }

  clickedConvertToTeam() {
    this.convertTopicDraftToTeamService.convertTopicDraftToTeam$(this.topicDraft.id)
      .pipe(take(1))
      .subscribe(okrDepartment => this.dialogRef.close(okrDepartment.id));
  }
}
