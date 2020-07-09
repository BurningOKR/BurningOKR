import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { InitRoutingModule } from './init-routing.module';
import { InitStateViewComponent } from './init-state-view/init-state-view.component';
import { CompleteInitStateFormComponent } from './init-state-view/init-state-forms/complete-init-state-form/complete-init-state-form.component';
import { CreateUserInitStateFormComponent } from './init-state-view/init-state-forms/create-user-init-state-form/create-user-init-state-form.component';
import { SetOauthClientDetailsFormComponent } from './init-state-view/init-state-forms/set-oauth-client-details-form/set-oauth-client-details-form.component';
import { SharedModule } from '../../../shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { UserManagementModule } from '../local-auth/user-management/user-management.module';
import { LocalAuthModule } from '../local-auth/local-auth.module';
import { MatCheckboxModule, MatFormFieldModule, MatInputModule } from '@angular/material';

@NgModule({
  declarations: [
    InitStateViewComponent,
    CompleteInitStateFormComponent,
    CreateUserInitStateFormComponent,
    SetOauthClientDetailsFormComponent
  ],
  imports: [
    CommonModule,
    InitRoutingModule,
    SharedModule,
    FormsModule,
    ReactiveFormsModule,
    UserManagementModule,
    LocalAuthModule,
    MatCheckboxModule,
    MatFormFieldModule,
    MatInputModule
  ],
  entryComponents: [
    CompleteInitStateFormComponent,
    CreateUserInitStateFormComponent,
    SetOauthClientDetailsFormComponent
  ]
})
export class InitModule { }
