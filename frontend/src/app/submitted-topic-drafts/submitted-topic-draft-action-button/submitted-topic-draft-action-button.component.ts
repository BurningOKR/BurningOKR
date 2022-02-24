import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { of, Subscription } from 'rxjs';
import { Observable } from 'rxjs/internal/Observable';
import { switchMap, take } from 'rxjs/operators';
import { CurrentUserService } from '../../core/services/current-user.service';
import {
  CommentViewDialogComponent,
  CommentViewDialogFormData,
} from '../../okrview/comment/comment-view-dialog/comment-view-dialog.component';
import {
  ConfirmationDialogComponent,
  ConfirmationDialogData,
} from '../../shared/components/confirmation-dialog/confirmation-dialog.component';
import { OkrTopicDraft } from '../../shared/model/ui/OrganizationalUnit/okr-topic-draft/okr-topic-draft';
import { status } from '../../shared/model/ui/OrganizationalUnit/okr-topic-draft/okr-topic-draft-status-enum';
import { ViewCommentParentType } from '../../shared/model/ui/view-comment-parent-type';
import { TopicDraftMapper } from '../../shared/services/mapper/topic-draft-mapper';
import { SubmittedTopicDraftEditComponent } from '../submitted-topic-draft-edit/submitted-topic-draft-edit.component';
import { ConvertSubmittedTopicDraftToTeamComponent } from '../submitted-topic-drafts-convert-to-team/convert-submitted-topic-draft-to-team.component';

@Component({
  selector: 'app-submitted-topic-draft-action-button',
  templateUrl: './submitted-topic-draft-action-button.component.html',
  styleUrls: ['./submitted-topic-draft-action-button.component.css'],
})
export class SubmittedTopicDraftActionButtonComponent implements OnInit {
  @Input() topicDraft: OkrTopicDraft;
  @Output() topicDraftDeletedEvent = new EventEmitter();
  @Output() editedTopicDraftEvent: EventEmitter<OkrTopicDraft> = new EventEmitter<OkrTopicDraft>();

  subscriptions: Subscription[] = [];

  editTooltipStatus$: Observable<string>;
  editTooltipUser$: Observable<string>;
  editTooltipStatusAndUser$: Observable<string>;
  stateMustBeSubmittedTooltip$: Observable<string>;
  userRoleToChangeStatus$: Observable<string>;
  changeCurrentStatusByStatusAndUser$: Observable<string>;
  approveTopicDraftText$: Observable<string>;
  withDrawApprovalTopicDraftText$: Observable<string>;
  rejectTopicDraftText$: Observable<string>;
  withDrawRejectionTopicDraftText$: Observable<string>;
  submitTopicDraftText$: Observable<string>;
  withDrawSubmitTopicDraftText$: Observable<string>;
  adminOrInitiatorTooltip$: Observable<string>;
  statusMustBeSubmitted$: Observable<string>;
  statusMustBeSubmittedAndUser$: Observable<string>;
  notAdminToolTip$: Observable<string>;
  notApprovedToolTip$: Observable<string>;

  constructor(private topicDraftMapper: TopicDraftMapper,
              private currentUserService: CurrentUserService,
              private translate: TranslateService,
              private dialog: MatDialog,
              private snackBar: MatSnackBar,
              private router: Router) {
  }

  ngOnInit(): void {
    this.loadTranslations();
  }

  editTopicDraft(): void {
    const data: object = {
      data: {
        topicDraft: this.topicDraft,
        editedTopicDraftEvent: this.editedTopicDraftEvent,
      },
    };
    this.dialog.open(SubmittedTopicDraftEditComponent, data);
  }

  clickedDeleteTopicDraft(): void {
    const title: string = this.translate.instant('submitted-topic-draft-action-button.delete.title');
    const message: string = this.translate.instant('submitted-topic-draft-action-button.delete.message', { name: this.topicDraft.name });
    const confirmButtonText: string = this.translate.instant('submitted-topic-draft-action-button.delete.button-text');

    const dialogData: ConfirmationDialogData = {
      title,
      message,
      confirmButtonText,
    };

    const dialogReference: MatDialogRef<ConfirmationDialogComponent, object>
      = this.dialog.open(ConfirmationDialogComponent, { width: '600px', data: dialogData });

    dialogReference
      .afterClosed()
      .pipe(take(1))
      .subscribe(isConfirmed => {
        if (isConfirmed) {
          this.deleteTopicDraft();
        }
      });
  }

