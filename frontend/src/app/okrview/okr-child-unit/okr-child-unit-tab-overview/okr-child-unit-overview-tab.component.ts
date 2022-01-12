import { CdkDragDrop, moveItemInArray } from '@angular/cdk/drag-drop';
import { Component, Input, OnChanges, OnDestroy, OnInit, SimpleChanges } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { Subscription } from 'rxjs';
import { filter, switchMap, take } from 'rxjs/operators';
import { ContextRole } from '../../../shared/model/ui/context-role';
import { CycleUnit } from '../../../shared/model/ui/cycle-unit';
import { OkrChildUnit } from '../../../shared/model/ui/OrganizationalUnit/okr-child-unit';
import { ViewObjective } from '../../../shared/model/ui/view-objective';
import { ObjectiveViewMapper } from '../../../shared/services/mapper/objective-view.mapper';
import { OkrUnitService } from '../../../shared/services/mapper/okr-unit.service';
import { ObjectiveFormComponent } from '../../objective/objective-form/objective-form.component';

@Component({
  selector: 'app-okr-child-unit-overview-tab',
  templateUrl: './okr-child-unit-overview-tab.component.html',
  styleUrls: ['./okr-child-unit-overview-tab.component.scss'],
})
export class OkrChildUnitOverviewTabComponent implements OnInit, OnDestroy, OnChanges {
  @Input() okrChildUnit: OkrChildUnit;
  @Input() currentUserRole: ContextRole;
  @Input() cycle: CycleUnit;

  subscriptions: Subscription[] = [];

  objectiveList: ViewObjective[];

  constructor(
    private objectiveMapper: ObjectiveViewMapper,
    private okrUnitService: OkrUnitService,
    private matDialog: MatDialog,
  ) {
  }

  ngOnInit(): void {
    this.subscriptions.push(
      this.objectiveMapper
        .getObjectivesForUnit$(this.okrChildUnit.id).pipe(take(1))
        .subscribe(newObjectiveList => (this.objectiveList = newObjectiveList)),
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe());
    this.subscriptions = [];
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes.okrChildUnit) {
      this.objectiveList = undefined;
      this.objectiveMapper
        .getObjectivesForUnit$(this.okrChildUnit.id).pipe(take(1))
        .subscribe(newObjectiveList => (this.objectiveList = newObjectiveList));
    }
  }

  // --
  // Objective ordering logic
  // --
  dropObjective(event: CdkDragDrop<ViewObjective[]>): void {
    moveItemInArray(this.objectiveList, event.previousIndex, event.currentIndex);
    this.queryUpdatedObjectiveOrder();
  }

  moveObjectiveToTop(objectiveToMove: ViewObjective): void {
    const currentIndex: number = this.objectiveList.indexOf(objectiveToMove);
    moveItemInArray(this.objectiveList, currentIndex, 0);
    this.queryUpdatedObjectiveOrder();
  }

  moveObjectiveToBottom(objectiveToMove: ViewObjective): void {
    const currentIndex: number = this.objectiveList.indexOf(objectiveToMove);
    moveItemInArray(this.objectiveList, currentIndex, this.objectiveList.length - 1);
    this.queryUpdatedObjectiveOrder();
  }

  queryUpdatedObjectiveOrder(): void {
    const sequenceList: number[] = this.calculateDepartmentOrderedIdList();
    this.okrUnitService
      .putOkrUnitObjectiveSequence$(this.okrChildUnit.id, sequenceList)
      .pipe(take(1))
      .subscribe();
  }

  calculateDepartmentOrderedIdList(): number[] {
    const objectiveIdList: number[] = [];
    this.objectiveList.forEach(x => objectiveIdList.push(x.id));

    return objectiveIdList;
  }

  // --
  // Objective add new logic
  // --

  clickedAddObjective(): void {
    const dialogReference: MatDialogRef<ObjectiveFormComponent> = this.matDialog.open(ObjectiveFormComponent, {
      data: { unitId: this.okrChildUnit.id },
    });

    this.subscriptions.push(
      dialogReference
        .afterClosed()
        .pipe(
          take(1),
          filter(v => v),
          switchMap(n => n),
        )
        .subscribe(newObjective => this.onObjectiveAdded(newObjective as ViewObjective)),
    );
  }

  onObjectiveAdded(newObjective: ViewObjective): void {
    this.objectiveList.unshift(newObjective);
    this.okrChildUnit.objectives.unshift(newObjective.id);
  }
}
