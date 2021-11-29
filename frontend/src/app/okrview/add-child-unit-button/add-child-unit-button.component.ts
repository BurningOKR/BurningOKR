import { Component, EventEmitter, Input, Output } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { I18n } from '@ngx-translate/i18n-polyfill';
import { take, filter, switchMap } from 'rxjs/operators';
import { OkrBranch } from '../../shared/model/ui/OrganizationalUnit/okr-branch';
import { CompanyUnit } from '../../shared/model/ui/OrganizationalUnit/company-unit';
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

}
