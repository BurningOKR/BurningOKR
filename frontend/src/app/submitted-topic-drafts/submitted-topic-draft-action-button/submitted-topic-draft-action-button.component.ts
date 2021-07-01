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

@Component({
    selector: 'app-submitted-topic-draft-action-button',
    templateUrl: './submitted-topic-draft-action-button.component.html',
    styleUrls: ['./submitted-topic-draft-action-button.component.css']
})
export class SubmittedTopicDraftActionButtonComponent implements OnDestroy, OnInit {
    private currentUser: User;

    @Input() topicDraft: OkrTopicDraft;
    @Output() topicDraftDeletedEvent = new EventEmitter();

    subscriptions: Subscription[] = [];

    constructor(private matDialog: MatDialog,
                private topicDraftMapper: TopicDraftMapper,
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

    currentUserNotAdminOrCreator(): boolean {
        const userNotCreator: boolean = (this.currentUser.id !== this.topicDraft.initiatorId);
        let userNotAdmin: boolean;
        this.currentUserService.isCurrentUserAdmin$()
            .pipe(take(1))
                .subscribe((received: boolean) => {
                userNotAdmin = !received;
        });

        return (userNotCreator && userNotAdmin);
    }

    editTopicDraft(): void {
    const data: object = {
      data: {
        topicDraft: this.topicDraft
      }
    };
    this.dialog.open(SubmittedTopicDraftDetailsComponent, data);
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
      id: 'deleteButtonText',
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
                this.deleteTopicDraft();
              }
            })
    );
  }

    deleteTopicDraft(): void {
    this.subscriptions.push(
        this.topicDraftMapper
            .deleteTopicDraft$(this.topicDraft.descriptionId)
            .pipe(take(1))
            .subscribe(() => {
                  this.topicDraftDeletedEvent.emit();
                }
            ));
  }
}
