import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CycleAdminContainerComponent } from './cycle-admin-container/cycle-admin-container.component';
import { CycleAdminCardComponent } from './cycle-admin-card/cycle-admin-card.component';
import {
  MatCardModule, MatDatepickerModule, MatDialogModule, MatFormFieldModule,
  MatIconModule, MatInputModule,
  MatMenuModule,
  MatProgressBarModule, MatSelectModule, MatSlideToggleModule, MatStepperModule,
  MatToolbarModule,
  MatTooltipModule
} from '@angular/material';
import { SharedModule } from '../shared/shared.module';
import { CycleCreationFormComponent } from './cycle-creation-form/cycle-creation-form.component';
import { CycleEditFormComponent } from './cycle-edit-form/cycle-edit-form.component';
import { ReactiveFormsModule } from '@angular/forms';

@NgModule({
  declarations: [
    CycleCreationFormComponent,
    CycleEditFormComponent,
    CycleAdminContainerComponent,
    CycleAdminCardComponent,
  ],
  entryComponents: [
    CycleCreationFormComponent,
    CycleEditFormComponent
  ],
  imports: [
    MatInputModule,
    CommonModule,
    MatToolbarModule,
    MatCardModule,
    MatIconModule,
    MatMenuModule,
    MatProgressBarModule,
    MatTooltipModule,
    SharedModule,
    SharedModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatDatepickerModule,
    MatDialogModule,
    MatStepperModule,
    MatSelectModule,
    MatSlideToggleModule
  ]
})
export class CycleAdminModule {
}
