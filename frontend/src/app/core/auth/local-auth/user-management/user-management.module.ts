import { TranslateModule } from '@ngx-translate/core';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { UserManagementRoutingModule } from './user-management-routing.module';
import { UserManagementComponent } from './user-management.component';
import { SharedModule } from '../../../../shared/shared.module';
import { MatCardModule } from '@angular/material/card';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatMenuModule } from '@angular/material/menu';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { MatTableModule } from '@angular/material/table';
import { MatTooltipModule } from '@angular/material/tooltip';
import { ReactiveFormsModule } from '@angular/forms';
import { AvatarModule } from 'ngx-avatars';
import { UserDialogComponent } from './forms/user-dialog/user-dialog.component';
import { ImportCsvDialogComponent } from './forms/import-csv-dialog/import-csv-dialog.component';
import { MaterialFileInputModule } from 'ngx-material-file-input';
import { UserFormComponent } from './forms/user-form/user-form.component';

@NgModule({
  declarations: [
    UserManagementComponent,
    UserDialogComponent,
    ImportCsvDialogComponent,
    UserFormComponent
  ],
  imports: [TranslateModule,
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
  ]
})
export class UserManagementModule {
}
