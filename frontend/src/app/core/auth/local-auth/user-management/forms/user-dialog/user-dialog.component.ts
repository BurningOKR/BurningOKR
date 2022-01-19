import { Component, Inject, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { TranslateService } from '@ngx-translate/core';
import { combineLatest, Observable, of, Subscription } from 'rxjs';
import { shareReplay, switchMap, take } from 'rxjs/operators';
import { environment } from '../../../../../../../environments/environment';
import { User } from '../../../../../../shared/model/api/user';
import { LocalUserService } from '../../../../../../shared/services/helper/local-user.service';
import { CurrentUserService } from '../../../../../services/current-user.service';
import { PasswordService } from '../../../password-service/password.service';
import { emailAlreadyInUseValidatorFunction } from '../email-already-in-use-validator-function';
import { UserDialogData } from '../user-dialog-data';

@Component({
  selector: 'app-user-dialog',
  templateUrl: './user-dialog.component.html',
  styleUrls: ['./user-dialog.component.css'],
})
export class UserDialogComponent implements OnInit, OnDestroy {
  @ViewChild('canvasElement') canvas;

  subscriptions: Subscription[] = [];
  userForm: FormGroup;
  userEmails: string[] = [];
  editedUserIsCurrentUser$: Observable<boolean>;
  resetPasswordButtonDisabled: boolean;
  isPlayground: boolean = environment.playground;

  private passwordResetSuccessMsg: string;
  private okMsg: string;

  constructor(private dialogRef: MatDialogRef<UserDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public formData: UserDialogData,
              private formBuilder: FormBuilder,
              private userService: LocalUserService,
              private currentUserService: CurrentUserService,
              private passwordService: PasswordService,
              private snackBar: MatSnackBar,
              private translate: TranslateService) {
  }

  ngOnInit(): void {
    this.translate.stream('user-dialog.snackbar.message').pipe(take(1))
      .subscribe(text => {
        this.passwordResetSuccessMsg = text;
      });
    this.translate.stream('user-dialog.snackbar.ok').pipe(take(1))
      .subscribe(text => {
        this.okMsg = text;
      });
    // Disable Admin Checkbox when the user edits himself and is admin.
    this.editedUserIsCurrentUser$ = this.currentUserService.getCurrentUser$()
      .pipe(
        switchMap((user: User) => {
          if (!!this.formData.user) {
            if (user.id === this.formData.user.id) {
              return this.currentUserService.isCurrentUserAdmin$();
            }
          }

          return of(false);
        }),
        shareReplay(),
      );

    const users$: Observable<User[]> = this.userService.getUsers$();
    this.subscriptions.push(combineLatest([users$, this.editedUserIsCurrentUser$])
      .subscribe(([users, adminCheckBox]: [User[], boolean]) => {
        for (const user of users) {
          if (!this.formData.user || (user.email !== this.formData.user.email)) {
            this.userEmails.Add(user.email);
          }
        }
        this.userForm = this.generateUserEditForm();
        if (!!this.formData.user) {
          this.userForm.patchValue(this.formData.user);
        }
        this.resetPasswordButtonDisabled = this.isPlayground;
      }));
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }

  onSave(): void {
    this.dialogRef.close(this.userForm.getRawValue());
  }

  resetUserPassword(): void {
    this.resetPasswordButtonDisabled = true;
    this.passwordService.sendPasswordResetEmail$({ email: this.formData.user.email })
      .pipe(take(1))
      .subscribe(() => {
        this.resetPasswordButtonDisabled = false;
        this.snackBar.open(this.passwordResetSuccessMsg, this.okMsg,
          {
            verticalPosition: 'top',
            duration: 20000,
          });
      });
  }

  private generateUserEditForm(): FormGroup {
    return this.formBuilder.group({
      id: [''],
      givenName: ['', Validators.required],
      surname: ['', Validators.required],
      email: ['', [Validators.required, Validators.email, emailAlreadyInUseValidatorFunction(this.userEmails)]],
      jobTitle: [''],
      department: [''],
      photo: [''],
      isAdmin: [false],
      active: [true],
    });
  }
}
