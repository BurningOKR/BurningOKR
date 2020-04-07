import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';
import { EditUserDialogData } from './edit-user-dialog-data';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { LocalUserManagementUser } from '../../user-management.component';
import { emailAlreadyInUseValidatorFunction } from '../email-already-in-use-validator-function';
import { LocalUserApiService } from '../../../../../../shared/services/api/local-user-api.service';
import { combineLatest, Observable, of } from 'rxjs';
import { shareReplay, switchMap } from 'rxjs/operators';
import { User } from '../../../../../../shared/model/api/user';

@Component({
  selector: 'app-edit-user-dialog',
  templateUrl: './edit-user-dialog.component.html',
  styleUrls: ['./edit-user-dialog.component.css']
})
export class EditUserDialogComponent implements OnInit {

  @ViewChild('canvasElement', { static: true }) canvas;
  userForm: FormGroup;
  emailsOfOtherUsers: string[] = [];
  adminCheckBoxDisabled$: Observable<boolean>;

  constructor(private dialogRef: MatDialogRef<EditUserDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public formData: EditUserDialogData,
              private formBuilder: FormBuilder,
              private userService: LocalUserApiService) {
  }

  ngOnInit(): void {
    // Disable Admin Checkbox when the user edits himself and is admin.
    this.adminCheckBoxDisabled$ = this.userService.getCurrentUser()
      .pipe(
        switchMap((user: User) => {
          if (user.id === this.formData.user.id) {
            return this.userService.isCurrentUserAdmin$();
          } else {
            return of(false);
          }
        }),
        shareReplay()
      );

    const users$: Observable<User[]> = this.userService.getUsers$();
    combineLatest([users$, this.adminCheckBoxDisabled$])
      .subscribe(([users, adminCheckBox]: [User[], boolean]) => {
        for (const user of users) {
          if (user.email !== this.formData.user.email) {
            this.emailsOfOtherUsers.Add(user.email);
          }
        }
        this.userForm = this.generateUserEditForm(this.formData.user);
      });
  }

  private generateUserEditForm(user: LocalUserManagementUser): FormGroup {
    return this.formBuilder.group({
      id: [user.id],
      givenName: [user.givenName, Validators.required],
      surname: [user.surname, Validators.required],
      email: [user.email, [Validators.required, Validators.email, emailAlreadyInUseValidatorFunction(this.emailsOfOtherUsers)]],
      jobTitle: [user.jobTitle],
      department: [user.department],
      photo: [user.photo],
      isAdmin: [user.isAdmin],
      active: [user.active]
    });
  }

  onSave(): void {
    this.dialogRef.close(this.userForm.value);
  }
}
