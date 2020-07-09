import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { LocalAuthRoutingModule } from './local-auth-routing.module';
import { LoginComponent } from './login/login.component';
import { ResetPasswordComponent } from './reset-password/reset-password.component';
import { SharedModule } from '../../../shared/shared.module';
import {
  MatCardModule,
  MatCheckboxModule,
  MatFormFieldModule,
  MatIconModule,
  MatInputModule,
  MatStepperModule
} from '@angular/material';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { SetPasswordComponent } from './set-password/set-password.component';
import { UserManagementModule } from './user-management/user-management.module';
import { PasswordFormComponent } from './password-form/password-form.component';
import { ChangePasswordDialogComponent } from './change-password-dialog/change-password-dialog.component';

@NgModule({
  declarations: [
    LoginComponent,
    ResetPasswordComponent,
    SetPasswordComponent,
    PasswordFormComponent,
    ChangePasswordDialogComponent,
  ],
  imports: [
    CommonModule,
    LocalAuthRoutingModule,
    SharedModule,
    MatCardModule,
    ReactiveFormsModule,
    FormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatStepperModule,
    UserManagementModule,
    MatCheckboxModule,
    MatIconModule
  ],
  entryComponents: [
    ChangePasswordDialogComponent,
  ],
  exports: [
    PasswordFormComponent
  ]
})
export class LocalAuthModule {
}
