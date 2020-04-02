import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { UserManagementRoutingModule } from './user-management-routing.module';
import { UserManagementComponent } from './user-management.component';
import { SharedModule } from '../../../../shared/shared.module';
import {
  MatCardModule,
  MatCheckboxModule,
  MatIconModule,
  MatInputModule,
  MatMenuModule,
  MatPaginatorModule,
  MatSortModule,
  MatTableModule,
  MatTooltipModule
} from '@angular/material';
import { EditUserDialogComponent } from './forms/edit-user-dialog/edit-user-dialog.component';
import { ReactiveFormsModule } from '@angular/forms';
import { AvatarModule } from 'ngx-avatar';
import { CreateUserDialogComponent } from './forms/create-user-dialog/create-user-dialog.component';
import { ResetPasswordDialogComponent } from './forms/reset-password-dialog/reset-password-dialog.component';
import { UserDialogComponent } from './forms/user-dialog/user-dialog.component';
import { ImportCsvDialogComponent } from './forms/import-csv-dialog/import-csv-dialog.component';
import { MaterialFileInputModule } from 'ngx-material-file-input';
import { UserFormComponent } from './forms/user-form/user-form.component';

@NgModule({
    declarations: [
        UserManagementComponent,
        EditUserDialogComponent,
        CreateUserDialogComponent,
        ResetPasswordDialogComponent,
        UserDialogComponent,
        ImportCsvDialogComponent,
        UserFormComponent
    ],
    imports: [
        CommonModule,
        UserManagementRoutingModule,
        SharedModule,
        MatIconModule,
        MatCardModule,
        MatTableModule,
        MatPaginatorModule,
        MatSortModule,
        MatInputModule,
        ReactiveFormsModule,
        MatTooltipModule,
        AvatarModule,
        MatMenuModule,
        MatCheckboxModule,
        MaterialFileInputModule,
    ],
    exports: [
    UserDialogComponent,
    UserFormComponent
  ],
    entryComponents: [EditUserDialogComponent, CreateUserDialogComponent, ResetPasswordDialogComponent, ImportCsvDialogComponent]
})
export class UserManagementModule {
}
