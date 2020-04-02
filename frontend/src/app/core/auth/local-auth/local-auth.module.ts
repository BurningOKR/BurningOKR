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
import { InitStateViewComponent } from './init/init-state-view/init-state-view.component';
import { CompleteInitStateFormComponent } from './init/init-state-view/init-state-forms/complete-init-state-form/complete-init-state-form.component';
import { WelcomeInitStateFormComponent } from './init/init-state-view/init-state-forms/welcome-init-state-form/welcome-init-state-form.component';
import { CreateUserInitStateFormComponent } from './init/init-state-view/init-state-forms/create-user-init-state-form/create-user-init-state-form.component';
import { SetOauthClientDetailsFormComponent } from './init/init-state-view/init-state-forms/set-oauth-client-details-form/set-oauth-client-details-form.component';

@NgModule({
  declarations: [
    LoginComponent,
    ResetPasswordComponent,
    SetPasswordComponent,
    PasswordFormComponent,
    ChangePasswordDialogComponent,
    InitStateViewComponent,
    CreateUserInitStateFormComponent,
    CompleteInitStateFormComponent,
    WelcomeInitStateFormComponent,
    SetOauthClientDetailsFormComponent,
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
    CreateUserInitStateFormComponent,
    WelcomeInitStateFormComponent,
    CompleteInitStateFormComponent,
    SetOauthClientDetailsFormComponent,
  ],
  exports: []
})
export class LocalAuthModule {
}
