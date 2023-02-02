import { TranslateModule } from '@ngx-translate/core';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatTooltipModule } from '@angular/material/tooltip';
import { ErrorComponent } from './error.component';
import { SharedModule } from '../../shared/shared.module';

@NgModule({
  declarations: [
    ErrorComponent
  ],
  imports: [TranslateModule,
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
