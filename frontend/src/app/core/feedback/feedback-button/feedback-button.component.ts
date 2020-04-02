import { Component, OnDestroy } from '@angular/core';
import { Observable, ObservableInput, Subscription } from 'rxjs';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { FeedbackFormComponent } from '../feedback-form/feedback-form.component';
import { switchMap } from 'rxjs/operators';

@Component({
  selector: 'app-feedback-button',
  templateUrl: './feedback-button.component.html',
  styleUrls: ['./feedback-button.component.scss']
})
export class FeedbackButtonComponent implements OnDestroy {

  private subscriptions: Subscription[] = [];

  constructor(
    private dialog: MatDialog) {}

  ngOnDestroy(): void {
    this.subscriptions.forEach((sub: Subscription) => sub.unsubscribe());
    this.subscriptions = [];
  }

  openFeedbackPopup(): void {
    this.subscriptions.push(this.postFeedback$()
      .subscribe(feedback => {
        // tslint:disable-next-line:no-console TODO: use snackbar
      console.log(`${feedback} send`);
    }));
  }

  postFeedback$(): Observable<any extends ObservableInput<infer T> ? T : never> {
    const dialogRef: MatDialogRef<FeedbackFormComponent> = this.dialog.open(FeedbackFormComponent, {});

    return dialogRef.afterClosed()
      .pipe(switchMap(n => n));
  }

}