  deleteTopicDraft(): void {
    this.topicDraftMapper
      .deleteTopicDraft$(this.topicDraft.id)
      .pipe(take(1))
      .subscribe(() => this.topicDraftDeletedEvent.emit(),
      );
  }

  canEditTopicDraft$(): Observable<boolean> {
    return this.currentUserService.isCurrentUserAdminOrCreator$(this.topicDraft.initiatorId)
      .pipe(
        switchMap((hasAuthorization: boolean) => {
          return of(hasAuthorization && (this.topicDraft.currentStatus === status.draft
            || this.topicDraft.currentStatus === status.submitted));
        }),
      );
  }

  isTopicDraftConvertableToTeam$(): Observable<boolean> {
    return this.currentUserService.isCurrentUserAdmin$()
      .pipe(
        switchMap((hasAuthorization: boolean) => {
          return of(hasAuthorization && this.draftIsApproved());
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
        }),
      );
  }

  canChangeCurrentStatusForSubmission$(): Observable<boolean> {
    return this.currentUserService.isCurrentUserAdminOrCreator$(this.topicDraft.initiatorId)
      .pipe(
        switchMap((hasAuthorization: boolean) => {
          return of(hasAuthorization && (
            this.topicDraft.currentStatus === status.draft ||
            this.topicDraft.currentStatus === status.submitted));
        }),
      );
  }

  changeCurrentStatus(newStatus: status): void {
    this.topicDraft.currentStatus = newStatus;
    this.topicDraftMapper.updateTopicDraftStatus$(this.topicDraft).pipe(take(1))
      .subscribe();
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
        duration: 3500,
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
              return this.changeCurrentStatusByStatusAndUser$;
            } else if (!isAdminOrAuditor) {
              return this.userRoleToChangeStatus$;
            } else if (this.topicDraft.currentStatus === status.draft) {
              return this.stateMustBeSubmittedTooltip$;
            }

