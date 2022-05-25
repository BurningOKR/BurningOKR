import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { of, Subscription } from 'rxjs';
import { Observable } from 'rxjs/internal/Observable';
import { take } from 'rxjs/operators';
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
import {
  TopicDraftEditDialogueComponent
} from '../topic-draft-edit-dialogue-component/topic-draft-edit-dialogue.component';
import {
  ConvertTopicDraftToTeamDialogueComponent,
} from '../convert-topic-draft-to-team/convert-topic-draft-to-team-dialogue-component/convert-topic-draft-to-team-dialogue.component';
import { TopicDraftPermissionService } from '../topic-draft-permission.service';
import { TopicDraftStatusService } from '../topic-draft-status.service';

@Component({
  selector: 'app-topic-draft-action-button',
  templateUrl: './topic-draft-action-button.component.html',
  styleUrls: ['./topic-draft-action-button.component.css'],
})
export class TopicDraftActionButtonComponent implements OnInit {
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
              private topicDraftPermissionService: TopicDraftPermissionService,
              private translate: TranslateService,
              private dialog: MatDialog,
              private snackBar: MatSnackBar,
              private router: Router,
              private topicDraftStatusService: TopicDraftStatusService) {
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
    this.dialog.open(TopicDraftEditDialogueComponent, data);
  }

  getEditTooltipText$(): Observable<string> {
    const hasEditingPermissions: boolean = this.topicDraftPermissionService.hasCurrentUserEditingPermissions(this.topicDraft);
    const isTopicDraftApprovedOrRejected: boolean = TopicDraftStatusService.isTopicDraftApprovedOrRejected(this.topicDraft);
    if (isTopicDraftApprovedOrRejected && !hasEditingPermissions) {
      return this.editTooltipStatusAndUser$;
    } else if (!hasEditingPermissions) {
      return this.editTooltipUser$;
    } else if (isTopicDraftApprovedOrRejected) {
      return this.editTooltipStatus$;
    }

    return of('');
  }

  clickedDeleteTopicDraft(): void {
    const dialogData: ConfirmationDialogData = {
      title: this.translate.instant('topic-draft-action-button.delete.title'),
      message: this.translate.instant('topic-draft-action-button.delete.message', {name: this.topicDraft.name}),
      confirmButtonText: this.translate.instant('topic-draft-action-button.delete.button-text'),
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
      .subscribe(() => this.topicDraftDeletedEvent.emit());
  }

  changeCurrentStatus(newStatus: status): void {
    this.topicDraftStatusService.changeTopicDraftStatusTo(this.topicDraft, newStatus);
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
      const snackBarText: string = this.translate.instant('topic-draft-action-button.snackbar.submit');
      const snackBarOk: string = this.translate.instant('topic-draft-action-button.snackbar.ok');
      this.snackBar.open(snackBarText, snackBarOk, {
        verticalPosition: 'top',
        duration: 3500,
      });
    } else {
      this.changeCurrentStatus(status.draft);
    }
  }

  getApproveOrRejectTooltipText$(): Observable<string> {
    const hasApprovingPrivileges: boolean = this.topicDraftPermissionService.hasCurrentUserApprovingPermissions();
    if (this.topicDraft.currentStatus === status.draft && !hasApprovingPrivileges) {
      return this.changeCurrentStatusByStatusAndUser$;
    } else if (!hasApprovingPrivileges) {
      return this.userRoleToChangeStatus$;
    } else if (!TopicDraftStatusService.isTopicDraftInApprovingStage(this.topicDraft)) {
      return this.stateMustBeSubmittedTooltip$;
    }

    return of('');
  }

  getSubmissionTooltipText$(): Observable<string> {
    const hasEditingPermissions: boolean = this.topicDraftPermissionService.hasCurrentUserEditingPermissions(this.topicDraft);
    const isApprovedOrRejected: boolean = TopicDraftStatusService.isTopicDraftApprovedOrRejected(this.topicDraft);
    if (isApprovedOrRejected && hasEditingPermissions) {
      return this.statusMustBeSubmittedAndUser$;
    } else if (hasEditingPermissions) {
      return this.adminOrInitiatorTooltip$;
    } else if (isApprovedOrRejected) {
      return this.statusMustBeSubmitted$;
    }

    return of('');
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
    const topicDraftHeading: string = this.translate.instant('topic-draft-action-button.comments.heading');

    const dialogData: CommentViewDialogFormData = {
      componentTypeTitle: topicDraftHeading,
      componentName: this.topicDraft.name,
      viewCommentParentType: ViewCommentParentType.topicDraft,
      parentId: this.topicDraft.id,
    };
    this.dialog.open(CommentViewDialogComponent, { autoFocus: false, data: dialogData, minWidth: '50vw' });
  }

  getConvertToTeamTooltipText$(): Observable<string> {
    if (!TopicDraftStatusService.isTopicDraftConvertableToTeam(this.topicDraft)) {
      return this.notApprovedToolTip$;
    }
    if (this.topicDraftPermissionService.hasCurrentUserConvertToTeamPermissions()) {
      return this.notAdminToolTip$;
    }
  }

  clickedConvertToTeam() {
    const topicDraft: OkrTopicDraft = this.topicDraft;

    const convertSubmittedTopicDraftToTeamReference: MatDialogRef<ConvertTopicDraftToTeamDialogueComponent, object>
      = this.dialog.open(ConvertTopicDraftToTeamDialogueComponent, {
      minWidth: '600px',
      width: 'auto',
      data: { topicDraft },
    });

    convertSubmittedTopicDraftToTeamReference
      .afterClosed()
      .pipe(take(1))
      .subscribe(departmentId => {
          if (departmentId) {
            const url: string = `/okr/departments/${departmentId}`;
            this.router.navigateByUrl(url)
              .catch(console.log);
          }
        },
      );
  }

  canEditTopicDraft(): boolean {
    return this.topicDraftPermissionService.hasCurrentUserEditingPermissions(this.topicDraft)
      && TopicDraftStatusService.isTopicDraftInSubmissionStage(this.topicDraft);
  }

  canDeleteTopicDraft(): boolean {
    return this.topicDraftPermissionService.hasCurrentUserDeletePermissions(this.topicDraft);
  }

  isTopicDraftConvertableToTeam(): boolean {
    return this.topicDraftPermissionService.hasCurrentUserConvertToTeamPermissions()
      && TopicDraftStatusService.isTopicDraftConvertableToTeam(this.topicDraft);
  }

  isTopicDraftInSubmissionStage(): boolean {
    return this.topicDraftPermissionService.hasCurrentUserSubmissionPermissions(this.topicDraft)
      && TopicDraftStatusService.isTopicDraftInSubmissionStage(this.topicDraft);
  }

  isTopicDraftInApprovingStage(): boolean {
    return this.topicDraftPermissionService.hasCurrentUserApprovingPermissions()
      && TopicDraftStatusService.isTopicDraftInApprovingStage(this.topicDraft);
  }

  private loadTranslations(): void {
    this.editTooltipStatus$ = this.translate.stream('topic-draft-action-button.edit-tooltip.status');
    this.editTooltipUser$ = this.translate.stream('topic-draft-action-button.edit-tooltip.user');
    this.editTooltipStatusAndUser$ = this.translate.stream('topic-draft-action-button.edit-tooltip.status-and-user');
    this.stateMustBeSubmittedTooltip$ = this.translate.stream('topic-draft-action-button.state-submitted-tooltip');
    this.userRoleToChangeStatus$ = this.translate.stream('topic-draft-action-button.no-permission');
    this.changeCurrentStatusByStatusAndUser$ = this.translate.stream('topic-draft-action-button.approving-status-and-user');
    this.approveTopicDraftText$ = this.translate.stream('topic-draft-action-button.capitalised-approve');
    this.withDrawApprovalTopicDraftText$ = this.translate.stream('topic-draft-action-button.withdraw-approval');
    this.rejectTopicDraftText$ = this.translate.stream('topic-draft-action-button.capitalized-reject');
    this.withDrawRejectionTopicDraftText$ = this.translate.stream('topic-draft-action-button.withdraw-rejection');
    this.submitTopicDraftText$ = this.translate.stream('topic-draft-action-button.capitalized-submit');
    this.withDrawSubmitTopicDraftText$ = this.translate.stream('topic-draft-action-button.withdraw-submit');
    this.adminOrInitiatorTooltip$ = this.translate.stream('topic-draft-action-button.admin-initiator-tooltip');
    this.statusMustBeSubmitted$ = this.translate.stream('topic-draft-action-button.status-submitted');
    this.statusMustBeSubmittedAndUser$ = this.translate.stream('topic-draft-action-button.status-submitted-user');
    this.notAdminToolTip$ = this.translate.stream('topic-draft-action-button.user-not-admin');
    this.notApprovedToolTip$ = this.translate.stream('topic-draft-action-button.not-approved');
  }
}
