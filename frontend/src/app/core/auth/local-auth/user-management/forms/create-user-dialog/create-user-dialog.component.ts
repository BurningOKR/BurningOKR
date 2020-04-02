import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { LocalUserManagementUser } from '../../user-management.component';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';
import { FormBuilder, Validators } from '@angular/forms';
import { UserCreationDialogData } from './user-creation-dialog-data';
import { LocalUserApiService } from '../../../../../../shared/services/api/local-user-api.service';
import { emailAlreadyInUseValidatorFunction } from '../email-already-in-use-validator-function';
import { FormGroupTyped } from '../../../../../../../typings';

@Component({
  selector: 'app-create-user-dialog',
  templateUrl: './create-user-dialog.component.html',
  styleUrls: ['./create-user-dialog.component.css']
})
export class CreateUserDialogComponent implements OnInit {

  @ViewChild('canvas', {static: false}) canvas;
  userForm: FormGroupTyped<LocalUserManagementUser>;
  userEmails: string[] = [];

  constructor(private dialogRef: MatDialogRef<CreateUserDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public formData: UserCreationDialogData,
              private formBuilder: FormBuilder,
              private userService: LocalUserApiService) {
  }

  ngOnInit(): void {
    this.userService.getUsers$()
      .subscribe(users => {
        for (const user of users) {
          this.userEmails.Add(user.email);
        }
        this.userForm = this.generateUserCreationForm();
      });
  }

  private generateUserCreationForm(): FormGroupTyped<LocalUserManagementUser> {
    return this.formBuilder.group({
      givenName: ['', Validators.required],
      surname: ['', Validators.required],
      email: ['', [Validators.required, Validators.email, emailAlreadyInUseValidatorFunction(this.userEmails)]],
      jobTitle: [''],
      department: [''],
      photo: [''],
      isAdmin: [false],
      active: [true]
    }) as FormGroupTyped<LocalUserManagementUser>;
  }

  onSave(): void {
    this.dialogRef.close(this.userForm.value);
  }

}
