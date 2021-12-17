import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { OkrTopicDraft } from '../../shared/model/ui/OrganizationalUnit/okr-topic-draft/okr-topic-draft';
import {
  ConfirmationDialogComponent,
  ConfirmationDialogData
} from '../../shared/components/confirmation-dialog/confirmation-dialog.component';
import { switchMap, take } from 'rxjs/operators';
import { of, Subscription } from 'rxjs';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { TopicDraftMapper } from '../../shared/services/mapper/topic-draft-mapper';
import { CurrentUserService } from '../../core/services/current-user.service';
import { SubmittedTopicDraftEditComponent } from '../submitted-topic-draft-edit/submitted-topic-draft-edit.component';
import { status } from '../../shared/model/ui/OrganizationalUnit/okr-topic-draft/okr-topic-draft-status-enum';
import { Observable } from 'rxjs/internal/Observable';
import {
  CommentViewDialogComponent,
  CommentViewDialogFormData
} from '../../okrview/comment/comment-view-dialog/comment-view-dialog.component';
import { ViewCommentParentType } from '../../shared/model/ui/view-comment-parent-type';
import { TranslateService } from '@ngx-translate/core';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-submitted-topic-draft-action-button',
  templateUrl: './submitted-topic-draft-action-button.component.html',
  styleUrls: ['./submitted-topic-draft-action-button.component.css']
})
export class SubmittedTopicDraftActionButtonComponent implements OnDestroy, OnInit {
  @Input() topicDraft: OkrTopicDraft;
  @Output() topicDraftDeletedEvent = new EventEmitter();
  @Output() editedTopicDraftEvent: EventEmitter<OkrTopicDraft> = new EventEmitter<OkrTopicDraft>();

  subscriptions: Subscription[] = [];

  editTooltipStatus: string;
  editTooltipUser: string;
  editTooltipStatusAndUser: string;
  stateMustBeSubmittedTooltip: string;
  userRoleToChangeStatus: string;
  changeCurrentStatusByStatusAndUser: string;
  approveTopicDraftText: string;
  withDrawApprovalTopicDraftText: string;
  rejectTopicDraftText: string;
  withDrawRejectionTopicDraftText: string;
  submitTopicDraftText: string;
  withDrawSubmitTopicDraftText: string;
  adminOrInitiatorTooltip: string;
  statusMustBeSubmitted: string;
  statusMustBeSubmittedAndUser: string;

