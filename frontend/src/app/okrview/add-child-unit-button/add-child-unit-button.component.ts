import { Component, EventEmitter, Input, Output } from '@angular/core';
import { MatDialog, MatDialogRef, MatSnackBar } from '@angular/material';
import { I18n } from '@ngx-translate/i18n-polyfill';
import { take, filter, switchMap } from 'rxjs/operators';
import { CompanyUnit } from 'src/app/shared/model/ui/OrganizationalUnit/company-unit';
import { OkrBranch } from 'src/app/shared/model/ui/OrganizationalUnit/okr-branch';
import { ContextRole } from '../../shared/model/ui/context-role';
import { TopicDraftCreationFormComponent } from '../okr-child-unit/okr-child-unit-form/topic-draft-creation-form/topic-draft-creation-form.component';

@Component({
  selector: 'app-add-child-unit-button',
  templateUrl: './add-child-unit-button.component.html',
  styleUrls: ['./add-child-unit-button.component.css']
})
export class AddChildUnitButtonComponent {

  @Input() cycleClosed: boolean;
  @Input() userRole: ContextRole;
  @Input() topicSubject: OkrBranch | CompanyUnit;

  @Output() clickedAddSubDepartment: EventEmitter<void> = new EventEmitter<void>();
  @Output() clickedAddSubBranch: EventEmitter<void> = new EventEmitter<void>();

  constructor(
    private i18n: I18n,
    private snackBar: MatSnackBar,
    private matDialog: MatDialog
  ) { }

  clickedAddTopicDraft(): void {
    // creates fitting config for either okrbranch id or company id
    let config = { width: '600px', data: { } };
    if (this.topicSubject instanceof OkrBranch) {
      config.data = { unitId: this.topicSubject.id };
    } else if (this.topicSubject instanceof CompanyUnit) {
      config.data = { companyId: this.topicSubject.id };
    }

    const dialogReference: MatDialogRef<TopicDraftCreationFormComponent> = this.matDialog.open(TopicDraftCreationFormComponent, config);

    dialogReference
      .afterClosed()
      .pipe(
        take(1),
        filter(v => v),
        switchMap(n => n)
      )
      .subscribe(addedTopicDraft => {
        const snackBarText: string = this.i18n({
          id: 'snackbar_addTopicDraft',
          value: 'Ihr Themenentwurf wurde zur Prüfung abgeschickt.'
        });
        const snackBarOk: string = this.i18n({ id: 'snackbar_ok', value: 'Ok' });
        this.snackBar.open(snackBarText, snackBarOk, { verticalPosition: 'top' });
      });
  }
}
