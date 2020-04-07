import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { combineLatest, Observable, of } from 'rxjs';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';
import { LocalUserApiService } from '../../../../../../shared/services/api/local-user-api.service';
import { shareReplay, switchMap } from 'rxjs/operators';
import { User } from '../../../../../../shared/model/api/user';
import { emailAlreadyInUseValidatorFunction } from '../email-already-in-use-validator-function';
import { UserDialogData } from '../user-dialog-data';

@Component({
  selector: 'app-user-dialog',
  templateUrl: './user-dialog.component.html',
  styleUrls: ['./user-dialog.component.css']
})
export class UserDialogComponent implements OnInit {
  @ViewChild('canvasElement', {static: false}) canvas;

  userForm: FormGroup;
  userEmails: string[] = [];
  adminCheckBoxDisabled$: Observable<boolean>;

  constructor(private dialogRef: MatDialogRef<UserDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public formData: UserDialogData,
              private formBuilder: FormBuilder,
              private userService: LocalUserApiService) {
  }

  ngOnInit(): void {
    // Disable Admin Checkbox when the user edits himself and is admin.
    this.adminCheckBoxDisabled$ = this.userService.getCurrentUser()
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
    combineLatest([users$, this.adminCheckBoxDisabled$])
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
