import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { ContextRole } from '../../../shared/services/helper/department-context-role.service';
import { KeyResultMapper } from '../../../shared/services/mapper/key-result.mapper';
import { filter, switchMap, take } from 'rxjs/operators';
import { CycleUnit } from '../../../shared/model/ui/cycle-unit';
import { ObjectiveViewMapper } from '../../../shared/services/mapper/objective-view.mapper';
import { ConfigurationManagerService } from '../../../core/settings/configuration-manager.service';
import { ViewKeyResult } from '../../../shared/model/ui/view-key-result';
import { MatDialog, MatDialogRef } from '@angular/material';
import { ViewObjective } from '../../../shared/model/ui/view-objective';
import { KeyResultFormComponent } from '../../keyresult/key-result-form/key-result-form.component';
import { ObservableInput, Subscription } from 'rxjs';
import { CdkDragDrop, moveItemInArray } from '@angular/cdk/drag-drop';
import { Configuration } from '../../../shared/model/ui/configuration';

@Component({
  selector: 'app-objective-contents',
  templateUrl: './objective-contents.component.html',
  styleUrls: ['./objective-contents.component.scss']
})
export class ObjectiveContentsComponent implements OnInit, OnDestroy {
  @Input() objective: ViewObjective;
  @Input() currentUserRole: ContextRole;
  @Input() cycle: CycleUnit;

  @Output() objectiveProgressChanged: EventEmitter<number> = new EventEmitter();

  subscription: Subscription;

  parentObjective: ViewObjective;
  keyResultList: ViewKeyResult[];

  maxKeyResults: number;

  constructor(
    private objectiveMapperService: ObjectiveViewMapper,
    private keyResultMapper: KeyResultMapper,
    private matDialog: MatDialog,
    private configurationManagerService: ConfigurationManagerService
  ) {
  }

  ngOnInit(): void {
    this.subscription = this.keyResultMapper.getKeyResultsForObjective$(this.objective.id)
        .subscribe(newKeyResultList => {
        this.keyResultList = newKeyResultList;
        this.updateVisualKeyResultProgressTotals();
      });

    this.subscription = this.configurationManagerService.getMaxKeyResults$()
      .subscribe((maxKeyResults: Configuration) => {
        this.maxKeyResults = +maxKeyResults.value;
      });

    this.refreshParentObjective();
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  // --
  // KeyResult view helpers
  // --

  updateVisualKeyResultProgressTotals(): void {
    let progressValuesCombined: number = 0;
    this.keyResultList.forEach(keyResult => (progressValuesCombined += keyResult.getProgressNormalized()));
    this.objectiveProgressChanged.emit(progressValuesCombined / this.keyResultList.length);
  }

  // --
  // KeyResult ordering
  // --
  dropKeyResult(event: CdkDragDrop<ViewObjective[]>): void {
    moveItemInArray(this.keyResultList, event.previousIndex, event.currentIndex);
    this.queryUpdatedKeyResultOrder();
  }

  moveKeyResultToTop(keyResultToMove: ViewKeyResult): void {
    const currentIndex: number = this.keyResultList.indexOf(keyResultToMove);
    moveItemInArray(this.keyResultList, currentIndex, 0);
    this.queryUpdatedKeyResultOrder();
  }

  moveKeyResultToBottom(keyResultToMove: ViewKeyResult): void {
    const currentIndex: number = this.keyResultList.indexOf(keyResultToMove);
    moveItemInArray(this.keyResultList, currentIndex, this.keyResultList.length - 1);
    this.queryUpdatedKeyResultOrder();
  }

  queryUpdatedKeyResultOrder(): void {
    const sequenceList: number[] = this.calculateKeyResultOrderedIdList();
    this.objectiveMapperService
      .putObjectiveKeyResultSequence$(this.objective.id, sequenceList)
      .pipe(take(1))
      .subscribe();
  }

  calculateKeyResultOrderedIdList(): number[] {
    const keyResultIdList: number[] = [];
    this.keyResultList.forEach(x => keyResultIdList.push(x.id));

    return keyResultIdList;
  }

  // --
  // KeyResult creating new logic
  // --

  clickedAddKeyResult(): void {
    if (this.objective.keyResultIdList.length < this.maxKeyResults) {
      const dialogReference: MatDialogRef<KeyResultFormComponent, ObservableInput<any>> = this.matDialog.open(KeyResultFormComponent, {
        data: {objectiveId: this.objective.id}
      });

      dialogReference
        .afterClosed()
        .pipe(
          take(1),
          filter(v => v as any),
          switchMap(n => n),
          take(1)
        )
        .subscribe(newKeyResult => {
          this.onKeyResultAdded(newKeyResult as ViewKeyResult);
        });
    }
  }

  onKeyResultAdded(newKeyResult: ViewKeyResult): void {
    this.keyResultList.unshift(newKeyResult);
    this.objective.keyResultIdList.unshift(newKeyResult.id);
    this.updateVisualKeyResultProgressTotals();
  }

  refreshParentObjective(): void {
    if (this.objective.hasParentObjective()) {
        this.objectiveMapperService
          .getObjectiveById$(this.objective.parentObjectiveId)
          .pipe(take(1))
          .subscribe(parentObjective => (this.parentObjective = parentObjective));
    } else {
      this.parentObjective = null;
    }
  }
}
