import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule, MatIconModule, MatToolbarModule, MatTooltipModule } from '@angular/material';
import { ErrorComponent } from './error.component';
import { SharedModule } from '../../shared/shared.module';

@NgModule({
  declarations: [
    ErrorComponent
  ],
  imports: [
    CommonModule,
    MatToolbarModule,
    MatIconModule,
    SharedModule,
    MatCardModule,
    MatTooltipModule,
  ],
  exports: [
    ErrorComponent
  ]
})
export class ErrorModule {
}
