import { Component, EventEmitter, Input, OnDestroy, Output } from '@angular/core';
import { OkrTopicDraft } from '../../shared/model/ui/OrganizationalUnit/okr-topic-draft/okr-topic-draft';
import {
    ConfirmationDialogComponent,
    ConfirmationDialogData
} from '../../shared/components/confirmation-dialog/confirmation-dialog.component';
import { switchMap, take } from 'rxjs/operators';
import { of, Subscription } from 'rxjs';
import { I18n } from '@ngx-translate/i18n-polyfill';
import { MatDialog, MatDialogRef } from '@angular/material';
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

  @Output() clickedEditAction: EventEmitter<void> = new EventEmitter<void>();
  @Output() clickedDeleteAction: EventEmitter<void> = new EventEmitter<void>();
  @Output() clickedCommentsAction: EventEmitter<void> = new EventEmitter<void>();
  @Output() clickedSubmitAction: EventEmitter<void> = new EventEmitter<void>();
  @Output() clickedWithdrawAction: EventEmitter<void> = new EventEmitter<void>();
  @Output() clickedApprove: EventEmitter<void> = new EventEmitter<void>();
  @Output() clickedDiscardApprovalAction: EventEmitter<void> = new EventEmitter<void>();
  @Output() clickedRefuse: EventEmitter<void>  = new EventEmitter<void>();
  @Output() clickedDiscardRefusalAction: EventEmitter<void> = new EventEmitter<void>();

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

  constructor(private topicDraftMapper: TopicDraftMapper,
              private currentUserService: CurrentUserService,
              private i18n: I18n,
              private dialog: MatDialog) {
    }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe());
    this.subscriptions = [];
  }

  printNotImplemented(): string {
    // TODO: methode Entfernen
    // tslint:disable-next-line: no-console
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
          .subscribe(() => {
                this.topicDraftDeletedEvent.emit();
              }
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

  changeCurrentStatus(newStatus: status): void {
    this.topicDraft.currentStatus = newStatus;
    this.topicDraftMapper.updateTopicDraftStatus$(this.topicDraft)
      .subscribe();
  }

  approvingTopicDraft(): void {
     this.changeCurrentStatus(this.topicDraft.currentStatus !== status.approved ? status.approved : status.submitted);
  }

  rejectingTopicDraft(): void {
    this.changeCurrentStatus(this.topicDraft.currentStatus !== status.rejected ? status.rejected : status.submitted);
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

  clickedOpenComments(): void {
    const dialogData: CommentViewDialogFormData = {
      componentTypeTitle: 'Themenentwurf',
      componentName: this.topicDraft.name,
      viewCommentParentType: ViewCommentParentType.topicDraft,
      parentId: this.topicDraft.id,
    };
    const dialogReference: MatDialogRef<CommentViewDialogComponent, object> =
      this.dialog.open(CommentViewDialogComponent, {autoFocus: false, data: dialogData, minWidth: '50vw'});
  }
}
