import { KeyResultMapper } from '../../shared/services/mapper/key-result.mapper';
import { debounceTime, distinctUntilChanged, filter, switchMap, take } from 'rxjs/operators';
import { CycleUnit } from '../../shared/model/ui/cycle-unit';
import { Subject, Subscription } from 'rxjs';
import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { ViewKeyResult } from '../../shared/model/ui/view-key-result';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { MatSliderChange } from '@angular/material/slider';
import { ViewObjective } from '../../shared/model/ui/view-objective';
import { KeyResultFormComponent } from './key-result-form/key-result-form.component';
import {
  ConfirmationDialogComponent,
  ConfirmationDialogData
} from '../../shared/components/confirmation-dialog/confirmation-dialog.component';
import {
  CommentViewDialogComponent,
  CommentViewDialogFormData
} from '../comment/comment-view-dialog/comment-view-dialog.component';
import { Unit } from '../../shared/model/api/unit.enum';
import { ContextRole } from '../../shared/model/ui/context-role';
import { ViewCommentParentType } from '../../shared/model/ui/view-comment-parent-type';
import { TranslateService } from "@ngx-translate/core";

@Component({
  selector: 'app-keyresult',
  templateUrl: './keyresult.component.html',
  styleUrls: ['./keyresult.component.scss']
})

export class KeyresultComponent implements OnInit, OnDestroy {
  @Input() keyResult: ViewKeyResult;
  @Input() objective: ViewObjective;
  @Input() keyResultList: ViewKeyResult[];
  @Input() listNumber: number;
  @Input() currentUserRole: ContextRole;
  @Input() cycle: CycleUnit;

  @Output() moveKeyResultToTop: EventEmitter<ViewKeyResult> = new EventEmitter();
  @Output() moveKeyResultToBottom: EventEmitter<ViewKeyResult> = new EventEmitter();
  @Output() keyResultProgressChanged: EventEmitter<ViewKeyResult> = new EventEmitter();

  subscriptions: Subscription[] = [];

  timeInMsToWaitUntilPushingSliderChanges = 2000;
  isKeyResultSliderInverted: boolean = false;
  // We dynamically populate this as sliders are used
  private sliderChangeSubject$: Subject<number>;

  constructor(
    private matDialog: MatDialog,
    private keyResultMapperService: KeyResultMapper,
    private translate: TranslateService) {
  }

