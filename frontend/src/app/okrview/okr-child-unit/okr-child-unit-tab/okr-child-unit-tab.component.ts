import { OkrChildUnitFormComponent } from '../okr-child-unit-form/okr-child-unit-form.component';
import { filter, switchMap, take } from 'rxjs/operators';
import { CurrentOkrviewService } from '../../current-okrview.service';
import { CycleUnit } from '../../../shared/model/ui/cycle-unit';
import { Component, Input, OnDestroy } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Subscription } from 'rxjs';
import { ContextRole } from '../../../shared/model/ui/context-role';
import { OkrBranch } from '../../../shared/model/ui/OrganizationalUnit/okr-branch';
import { OkrChildUnit } from '../../../shared/model/ui/OrganizationalUnit/okr-child-unit';
import { UnitType } from '../../../shared/model/api/OkrUnit/unit-type.enum';
import { I18n } from '@ngx-translate/i18n-polyfill';

@Component({
  selector: 'app-okr-child-unit-tab',
  templateUrl: './okr-child-unit-tab.component.html',
  styleUrls: ['./okr-child-unit-tab.component.scss']
})
export class OkrChildUnitTabComponent implements OnDestroy {
  @Input() okrBranch: OkrBranch;
  @Input() currentUserRole: ContextRole;
  @Input() cycle: CycleUnit;

  subscriptions: Subscription[] = [];

  constructor(private matDialog: MatDialog, private currentOkrViewService: CurrentOkrviewService) {}

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe());
    this.subscriptions = [];
  }

  clickedAddChildOkrBranch(): void {
    const dialogReference: MatDialogRef<OkrChildUnitFormComponent> = this.matDialog.open(OkrChildUnitFormComponent, {
      data: { childUnitId: this.okrBranch.id, unitType: UnitType.OKR_BRANCH }
    });
    this.handleChildUnitDialogReference(dialogReference);
  }

  clickedAddChildDepartment(): void {
    const dialogReference: MatDialogRef<OkrChildUnitFormComponent> = this.matDialog.open(OkrChildUnitFormComponent, {
      data: { childUnitId: this.okrBranch.id, unitType: UnitType.DEPARTMENT }
    });
    this.handleChildUnitDialogReference(dialogReference);
  }

  private handleChildUnitDialogReference(dialogReference: MatDialogRef<OkrChildUnitFormComponent>): void {
    this.subscriptions.push(
      dialogReference
        .afterClosed()
        .pipe(
          take(1),
          filter(v => v),
          switchMap(n => n)
        )
        .subscribe(addedChildUnit => this.onSubDepartmentAdded(addedChildUnit as OkrChildUnit))
    );
  }

  private onSubDepartmentAdded(addedChildUnit: OkrChildUnit): void {
    this.okrBranch.okrChildUnitIds.push(addedChildUnit.id);
    this.currentOkrViewService.refreshCurrentDepartmentView(this.okrBranch.id);
  }
}
