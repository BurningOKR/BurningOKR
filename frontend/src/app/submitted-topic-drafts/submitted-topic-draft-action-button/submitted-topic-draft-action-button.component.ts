import { Component, EventEmitter, Input, OnDestroy, Output, OnInit } from '@angular/core';
import { OkrTopicDraft } from '../../shared/model/ui/OrganizationalUnit/okr-topic-draft/okr-topic-draft';
import { SubmittedTopicDraftDetailsComponent } from '../submitted-topic-draft-details/submitted-topic-draft-details.component';
import {
    ConfirmationDialogComponent,
    ConfirmationDialogData
} from '../../shared/components/confirmation-dialog/confirmation-dialog.component';
import { take } from 'rxjs/operators';
import { Subscription } from 'rxjs';
import { I18n } from '@ngx-translate/i18n-polyfill';
import { MatDialog, MatDialogRef } from '@angular/material';
import { TopicDraftMapper } from '../../shared/services/mapper/topic-draft-mapper';
import { User } from '../../shared/model/api/user';
import { CurrentUserService } from '../../core/services/current-user.service';
import { SubmittedTopicDraftEditComponent } from '../submitted-topic-draft-edit/submitted-topic-draft-edit.component';
import { status } from '../../shared/model/ui/OrganizationalUnit/okr-topic-draft/okr-topic-draft-status-enum';

@Component({
  selector: 'app-submitted-topic-draft-action-button',
  templateUrl: './submitted-topic-draft-action-button.component.html',
  styleUrls: ['./submitted-topic-draft-action-button.component.css']
})
export class SubmittedTopicDraftActionButtonComponent implements OnDestroy, OnInit {
  private currentUser: User;

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

  userRoleToApprove: string = this.i18n({
    id: 'onlyAdminOrAuditorApprovePermission',
    value: 'Nur ein Admin oder ein Auditor können annehmen'
  });

  userRoleToReject: string = this.i18n({
    id: 'onlyAdminOrAuditorRejectPermission',
    value: 'Nur ein Admin oder ein Auditor können ablehnen'
  });

  approveTopicdraftStatusAndUser: string = this.i18n({
    id: 'approvingStatusAndUser',
    value: 'Der Status muss auf "Eingereicht" sein und nur ein Admin oder ein Auditor können annehmen'
  });

  rejectTopicdraftStatusAndUser: string = this.i18n({
    id: 'rejectingStatusAndUser',
    value: 'Der Status muss auf "Eingereicht" sein und nur ein Admin oder ein Auditor können ablehnen'
  });

  constructor(private topicDraftMapper: TopicDraftMapper,
              private currentUserService: CurrentUserService,
              private i18n: I18n,
              private dialog: MatDialog) {
    }

  ngOnInit(): void {
      this.currentUserService.getCurrentUser$()
          .pipe(take(1))
          .subscribe((received: User) => {
              this.currentUser = received;
          }
      );
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

  isCurrentUserAdmin(): boolean {
    let userAdmin: boolean;
    this.currentUserService.isCurrentUserAdmin$()
      .pipe(take(1))
      .subscribe((received: boolean) => {
        userAdmin = received;
      });

    return userAdmin;
  }

  currentUserNotAdminOrCreator(): boolean {
    const userNotCreator: boolean = (this.currentUser.id !== this.topicDraft.initiatorId);
    const userNotAdmin: boolean = !this.isCurrentUserAdmin();

    return (userNotCreator && userNotAdmin);
  }

  // TODO NL 07.07.2021 Auditor needs to be added
  currentUserNotAdminOrAuditor(): boolean {
    const userNotAdmin: boolean = !this.isCurrentUserAdmin();

    return userNotAdmin;
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

  canApproveTopicDraft(): boolean {
    return !this.currentUserNotAdminOrAuditor() &&
      (this.topicDraft.currentStatus === status.submitted || this.topicDraft.currentStatus === status.approved);
  }

  canRejectTopicDraft(): boolean {
    return !this.currentUserNotAdminOrAuditor() &&
      (this.topicDraft.currentStatus === status.submitted || this.topicDraft.currentStatus === status.rejected);
  }

  approvingTopicDraft(): void {
    if (this.topicDraft.currentStatus !== status.approved) {
      this.topicDraft.currentStatus = status.approved;
      this.topicDraftMapper.updateTopicDraftStatus$(this.topicDraft)
        .subscribe();
    } else {
      this.topicDraft.currentStatus = status.submitted;
      this.topicDraftMapper.updateTopicDraftStatus$(this.topicDraft)
        .subscribe();
    }
  }

  rejectingTopicDraft(): void {
    if (this.topicDraft.currentStatus !== status.rejected) {
      this.topicDraft.currentStatus = status.rejected;
      this.topicDraftMapper.updateTopicDraftStatus$(this.topicDraft)
        .subscribe();
    } else {
      this.topicDraft.currentStatus = status.submitted;
      this.topicDraftMapper.updateTopicDraftStatus$(this.topicDraft)
        .subscribe();
    }
  }

  canEditTopicDraft(): boolean {
    return this.topicDraft.currentStatus === status.submitted && !this.currentUserNotAdminOrCreator();
  }

  // TODO NL 07.07.2021 ggf Auditor hinzufügen
  getEditTooltipText(): string {
    if ((this.topicDraft.currentStatus === status.approved || this.topicDraft.currentStatus === status.rejected) &&
    this.currentUserNotAdminOrCreator()) {
      return this.editTooltipStatusAndUser;
    } else if (this.currentUserNotAdminOrCreator()) {
      return this.editTooltipUser;
    } else if (this.topicDraft.currentStatus === status.approved || this.topicDraft.currentStatus === status.rejected) {
      return this.editTooltipStatus;
    }
  }

  getApproveTooltipText(): string {
    if ((this.topicDraft.currentStatus === status.rejected || this.topicDraft.currentStatus === status.draft) &&
      this.currentUserNotAdminOrAuditor()) {
      return this.approveTopicdraftStatusAndUser;
    } else if (this.currentUserNotAdminOrAuditor()) {
      return this.userRoleToApprove;
    } else if (this.topicDraft.currentStatus === status.draft || this.topicDraft.currentStatus === status.rejected) {
      return this.stateMustBeSubmittedTooltip;
    }
  }

  getRejectTooltipText(): string {
    if ((this.topicDraft.currentStatus === status.approved || this.topicDraft.currentStatus === status.draft) &&
      this.currentUserNotAdminOrAuditor()) {
      return this.rejectTopicdraftStatusAndUser;
    } else if (this.currentUserNotAdminOrAuditor()) {
      return this.userRoleToReject;
    } else if (this.topicDraft.currentStatus === status.draft || this.topicDraft.currentStatus === status.approved) {
      return this.stateMustBeSubmittedTooltip;
    }
  }
}