  ngOnInit(): void {
    this.updateIsKeyResultSliderInverted();
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe());
    this.subscriptions = [];
  }

  getKeyResultCommentCount(): string | number {
    const commentListSize: number = this.keyResult.commentIdList.length;
    if (commentListSize === 0) {
      return '';
    } else {
      return commentListSize;
    }
  }

  getKeyResultProgressText(): string {
    let progressText: string;
    if (this.keyResult.start === 0) {
      progressText = `${this.keyResult.current}/${this.keyResult.end}`;
    } else {
      progressText = `${this.keyResult.current}/${this.keyResult.start}-${this.keyResult.end}`;
    }

    if (this.keyResult.unit === Unit.EURO) {
      progressText += ' €';
    } else if (this.keyResult.unit === Unit.PERCENT) {
      progressText += ' %';
    }

    return progressText;
  }

  clickedMoveKeyResultToTop(): void {
    this.moveKeyResultToTop.emit(this.keyResult);
  }

  clickedMoveKeyResultToBottom(): void {
    this.moveKeyResultToBottom.emit(this.keyResult);
  }

  isKeyResultOnTop(): boolean {
    return this.keyResultList.indexOf(this.keyResult) === 0;
  }

  isKeyResultOnBottom(): boolean {
    return this.keyResultList.indexOf(this.keyResult) === this.keyResultList.length - 1;
  }

  clickedOpenComments(): void {
    const dialogData: CommentViewDialogFormData = {
      componentTypeTitle: 'Key Result',
      componentName: this.keyResult.keyResult,
      viewCommentParentType: ViewCommentParentType.keyResult,
      parentId: this.keyResult.id,
      onUpdateCommentIdList: this.keyResult.commentIdList
    };
    const dialogReference: MatDialogRef<CommentViewDialogComponent, object> =
      this.matDialog.open(CommentViewDialogComponent, {autoFocus: false, data: dialogData, minWidth: '50vw'});
  }

  clickedDeleteKeyResult(): void {
    const data: ConfirmationDialogData = this.getDataForDeletionDialog();

    const dialogReference: MatDialogRef<ConfirmationDialogComponent, ConfirmationDialogData> =
      this.matDialog.open(ConfirmationDialogComponent, {width: '600px', data});

    this.subscriptions.push(
      dialogReference
        .afterClosed()
        .pipe(take(1))
        .subscribe(isConfirmed => {
          if (isConfirmed) {
            this.queryDeleteKeyResult(this.keyResult);
          }
        })
    );
  }

  queryDeleteKeyResult(keyResultToDelete: ViewKeyResult): void {
    this.subscriptions.push(
      this.keyResultMapperService
        .deleteKeyResult$(keyResultToDelete.id)
        .pipe(take(1))
        .subscribe(() => {
          this.onKeyResultDeleted(keyResultToDelete);
        })
    );
  }

  onKeyResultDeleted(deletedKeyResult: ViewKeyResult): void {
    let keyResultIndex: number = this.keyResultList.indexOf(deletedKeyResult);
    this.keyResultList.splice(keyResultIndex, 1);
    keyResultIndex = this.objective.keyResultIdList.indexOf(deletedKeyResult.id);
    this.objective.keyResultIdList.splice(keyResultIndex, 1);
    this.keyResultProgressChanged.emit(this.keyResult);
  }

  clickedEditKeyResult(): void {
    const dialogReference: MatDialogRef<KeyResultFormComponent, any> = this.matDialog.open(KeyResultFormComponent, {
      data: { keyResult: this.keyResult }, width: '75%'
    });

    this.subscriptions.push(
      dialogReference
        .afterClosed()
        .pipe(
          take(1),
          filter(v => v),
          switchMap(n => n),
          take(1),
        )
        .subscribe(editedKeyResult => this.onKeyResultEdited(editedKeyResult as ViewKeyResult))
    );
  }

  onKeyResultEdited(editedKeyResult: ViewKeyResult): void {
    this.updateIsKeyResultSliderInverted();

    const keyResultIndex: number = this.keyResultList.indexOf(editedKeyResult);
    this.keyResultList[keyResultIndex] = editedKeyResult;
    this.keyResultProgressChanged.emit(this.keyResult);
  }

  /**
   * We want the Frontend to wait a bit for the user to refine their slider input before sending a POST request.
   * Therefore we create a subject for each changed KeyResult (or use an existing one).
   * We subscribe to the subject with the pipe conditions to filter out the same value and add a delay from the last change.
   */
  onKeyResultSliderDropped(sliderChange: MatSliderChange): void {
    if (this.sliderChangeSubject$) {
      this.sliderChangeSubject$.next(sliderChange.value);
    } else {
      this.sliderChangeSubject$ = new Subject<number>();
      this.subscriptions.push(
        this.sliderChangeSubject$
          .pipe(
            debounceTime(this.timeInMsToWaitUntilPushingSliderChanges),
            distinctUntilChanged()
          )
          .subscribe(newValue => this.onKeyResultSliderChangeApplied(newValue))
      );
      this.sliderChangeSubject$.next(sliderChange.value);
    }
    this.keyResult.current = sliderChange.value;
    this.keyResultProgressChanged.emit(this.keyResult);
  }

  onKeyResultSliderChangeApplied(newCurrentValue: number): void {
    this.keyResult.current = newCurrentValue;
    this.subscriptions.push(
      this.keyResultMapperService
        .putKeyResult$(this.keyResult)
        .subscribe(editedKeyResult => this.onKeyResultEdited(editedKeyResult))
    );
  }

  private updateIsKeyResultSliderInverted(): void {
    this.isKeyResultSliderInverted = this.keyResult.start < this.keyResult.end;
  }

  private getDataForDeletionDialog(): ConfirmationDialogData {
    const title: string = this.translate.instant('keyresult.deletion-dialog.title');
    const message: string = this.translate.instant('keyresult.deletion-dialog.message',
      {number: this.listNumber, keyResultTitle: this.keyResult.keyResult});
    const confirmButtonText: string = this.translate.instant('keyresult.deletion-dialog.button-text');

    return {
      title,
      message,
      confirmButtonText
    };
  }
}
