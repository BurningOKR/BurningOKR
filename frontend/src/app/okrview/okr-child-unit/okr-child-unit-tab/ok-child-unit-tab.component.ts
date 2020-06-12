import { OkrChildUnitFormComponent } from '../okr-child-unit-form/okr-child-unit-form.component';
import { filter, switchMap, take } from 'rxjs/operators';
import { CurrentOkrviewService } from '../../current-okrview.service';
import { CycleUnit } from '../../../shared/model/ui/cycle-unit';
import { Component, Input, OnDestroy } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material';
import { Subscription } from 'rxjs';
import { ContextRole } from '../../../shared/model/ui/context-role';
import { OkrBranch } from '../../../shared/model/ui/OrganizationalUnit/okr-branch';
import { OkrChildUnit } from '../../../shared/model/ui/OrganizationalUnit/okr-child-unit';

@Component({
  selector: 'app-okr-child-unit-tab',
  templateUrl: './ok-child-unit-tab.component.html',
  styleUrls: ['./ok-child-unit-tab.component.scss']
})
export class OkChildUnitTabComponent implements OnDestroy {
  @Input() okrBranch: OkrBranch;
  @Input() currentUserRole: ContextRole;
  @Input() cycle: CycleUnit;

  subscriptions: Subscription[] = [];

  constructor(private matDialog: MatDialog, private currentOkrViewService: CurrentOkrviewService) {}

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe());
    this.subscriptions = [];
  }

  clickedAddSubDepartment(): void {
    const dialogReference: MatDialogRef<OkrChildUnitFormComponent> = this.matDialog.open(OkrChildUnitFormComponent, {
      data: { childUnitId: this.okrBranch.id }
    });

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

  onSubDepartmentAdded(addedChildUnit: OkrChildUnit): void {
    this.okrBranch.childUnit.push(addedChildUnit.id);
    this.currentOkrViewService.refreshCurrentDepartmentView(this.okrBranch.id);
  }
}
