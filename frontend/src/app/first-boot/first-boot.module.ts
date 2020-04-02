import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { FirstBootRoutingModule } from './first-boot-routing.module';
import { RegisterAdminComponent } from './register-admin/register-admin.component';
import { SharedModule } from '../shared/shared.module';
import { MatCardModule, MatInputModule } from '@angular/material';
import { ReactiveFormsModule } from '@angular/forms';

@NgModule({
  declarations: [RegisterAdminComponent],
  imports: [
    CommonModule,
    FirstBootRoutingModule,
    SharedModule,
    MatCardModule,
    MatInputModule,
    ReactiveFormsModule
  ]
})
export class FirstBootModule {
}
