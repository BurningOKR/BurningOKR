import { Component, EventEmitter, Input, OnDestroy, Output } from '@angular/core';
import { OkrTopicDraft } from '../../shared/model/ui/OrganizationalUnit/okr-topic-draft/okr-topic-draft';
import {
  ConfirmationDialogComponent,
  ConfirmationDialogData
} from '../../shared/components/confirmation-dialog/confirmation-dialog.component';
import { switchMap, take } from 'rxjs/operators';
import { of, Subscription } from 'rxjs';
import { I18n } from '@ngx-translate/i18n-polyfill';
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
import { MatSnackBar } from "@angular/material/snack-bar";

@Component({
  selector: 'app-submitted-topic-draft-action-button',
  templateUrl: './submitted-topic-draft-action-button.component.html',
  styleUrls: ['./submitted-topic-draft-action-button.component.css']
})
export class SubmittedTopicDraftActionButtonComponent implements OnDestroy {
  @Input() topicDraft: OkrTopicDraft;
  @Output() topicDraftDeletedEvent = new EventEmitter();
  @Output() editedTopicDraftEvent: EventEmitter<OkrTopicDraft> = new EventEmitter<OkrTopicDraft>();

  subscriptions: Subscription[] = [];

  editTooltipStatus: string = this.i18n({
    id: 'statusShouldBeSubmittedOrDraft',
    value: 'Der Status muss auf "Eingereicht" oder "Vorlage" sein'
  });

  editTooltipUser: string = this.i18n({
    id: 'onlyAdminOrInitiatorPermission',
    value: 'Nur ein Admin oder der Initiator können bearbeiten'
  });

  editTooltipStatusAndUser: string = this.i18n({
    id: 'StatusSubmittedAndAdminOrIniator',
    value: 'Der Status muss auf "Eingereicht" oder "Vorlage" sein und nur ein Admin oder der Initiator können bearbeiten'
  });

  stateMustBeSubmittedTooltip: string = this.i18n({
    id: 'status-should-be-submitted',
    value: 'Der Status muss auf "Eingereicht" sein'
  });

  userRoleToChangeStatus: string = this.i18n({
    id: 'onlyAdminOrAuditorApprovePermission',
    value: 'Nur ein Admin oder ein Auditor können annehmen oder ablehnen'
  });

  changeCurrentStatusByStatusAndUser: string = this.i18n({
    id: 'approvingStatusAndUser',
    value: 'Der Status muss auf "Eingereicht" sein und nur ein Admin oder ein Auditor können annehmen oder ablehnen'
  });

  approveTopicDraftText: string = this.i18n({
    id: 'capitalised-approve',
    value: 'Genehmigen'
  });

  withDrawApprovalTopicDraftText: string = this.i18n({
    id: 'withdraw-approval',
    value: 'Genehmigung zurücknehmen'
  });

  rejectTopicDraftText: string = this.i18n({
    id: 'capitalised-reject',
    value: 'Ablehnen'
  });

  withDrawRejectionTopicDraftText: string = this.i18n({
    id: 'withdraw-rejection',
    value: 'Ablehnung zurücknehmen'
  });

  submitTopicDraftText: string = 'Einreichen';
  withDrawSubmitTopicDraftText: string = 'Einreichen zurücknehmen';
  adminOrInitiatorTooltip: string = 'Nur ein Admin oder der Initiator können Einreichen oder Einreichen zurücknehmen';
  statusMustBeSubmitted: string = 'Der Status muss auf "Eingereicht" sein, damit das Einreichen zurückgenommen werden kann';
  statusMustBeSubmittedAndUser: string = 'Der Status muss auf "Eingereicht" sein und Sie müssen ein Admin oder der Initiator sein, damit das Einreichen zurückgenommen werden kann';


  constructor(private topicDraftMapper: TopicDraftMapper,
              private currentUserService: CurrentUserService,
              private i18n: I18n,
              private dialog: MatDialog,
              private snackBar: MatSnackBar) {
    }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe());
    this.subscriptions = [];
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
    const title: string =
      this.i18n({
        id: 'deleteTopicDraftTitle',
        description: 'Title of the delete topicdraft dialog',
        value: 'Themenentwurf löschen'
      });

    const message: string =
      this.i18n({
        id: 'deleteTopicDraftMessage',
        description: 'Do you want to delete topic draft x',
        value: 'Themenentwurf "{{name}}" löschen?',
      }, {name: this.topicDraft.name});

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
      const snackBarText: string = 'Ihr Themenentwurf wurde zur Prüfung abgeschickt.';
      const snackBarOk: string = 'Ok';
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
            if ((this.topicDraft.currentStatus === status.approved || this.topicDraft.currentStatus === status.rejected) && !isAdminOrCreator) {
              return of(this.statusMustBeSubmittedAndUser);
            } else if (!isAdminOrCreator) {
              return of(this.adminOrInitiatorTooltip);
            } else if (this.topicDraft.currentStatus === status.approved || this.topicDraft.currentStatus === status.rejected) {
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
    const topicDraftHeading: string = this.i18n({
      id: 'component_departmentMenu_Topic_draft',
      value: 'Themenentwurf'
    });

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
