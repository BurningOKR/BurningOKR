import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';
import { FormBuilder, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { FormGroupTyped } from '../../../../../../../typings';
import { UserDialogData } from '../user-dialog-data';

interface PasswordForm {
  password1: string;
  password2: string;
}

@Component({
  selector: 'app-reset-password-dialog',
  templateUrl: './reset-password-dialog.component.html',
  styleUrls: ['./reset-password-dialog.component.css']
})
export class ResetPasswordDialogComponent {

  passwordForm: FormGroupTyped<PasswordForm>;

  constructor(private dialogRef: MatDialogRef<ResetPasswordDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public formData: UserDialogData,
              private formBuilder: FormBuilder) {
    this.passwordForm = this.generateUserEditForm();
    this.passwordForm.setValidators(this.comparisonValidator());
  }

  comparisonValidator(): ValidatorFn {
    return (group: FormGroupTyped<PasswordForm>): ValidationErrors => {
      if (group.value.password1 !== group.value.password2) {
        group.controls.password2.setErrors({passwordsNotEqual: true});
      }

      return;
    };
  }

  handleSave(passewordForm: FormGroupTyped<PasswordForm>): void {
    this.dialogRef.close(passewordForm.value);
  }

  private generateUserEditForm(): FormGroupTyped<PasswordForm> {
    return this.formBuilder.group({
      password1: ['', [Validators.required]],
      password2: ['', [Validators.required]],
    }) as FormGroupTyped<PasswordForm>;
  }
}
