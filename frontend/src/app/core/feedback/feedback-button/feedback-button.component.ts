import { Component, OnDestroy } from '@angular/core';
import { Observable, ObservableInput, Subscription } from 'rxjs';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { FeedbackFormComponent } from '../feedback-form/feedback-form.component';
import { MatSnackBar } from '@angular/material/snack-bar';
import { I18n } from '@ngx-translate/i18n-polyfill';
import { ConfigurationService } from '../../settings/configuration.service';
import { Consts } from '../../../shared/consts';
import { environment } from '../../../../environments/environment';

@Component({
  selector: 'app-feedback-button',
  templateUrl: './feedback-button.component.html',
  styleUrls: ['./feedback-button.component.scss']
})
export class FeedbackButtonComponent implements OnDestroy {

  hasMail$: Observable<boolean>;
  isPlayground: boolean = environment.playground;

  private subscriptions: Subscription[] = [];
  private feedbackSuccessfullySubmittedMessage: string = this.i18n({
    id: 'feedbackSuccessfullySubmittedMessage',
    description: 'message to be shown after the user feedback was submitted successfully',
    value: 'Feedback erfolgreich übermittelt 📬'
  });

  constructor(
    private dialog: MatDialog,
    private snackBar: MatSnackBar,
    private i18n: I18n,
    private configService: ConfigurationService
  ) {
    this.hasMail$ = configService.getHasMailConfigured$();
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach((sub: Subscription) => sub.unsubscribe());
    this.subscriptions = [];
  }

  navigateToGitHub(): void {
    window.open(Consts.GIT_HUB_ISSUES);
  }

  postFeedback$(): Observable<any extends ObservableInput<infer T> ? T : never> {
    const dialogRef: MatDialogRef<FeedbackFormComponent> = this.dialog.open(FeedbackFormComponent, {});

    return dialogRef.afterClosed();
  }

  openFeedbackPopup(): void {
    this.subscriptions.push(this.postFeedback$()
      .subscribe((success: boolean) => {
        if (success) {
          this.snackBar.open(this.feedbackSuccessfullySubmittedMessage, undefined, {
            verticalPosition: 'top',
            duration: 3500
          });
        }
      }));
  }

}
