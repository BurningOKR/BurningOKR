import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { combineLatest, Observable, of } from 'rxjs';
import { MAT_DIALOG_DATA, MatDialogRef, MatSnackBar } from '@angular/material';
import { LocalUserApiService } from '../../../../../../shared/services/api/local-user-api.service';
import { shareReplay, switchMap } from 'rxjs/operators';
import { User } from '../../../../../../shared/model/api/user';
import { emailAlreadyInUseValidatorFunction } from '../email-already-in-use-validator-function';
import { UserDialogData } from '../user-dialog-data';
import { PasswordService } from '../../../password-service/password.service';
import { I18n } from '@ngx-translate/i18n-polyfill';

@Component({
  selector: 'app-user-dialog',
  templateUrl: './user-dialog.component.html',
  styleUrls: ['./user-dialog.component.css']
})
export class UserDialogComponent implements OnInit {
  @ViewChild('canvasElement', { static: false }) canvas;

  userForm: FormGroup;
  userEmails: string[] = [];
  editedUserIsCurrentUser$: Observable<boolean>;
  resetPasswordButtonDisabled: boolean;

  private passwordResetSuccessMsg: string = this.i18n({
    id: 'resetPasswordSuccessSnackbar',
    description: 'Snackbar that is shown when a password was reset successfully',
    value: 'Das Passwort wurde erfolgreich zurückgesetzt.'
  });

  private okMsg: string = this.i18n({
    id: 'resetPasswordSuccesOK',
    description: 'Ok Button on resetPasswordSuccessSnackbar',
    value: 'Ok'
  });

  constructor(private dialogRef: MatDialogRef<UserDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public formData: UserDialogData,
              private formBuilder: FormBuilder,
              private userService: LocalUserApiService,
              private passwordService: PasswordService,
              private snackBar: MatSnackBar,
              private i18n: I18n) {
  }

  ngOnInit(): void {
    // Disable Admin Checkbox when the user edits himself and is admin.
    this.editedUserIsCurrentUser$ = this.userService.getCurrentUser$()
      .pipe(
        switchMap((user: User) => {
          if (!!this.formData.user) {
            if (user.id === this.formData.user.id) {
              return this.userService.isCurrentUserAdmin$();
            }
          }

          return of(false);
        }),
        shareReplay()
      );

    const users$: Observable<User[]> = this.userService.getUsers$();
    combineLatest([users$, this.editedUserIsCurrentUser$])
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
      });
  }

  onSave(): void {
    this.dialogRef.close(this.userForm.value);
  }

  resetUserPassword(): void {
    this.resetPasswordButtonDisabled = true;
    this.passwordService.sendPasswordResetEmail$({ email: this.formData.user.email })
      .subscribe(() => {
        this.resetPasswordButtonDisabled = false;
        this.snackBar.open(this.passwordResetSuccessMsg, this.okMsg,
          {
            verticalPosition: 'top',
            duration: 20000
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
      active: [true]
    });
  }
}
