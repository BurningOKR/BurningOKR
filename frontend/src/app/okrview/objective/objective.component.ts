import { ObjectiveFormComponent } from './objective-form/objective-form.component';
import { filter, switchMap, take } from 'rxjs/operators';
import { Component, EventEmitter, Input, OnDestroy, Output, ViewChild } from '@angular/core';
import { CycleUnit } from '../../shared/model/ui/cycle-unit';
import { ObjectiveViewMapper } from '../../shared/services/mapper/objective-view.mapper';
import { ObjectiveContentsComponent } from './objective-contents/objective-contents.component';
import { MatDialog, MatDialogRef } from '@angular/material';
import { ViewObjective } from '../../shared/model/ui/view-objective';
import {
  ConfirmationDialogComponent,
  ConfirmationDialogData
} from '../../shared/components/confirmation-dialog/confirmation-dialog.component';
import { Subscription } from 'rxjs';
import { ObjectiveScore, ObjectiveScoringService } from '../objective-scoring.service';
import { I18n } from '@ngx-translate/i18n-polyfill';
import { ContextRole } from '../../shared/model/ui/context-role';
@Component({
  selector: 'app-objective',
  templateUrl: './objective.component.html',
  styleUrls: ['./objective.component.scss']
})
export class ObjectiveComponent implements OnDestroy {
  @Input() objective: ViewObjective;
  @Input() objectiveList: ViewObjective[];
  @Input() listNumber: number;
  @Input() currentUserRole: ContextRole;
  @Input() cycle: CycleUnit;

  @Output() moveObjectiveToTop: EventEmitter<ViewObjective> = new EventEmitter();
  @Output() moveObjectiveToBottom: EventEmitter<ViewObjective> = new EventEmitter();

  @ViewChild('contentsComponent', { static: false }) contentsComponent: ObjectiveContentsComponent;

  isPanelOpen: boolean = false;

  currentObjectiveScore: ObjectiveScore;
  progressValue: number;
  subscriptions: Subscription[] = [];

  constructor(
    private objectiveMapper: ObjectiveViewMapper,
    private matDialog: MatDialog,
    private objectiveScoringService: ObjectiveScoringService,
    private i18n: I18n
  ) {
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe());
    this.subscriptions = [];
  }

  isProgressValueSetForObjective(): boolean {
    return this.objective.keyResultIdList.length > 0 && this.progressValue !== undefined && this.currentObjectiveScore !== undefined;
  }

  getProgressValueForObjective(): number {
    return Math.round(this.progressValue * 100);
  }

  updateObjectiveProgress(newValue: number): void {
    this.progressValue = newValue;
    const cycleProgress: number = this.cycle.getCurrentCycleProgressNormalized();
    this.currentObjectiveScore = this.objectiveScoringService.getObjectiveScoreForProgress(newValue, cycleProgress);
  }

  hasSubObjectives(): boolean {
    return this.objective.subObjectivesCount > 0;
  }

  // --
  // Objective comment logic
  // --
  // ToDo (C.K. add functionality)
  clickedCommentObjective(): void {

    // const dialogReference: MatDialogRef<CommentViewDialogComponent, object> =
    //  this.matDialog.open(CommentViewDialogComponent, {autoFocus: false});
  }

  // --
  // Objective ordering logic
  // --

  clickedMoveObjectiveToTop(): void {
    this.moveObjectiveToTop.emit(this.objective);
  }

  clickedMoveObjectiveToBottom(): void {
    this.moveObjectiveToBottom.emit(this.objective);
  }

  isObjectiveOnTop(): boolean {
    return this.objectiveList.indexOf(this.objective) === 0;
  }

  isObjectiveOnBottom(): boolean {
    return this.objectiveList.indexOf(this.objective) === this.objectiveList.length - 1;
  }

  // --
  // Objective delete logic
  // --

  clickedDeleteObjective(): void {
    const title: string =
      this.i18n({
        id: 'deleteObjectiveDialogTitle',
        description: 'Title of the delete objective dialog',
        value: 'Objective löschen'
      });

    const message: string =
      this.i18n({
        id: 'deleteObjectiveDialogMessage',
        description: 'Do you want to delete objective x',
        value: 'Objective {{number}}. {{objectiveTitle}} löschen?',
      }, {number: this.listNumber, objectiveTitle: this.objective.name});

    const confirmButtonText: string = this.i18n({
      id: 'capitalised_delete',
      description: 'deleteButtonText',
      value: 'Löschen'
    });

    const dialogData: ConfirmationDialogData = {
      title,
      message,
      confirmButtonText
    };

    const dialogReference: MatDialogRef<ConfirmationDialogComponent, object>
      = this.matDialog.open(ConfirmationDialogComponent, {width: '600px', data: dialogData});

    this.subscriptions.push(
      dialogReference
        .afterClosed()
        .pipe(take(1))
        .subscribe(isConfirmed => {
          if (isConfirmed) {
            this.queryDeleteObjective();
          }
        })
    );
  }

  queryDeleteObjective(): void {
    this.subscriptions.push(
      this.objectiveMapper
        .deleteObjective$(this.objective.id)
        .pipe(take(1))
        .subscribe(() => {
          this.onObjectiveDeleted();
        })
    );
  }

  onObjectiveDeleted(): void {
    const objectiveIndex: number = this.objectiveList.indexOf(this.objective);
    this.objectiveList.splice(objectiveIndex, 1);
  }

  // --
  // Objective edit existing logic
  // --

  clickedEditObjective(): void {
    const dialogReference: MatDialogRef<ObjectiveFormComponent, any> = this.matDialog.open(ObjectiveFormComponent, {
      data: { objective: this.objective }
    });

    this.subscriptions.push(
      dialogReference
        .afterClosed()
        .pipe(
          take(1),
          filter(v => v),
          switchMap(n => n),
          take(1)
        )
        .subscribe(editedObjective => this.onObjectiveEdited(editedObjective as ViewObjective))
    );
  }

  onObjectiveEdited(editedObjective: ViewObjective): void {
    const objectiveIndex: number = this.objectiveList.indexOf(editedObjective);
    this.objectiveList[objectiveIndex] = editedObjective;
    this.contentsComponent.refreshParentObjective();
  }

  toggleWhetherObjectiveIsActive(): void {
    this.objective.isActive = !this.objective.isActive;

    this.subscriptions.push(
      this.objectiveMapper
        .putObjective$(this.objective)
        .pipe(take(1))
        .subscribe(editedObjective => this.onObjectiveEdited(editedObjective))
    );
  }
}