            return of('');
          },
        ));
  }

  getSubmissionTooltipText$(): Observable<string> {
    return this.currentUserService.isCurrentUserAdminOrCreator$(this.topicDraft.initiatorId)
      .pipe(
        switchMap((isAdminOrCreator: boolean) => {
            if ((this.topicDraft.currentStatus === status.approved || this.topicDraft.currentStatus === status.rejected)
              && !isAdminOrCreator) {
              return this.statusMustBeSubmittedAndUser$;
            } else if (!isAdminOrCreator) {
              return this.adminOrInitiatorTooltip$;
            } else if (this.topicDraft.currentStatus === status.approved
              || this.topicDraft.currentStatus === status.rejected) {
              return this.statusMustBeSubmitted$;
            } else {
              return of('');
            }
          },
        ));
  }

  getEditTooltipText$(): Observable<string> {
    return this.currentUserService.isCurrentUserAdminOrCreator$(this.topicDraft.initiatorId)
      .pipe(
        switchMap((isAdminOrCreator: boolean) => {
            if ((this.topicDraft.currentStatus === status.approved || this.topicDraft.currentStatus === status.rejected) &&
              !isAdminOrCreator) {
              return this.editTooltipStatusAndUser$;
            } else if (!isAdminOrCreator) {
              return this.editTooltipUser$;
            } else if (this.topicDraft.currentStatus === status.approved || this.topicDraft.currentStatus === status.rejected) {
              return this.editTooltipStatus$;
            }

            return of('');
          },
        ));
  }

  getApprovalButtonText$(): Observable<string> {
    return this.topicDraft.currentStatus === status.approved ? this.withDrawApprovalTopicDraftText$
      : this.approveTopicDraftText$;
  }

  getRejectionButtonText$(): Observable<string> {
    return this.topicDraft.currentStatus === status.rejected ? this.withDrawRejectionTopicDraftText$
      : this.rejectTopicDraftText$;
  }

  getSubmissionButtonText$(): Observable<string> {
    return this.topicDraft.currentStatus !== status.draft ? this.withDrawSubmitTopicDraftText$
      : this.submitTopicDraftText$;
  }

  clickedOpenComments(): void {
    const topicDraftHeading: string = this.translate.instant('submitted-topic-draft-action-button.comments.heading');

    const dialogData: CommentViewDialogFormData = {
      componentTypeTitle: topicDraftHeading,
      componentName: this.topicDraft.name,
      viewCommentParentType: ViewCommentParentType.topicDraft,
      parentId: this.topicDraft.id,
    };
    this.dialog.open(CommentViewDialogComponent, { autoFocus: false, data: dialogData, minWidth: '50vw' });
  }

  getConvertToTeamTooltipText$(): Observable<string> {
    return this.currentUserService.isCurrentUserAdmin$()
      .pipe(
        take(1),
        switchMap((isAdmin: boolean) => {
          if (!isAdmin && !this.draftIsApproved()) {
            return this.notApprovedToolTip$;
          }
          if (isAdmin && !this.draftIsApproved()) {
            return this.notApprovedToolTip$;
          }
          if (!isAdmin && this.draftIsApproved()) {
            return this.notAdminToolTip$;
          }
        })
     );
  }

  clickedConvertToTeam() {
    const topicDraft: OkrTopicDraft = this.topicDraft;

    const convertSubmittedTopicDraftToTeamReference: MatDialogRef<ConvertSubmittedTopicDraftToTeamComponent, object>
      = this.dialog.open(ConvertSubmittedTopicDraftToTeamComponent, { width: '600px', data: { topicDraft } });

    convertSubmittedTopicDraftToTeamReference
      .afterClosed()
      .pipe(take(1))
      .subscribe(departmentId => {
          if (departmentId) {
            const url: string = `/okr/departments/${departmentId}`;
            this.router.navigateByUrl(url).then(
              () => this.deleteTopicDraft(),
            );
          }
        },
      );
  }

  private draftIsApproved(): boolean {
    return this.topicDraft.currentStatus === status.approved;
  }

  private loadTranslations(): void {
    this.editTooltipStatus$ = this.translate.stream('submitted-topic-draft-action-button.edit-tooltip.status');
    this.editTooltipUser$ = this.translate.stream('submitted-topic-draft-action-button.edit-tooltip.user');
    this.editTooltipStatusAndUser$ = this.translate.stream('submitted-topic-draft-action-button.edit-tooltip.status-and-user');
    this.stateMustBeSubmittedTooltip$ = this.translate.stream('submitted-topic-draft-action-button.state-submitted-tooltip');
    this.userRoleToChangeStatus$ = this.translate.stream('submitted-topic-draft-action-button.no-permission');
    this.changeCurrentStatusByStatusAndUser$ = this.translate.stream('submitted-topic-draft-action-button.approving-status-and-user');
    this.approveTopicDraftText$ = this.translate.stream('submitted-topic-draft-action-button.capitalised-approve');
    this.withDrawApprovalTopicDraftText$ = this.translate.stream('submitted-topic-draft-action-button.withdraw-approval');
    this.rejectTopicDraftText$ = this.translate.stream('submitted-topic-draft-action-button.capitalized-reject');
    this.withDrawRejectionTopicDraftText$ = this.translate.stream('submitted-topic-draft-action-button.withdraw-rejection');
    this.submitTopicDraftText$ = this.translate.stream('submitted-topic-draft-action-button.capitalized-submit');
    this.withDrawSubmitTopicDraftText$ = this.translate.stream('submitted-topic-draft-action-button.withdraw-submit');
    this.adminOrInitiatorTooltip$ = this.translate.stream('submitted-topic-draft-action-button.admin-initiator-tooltip');
    this.statusMustBeSubmitted$ = this.translate.stream('submitted-topic-draft-action-button.status-submitted');
    this.statusMustBeSubmittedAndUser$ = this.translate.stream('submitted-topic-draft-action-button.status-submitted-user');
    this.notAdminToolTip$ = this.translate.stream('submitted-topic-draft-action-button.user-not-admin');
    this.notApprovedToolTip$ = this.translate.stream('submitted-topic-draft-action-button.not-approved');
  }
}
