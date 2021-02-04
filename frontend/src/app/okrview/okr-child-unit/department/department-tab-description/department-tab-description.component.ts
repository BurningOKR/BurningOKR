import { Component, Input, OnChanges, OnInit } from '@angular/core';
import { OkrDepartment } from '../../../../shared/model/ui/OrganizationalUnit/okr-department';
import { ContextRole } from '../../../../shared/model/ui/context-role';
import { TopicDescriptionMapper } from '../../../../shared/services/mapper/topic-description-mapper';
import { OkrTopicDescription } from '../../../../shared/model/ui/OrganizationalUnit/okr-topic-description';
import { Observable } from 'rxjs';
import { MatDialog, MatDialogRef } from '@angular/material';
import { DepartmentDescriptionEditFormComponent } from './department-description-edit-form/department-description-edit-form.component';
import { switchMap, take } from 'rxjs/operators';

@Component({
  selector: 'app-department-tab-description',
  templateUrl: './department-tab-description.component.html',
  styleUrls: ['./department-tab-description.component.scss']
})
export class DepartmentTabDescriptionComponent implements OnInit, OnChanges {
  @Input() department: OkrDepartment;
  @Input() currentUserRole: ContextRole;

  description$: Observable<OkrTopicDescription>;
  canEdit: boolean;

  constructor(
    private topicDescriptionMapperService: TopicDescriptionMapper,
    private dialog: MatDialog
  ) {
  }

  ngOnInit(): void {
    this.updateDescription();
    this.canEdit = this.currentUserRole.isAtleastAdmin() || this.currentUserRole.isAtleastOKRManager();
  }

  ngOnChanges(): void {
    this.updateDescription();
  }

  openDialog(description: OkrTopicDescription): void {
    const dialogReference: MatDialogRef<DepartmentDescriptionEditFormComponent, Observable<any>> =
      this.dialog.open(DepartmentDescriptionEditFormComponent, {
        width: '600px', data: {
          departmentId: this.department.id,
          okrTopicDescription: description
        }
      });

    // TODO P.B. 04.02.2021 Fix dialog not closing when saving. Also maybe change the "Save" button in this form when selecting Topic-Draft?
    dialogReference.afterClosed()
      .pipe(
        switchMap(o$ => o$),
        take(1)
      )
      .subscribe(() => {
        this.updateDescription();
      });

  }

  private updateDescription(): void {
    this.description$ = this.topicDescriptionMapperService.getTopicDescriptionById$(this.department.id);
  }
}
