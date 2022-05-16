import { Component, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { TranslateService } from '@ngx-translate/core';
import { Subscription } from 'rxjs';
import { filter, switchMap, take } from 'rxjs/operators';
import {
  ConfirmationDialogComponent,
  ConfirmationDialogData,
} from '../../shared/components/confirmation-dialog/confirmation-dialog.component';
import { ContextRole } from '../../shared/model/ui/context-role';
import { CycleUnit } from '../../shared/model/ui/cycle-unit';
import { ViewCommentParentType } from '../../shared/model/ui/view-comment-parent-type';
import { ViewObjective } from '../../shared/model/ui/view-objective';
import { ObjectiveViewMapper } from '../../shared/services/mapper/objective-view.mapper';
import {
  CommentViewDialogComponent,
  CommentViewDialogFormData,
} from '../comment/comment-view-dialog/comment-view-dialog.component';
import { ObjectiveScore, ObjectiveScoringService } from '../objective-scoring.service';
import { ObjectiveContentsComponent } from './objective-contents/objective-contents.component';
import { ObjectiveFormComponent } from './objective-form/objective-form.component';

@Component({
  selector: 'app-objective',
  templateUrl: './objective.component.html',
  styleUrls: ['./objective.component.scss'],
})
export class ObjectiveComponent implements OnInit{
  @Input() objective: ViewObjective;
  @Input() objectiveList: ViewObjective[];
  @Input() listNumber: number;
  @Input() currentUserRole: ContextRole;
  @Input() cycle: CycleUnit;

  @Output() moveObjectiveToTop: EventEmitter<ViewObjective> = new EventEmitter();
  @Output() moveObjectiveToBottom: EventEmitter<ViewObjective> = new EventEmitter();

  @ViewChild('contentsComponent') contentsComponent: ObjectiveContentsComponent;

  isPanelOpen: boolean = false;

  currentObjectiveScore: ObjectiveScore;
  progressValue: number;
  subscriptions: Subscription[] = [];
  childObjectives: ViewObjective[];

  constructor(
    private objectiveMapper: ObjectiveViewMapper,
    private matDialog: MatDialog,
    private objectiveScoringService: ObjectiveScoringService,
    private translate: TranslateService,
  ) {
  }

  ngOnInit(){
    this.objectiveMapper
      .getChildObjectivesOfObjectiveById$(this.objective.id)
      .pipe(take(1))
      .subscribe(childObjective => (this.childObjectives = childObjective));
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

  userIsOkrMember(): boolean {
    return this.currentUserRole.isAtleastOKRMember();
  }

  // --
  // Objective comment logic
  // --

  clickedCommentObjective(): void {

    const dialogData: CommentViewDialogFormData = {
      componentTypeTitle: 'Objective',
      componentName: this.objective.name,
      viewCommentParentType: ViewCommentParentType.objective,
      parentId: this.objective.id,
      onUpdateCommentIdList: this.objective.commentIdList,
    };

    this.matDialog.open(CommentViewDialogComponent, { autoFocus: true, data: dialogData, minWidth: '50vw' });
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
    const title: string = this.translate.instant('objective.deletion-dialog.title');
    const message: string = this.translate.instant('objective.deletion-dialog.message',
      { number: this.listNumber, objectiveTitle: this.objective.name });
    const confirmButtonText: string = this.translate.instant('objective.deletion-dialog.button-text');

    const dialogData: ConfirmationDialogData = {
      title,
      message,
      confirmButtonText,
    };

    const dialogReference: MatDialogRef<ConfirmationDialogComponent, object>
      = this.matDialog.open(ConfirmationDialogComponent, { autoFocus: false, data: dialogData, minWidth: '50vw' });

    dialogReference
      .afterClosed()
      .pipe(take(1))
      .subscribe(isConfirmed => {
        if (isConfirmed) {
          this.queryDeleteObjective();
        }
      });
  }

  queryDeleteObjective(): void {
    this.objectiveMapper
      .deleteObjective$(this.objective.id)
      .pipe(take(1))
      .subscribe(() => {
        this.onObjectiveDeleted();
      });
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
      data: { objective: this.objective },
    });

    dialogReference
      .afterClosed()
      .pipe(
        take(1),
        filter(v => v),
        switchMap(n => n),
        take(1),
      )
      .subscribe(editedObjective => this.onObjectiveEdited(editedObjective as ViewObjective));
  }

  onObjectiveEdited(editedObjective: ViewObjective): void {
    const objectiveIndex: number = this.objectiveList.indexOf(editedObjective);
    this.objectiveList[objectiveIndex] = editedObjective;
    this.contentsComponent.refreshParentObjective();
  }

  toggleWhetherObjectiveIsActive(): void {
    this.objective.isActive = !this.objective.isActive;

    this.objectiveMapper
      .putObjective$(this.objective)
      .pipe(take(1))
      .subscribe(editedObjective => this.onObjectiveEdited(editedObjective));
  }
}
