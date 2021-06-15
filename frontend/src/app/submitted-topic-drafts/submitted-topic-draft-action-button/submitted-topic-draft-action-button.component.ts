import { Component, Input, OnDestroy } from '@angular/core';
import { OkrTopicDraft } from '../../shared/model/ui/OrganizationalUnit/okr-topic-draft/okr-topic-draft';
import {
  ConfirmationDialogComponent,
  ConfirmationDialogData
} from '../../shared/components/confirmation-dialog/confirmation-dialog.component';
import { take } from 'rxjs/operators';
import { Subscription } from 'rxjs';
import { I18n } from '@ngx-translate/i18n-polyfill';
import { MatDialog, MatDialogRef } from '@angular/material';

@Component({
  selector: 'app-submitted-topic-draft-action-button',
  templateUrl: './submitted-topic-draft-action-button.component.html',
  styleUrls: ['./submitted-topic-draft-action-button.component.css']
})
export class SubmittedTopicDraftActionButtonComponent implements OnDestroy {

  @Input() topicDraft: OkrTopicDraft;

  subscriptions: Subscription[] = [];

  constructor(private matDialog: MatDialog,
              private i18n: I18n) {
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
    // TODO
  }
}
