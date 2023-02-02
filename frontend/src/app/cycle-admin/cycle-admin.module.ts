import { TranslateModule } from '@ngx-translate/core';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CycleAdminContainerComponent } from './cycle-admin-container/cycle-admin-container.component';
import { CycleAdminCardComponent } from './cycle-admin-card/cycle-admin-card.component';
import { MatCardModule } from '@angular/material/card';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatMenuModule } from '@angular/material/menu';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatSelectModule } from '@angular/material/select';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatStepperModule } from '@angular/material/stepper';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatTooltipModule } from '@angular/material/tooltip';
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
  imports: [TranslateModule,
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
