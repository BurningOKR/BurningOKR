import { Component, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { DialogComponent } from '../../../shared/components/dialog-component/dialog.component';
import { User } from '../../../shared/model/api/user';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { CurrentUserService } from '../../services/current-user.service';
import { Observable } from 'rxjs';
import { map, take } from 'rxjs/operators';
import { FeedbackApiService } from '../../../shared/services/api/feedback-api.service';

@Component({
  selector: 'app-feedback-form',
  templateUrl: './feedback-form.component.html',
  styleUrls: ['./feedback-form.component.scss'],
})
export class FeedbackFormComponent implements OnInit {
  feedbackForm$: Observable<FormGroup>;
  currentUser$: Observable<User>;
  noCurrentRequest: boolean = true;

  constructor(
    private dialogRef: MatDialogRef<DialogComponent<object>>,
    private feedbackApiService: FeedbackApiService,
    private currentUserService: CurrentUserService,
  ) {
  }

  ngOnInit(): void {
    this.currentUser$ = this.currentUserService.getCurrentUser$();
    this.feedbackForm$ = this.getFeedbackForm$();
  }

  sendFeedback(formRawValue: { feedbackText: string; name: string }): void {
    const feedbackText: string = formRawValue.feedbackText;
    const name: string = formRawValue.name;

    this.noCurrentRequest = false;
    this.feedbackApiService.postFeedback$(feedbackText, name)
      .pipe(take(1))
      .subscribe(() =>
          this.dialogRef.close(true), () => {
          this.noCurrentRequest = true;
        },
      );
  }

  private getFeedbackForm$(): Observable<FormGroup> {
    return this.currentUser$.pipe(
      map((currentUser: User) => {
        return new FormGroup({
          feedbackText: new FormControl('', [Validators.required]),
          name: new FormControl(
            `${currentUser.givenName} ${currentUser.surname}`,
            [Validators.required],
          ),
        });
      }),
    );
  }
}