  constructor(private topicDraftMapper: TopicDraftMapper,
              private currentUserService: CurrentUserService,
              private translate: TranslateService,
              private dialog: MatDialog,
              private snackBar: MatSnackBar) {
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe());
    this.subscriptions = [];
  }

  ngOnInit(): void {
    this.subscriptions.push(this.translate.stream('submitted-topic-draft-action-button.edit-tooltip.status')
      .subscribe((text: string) => {
      this.editTooltipStatus = text;
    }));
    this.subscriptions.push(this.translate.stream('submitted-topic-draft-action-button.edit-tooltip.user')
      .subscribe((text: string) => {
      this.editTooltipUser = text;
    }));
    this.subscriptions.push(this.translate.stream('submitted-topic-draft-action-button.edit-tooltip.user')
      .subscribe((text: string) => {
      this.editTooltipStatusAndUser = text;
    }));
    this.subscriptions.push(this.translate.stream('submitted-topic-draft-action-button.edit-tooltip.user')
      .subscribe((text: string) => {
      this.stateMustBeSubmittedTooltip = text;
    }));
    this.subscriptions.push(this.translate.stream('submitted-topic-draft-action-button.no-permission')
      .subscribe((text: string) => {
      this.userRoleToChangeStatus = text;
    }));
    this.subscriptions.push(this.translate.stream('submitted-topic-draft-action-button.approving-status-and-user')
      .subscribe((text: string) => {
      this.changeCurrentStatusByStatusAndUser = text;
    }));
    this.subscriptions.push(this.translate.stream('submitted-topic-draft-action-button.capitalised-approve')
      .subscribe((text: string) => {
      this.approveTopicDraftText = text;
    }));
    this.subscriptions.push(this.translate.stream('submitted-topic-draft-action-button.withdraw-approval')
      .subscribe((text: string) => {
      this.withDrawApprovalTopicDraftText = text;
    }));
    this.subscriptions.push(this.translate.stream('submitted-topic-draft-action-button.capitalized-reject')
      .subscribe((text: string) => {
      this.rejectTopicDraftText = text;
    }));
    this.subscriptions.push(this.translate.stream('submitted-topic-draft-action-button.withdraw-rejection')
      .subscribe((text: string) => {
      this.withDrawRejectionTopicDraftText = text;
    }));
    this.subscriptions.push(this.translate.stream('submitted-topic-draft-action-button.capitalized-submit')
      .subscribe((text: string) => {
      this.submitTopicDraftText = text;
    }));
    this.subscriptions.push(this.translate.stream('submitted-topic-draft-action-button.withdraw-submit')
      .subscribe((text: string) => {
      this.withDrawSubmitTopicDraftText = text;
    }));
    this.subscriptions.push(this.translate.stream('submitted-topic-draft-action-button.admin-initator-tooltip')
      .subscribe((text: string) => {
      this.adminOrInitiatorTooltip = text;
    }));
    this.subscriptions.push(this.translate.stream('submitted-topic-draft-action-button.status-submitted')
      .subscribe((text: string) => {
      this.statusMustBeSubmitted = text;
    }));
    this.subscriptions.push(this.translate.stream('submitted-topic-draft-action-button.status-submitted-user')
      .subscribe((text: string) => {
      this.statusMustBeSubmittedAndUser = text;
    }));

  }

  printNotImplemented(): string {
    // eslint-disable-next-line no-console
    console.log('Not Implemented');

    return 'Not Implemented';
  }

  editTopicDraft(): void {
    const data: object = {
      data: {
        topicDraft: this.topicDraft,
        editedTopicDraftEvent: this.editedTopicDraftEvent
      }
    };
    this.dialog.open(SubmittedTopicDraftEditComponent, data);
  }

  clickedDeleteTopicDraft(): void {
    const title: string = this.translate.instant('submitted-topic-draft-action-button.delete.title');
    const message: string = this.translate.instant('submitted-topic-draft-action-button.delete.message', {name: this.topicDraft.name});
    const confirmButtonText: string = this.translate.instant('submitted-topic-draft-action-button.delete.button-text');

    const dialogData: ConfirmationDialogData = {
      title,
      message,
      confirmButtonText
    };

    const dialogReference: MatDialogRef<ConfirmationDialogComponent, object>
      = this.dialog.open(ConfirmationDialogComponent, {width: '600px', data: dialogData});

    this.subscriptions.push(
      dialogReference
        .afterClosed()
        .pipe(take(1))
        .subscribe(isConfirmed => {
          if (isConfirmed) {
            this.deleteTopicDraft();
          }
        })
    );
  }

  deleteTopicDraft(): void {
  this.subscriptions.push(
      this.topicDraftMapper
          .deleteTopicDraft$(this.topicDraft.id)
          .pipe(take(1))
          .subscribe(() => this.topicDraftDeletedEvent.emit()
          ));
  }

  canEditTopicDraft$(): Observable<boolean> {
    return this.currentUserService.isCurrentUserAdminOrCreator$(this.topicDraft.initiatorId)
      .pipe(
        switchMap((hasAuthorization: boolean) => {
          return of(hasAuthorization && (this.topicDraft.currentStatus === status.draft
            || this.topicDraft.currentStatus === status.submitted));
        })
      );
  }

  canDeleteTopicDraft$(): Observable<boolean> {
    return this.currentUserService.isCurrentUserAdminOrCreator$(this.topicDraft.initiatorId);
  }

  canChangeCurrentStatus$(): Observable<boolean> {
    return this.currentUserService.isCurrentUserAdminOrAuditor$()
      .pipe(
        switchMap((hasAuthorization: boolean) => {
          return of(hasAuthorization && this.topicDraft.currentStatus !== status.draft);
        })
      );
  }

  canChangeCurrentStatusForSubmission$(): Observable<boolean> {
    return this.currentUserService.isCurrentUserAdminOrCreator$(this.topicDraft.initiatorId)
      .pipe(
        switchMap((hasAuthorization: boolean) => {
          return of(hasAuthorization && (
            this.topicDraft.currentStatus === status.draft ||
            this.topicDraft.currentStatus === status.submitted));
        })
      );
  }

  changeCurrentStatus(newStatus: status): void {
    this.topicDraft.currentStatus = newStatus;
    this.subscriptions.push(this.topicDraftMapper.updateTopicDraftStatus$(this.topicDraft)
      .subscribe());
    this.editedTopicDraftEvent.emit(this.topicDraft);
  }

  approvingTopicDraft(): void {
     this.changeCurrentStatus(this.topicDraft.currentStatus !== status.approved ? status.approved : status.submitted);
  }

  rejectingTopicDraft(): void {
    this.changeCurrentStatus(this.topicDraft.currentStatus !== status.rejected ? status.rejected : status.submitted);
  }

  submittingTopicDraft(): void {
    if (this.topicDraft.currentStatus === status.draft) {
      this.changeCurrentStatus(status.submitted);
      const snackBarText: string = this.translate.instant('submitted-topic-draft-action-button.snackbar.submit');
      const snackBarOk: string = this.translate.instant('submitted-topic-draft-action-button.snackbar.ok');
      this.snackBar.open(snackBarText, snackBarOk, {
        verticalPosition: 'top',
        duration: 3500
      });
    } else {
      this.changeCurrentStatus(status.draft);
    }
  }

  getApproveOrRejectTooltipText$(): Observable<string> {
    return this.currentUserService.isCurrentUserAdminOrAuditor$()
      .pipe(
        switchMap((isAdminOrAuditor: boolean) => {
          if (this.topicDraft.currentStatus === status.draft && !isAdminOrAuditor) {
            return of(this.changeCurrentStatusByStatusAndUser);
          } else if (!isAdminOrAuditor) {
            return of(this.userRoleToChangeStatus);
          } else if (this.topicDraft.currentStatus === status.draft) {
            return of(this.stateMustBeSubmittedTooltip);
          }

          return of('');
        }
      ));
  }

  getSubmissionTooltipText$(): Observable<string> {
    return this.currentUserService.isCurrentUserAdminOrCreator$(this.topicDraft.initiatorId)
      .pipe(
        switchMap((isAdminOrCreator: boolean) => {
            if ((this.topicDraft.currentStatus === status.approved || this.topicDraft.currentStatus === status.rejected)
              && !isAdminOrCreator) {
              return of(this.statusMustBeSubmittedAndUser);
            } else if (!isAdminOrCreator) {
              return of(this.adminOrInitiatorTooltip);
            } else if (this.topicDraft.currentStatus === status.approved
              || this.topicDraft.currentStatus === status.rejected) {
              return of(this.statusMustBeSubmitted);
            } else {
              return of('');
            }
          }
        ));
  }

  getEditTooltipText$(): Observable<string> {
    return this.currentUserService.isCurrentUserAdminOrCreator$(this.topicDraft.initiatorId)
      .pipe(
        switchMap((isAdminOrCreator: boolean) => {
          if ((this.topicDraft.currentStatus === status.approved || this.topicDraft.currentStatus === status.rejected) &&
            !isAdminOrCreator) {
            return of(this.editTooltipStatusAndUser);
          } else if (!isAdminOrCreator) {
            return of(this.editTooltipUser);
          } else if (this.topicDraft.currentStatus === status.approved || this.topicDraft.currentStatus === status.rejected) {
            return of(this.editTooltipStatus);
          }

          return of('');
        }
      ));
  }

  getApprovalButtonText(): string {
    return this.topicDraft.currentStatus === status.approved ? this.withDrawApprovalTopicDraftText
      : this.approveTopicDraftText;
  }

  getRejectionButtonText(): string {
    return this.topicDraft.currentStatus === status.rejected ? this.withDrawRejectionTopicDraftText
      : this.rejectTopicDraftText;
  }

  getSubmissionButtonText(): string {
    return this.topicDraft.currentStatus !== status.draft ? this.withDrawSubmitTopicDraftText
      : this.submitTopicDraftText;
  }

  clickedOpenComments(): void {
    const topicDraftHeading: string = this.translate.instant('submitted-topic-draft-action-button.comments.heading');

    const dialogData: CommentViewDialogFormData = {
      componentTypeTitle: topicDraftHeading,
      componentName: this.topicDraft.name,
      viewCommentParentType: ViewCommentParentType.topicDraft,
      parentId: this.topicDraft.id,
    };
    const dialogReference: MatDialogRef<CommentViewDialogComponent, object> =
      this.dialog.open(CommentViewDialogComponent, {autoFocus: false, data: dialogData, minWidth: '50vw'});
  }
}
