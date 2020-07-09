import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DemoRoutingModule } from './demo-routing.module';
import { DemoMainViewComponent } from './demo-main-view/demo-main-view.component';
import { DemoHomeComponent } from './demo-home/demo-home.component';
import { SharedModule } from '../shared/shared.module';
import { MatDialogModule, MatIconModule, MatMenuModule, MatTooltipModule } from '@angular/material';
import { DemoWarningComponent } from './demo-warning/demo-warning.component';

@NgModule({
  declarations: [DemoMainViewComponent, DemoHomeComponent, DemoWarningComponent],
  imports: [
    CommonModule,
    DemoRoutingModule,
    SharedModule,
    MatIconModule,
    MatTooltipModule,
    MatMenuModule,
    MatDialogModule
  ],
  entryComponents: [DemoWarningComponent]
})
export class DemoModule { }
