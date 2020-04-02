import { SubstructureFormComponent } from '../substructure-form/substructure-form.component';
import { ContextRole } from '../../../shared/services/helper/department-context-role.service';
import { filter, switchMap, take } from 'rxjs/operators';
import { CurrentOkrviewService } from '../../current-okrview.service';
import { CycleUnit } from '../../../shared/model/ui/cycle-unit';
import { Component, Input, OnDestroy } from '@angular/core';
import { DepartmentUnit } from '../../../shared/model/ui/OrganizationalUnit/department-unit';
import { MatDialog, MatDialogRef } from '@angular/material';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-substructures-tab',
  templateUrl: './sub-structures-tab.component.html',
  styleUrls: ['./sub-structures-tab.component.scss']
})
export class SubStructuresTabComponent implements OnDestroy {
  @Input() department: DepartmentUnit;
  @Input() currentUserRole: ContextRole;
  @Input() cycle: CycleUnit;

  subscriptions: Subscription[] = [];

  constructor(private matDialog: MatDialog, private currentOkrViewService: CurrentOkrviewService) {}

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe());
    this.subscriptions = [];
  }

  clickedAddSubDepartment(): void {
    const dialogReference: MatDialogRef<SubstructureFormComponent> = this.matDialog.open(SubstructureFormComponent, {
      data: { departmentId: this.department.id }
    });

    this.subscriptions.push(
      dialogReference
        .afterClosed()
        .pipe(
          take(1),
          filter(v => v),
          switchMap(n => n)
        )
        .subscribe(addedSubDepartment => this.onSubDepartmentAdded(addedSubDepartment as DepartmentUnit))
    );
  }

  onSubDepartmentAdded(addedSubDepartment: DepartmentUnit): void {
    this.department.subDepartmentIds.push(addedSubDepartment.id);
    this.currentOkrViewService.refreshCurrentDepartmentView(this.department.id);
  }
}
