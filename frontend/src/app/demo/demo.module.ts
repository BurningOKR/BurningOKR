import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DemoRoutingModule } from './demo-routing.module';
import { DemoMainViewComponent } from './demo-main-view/demo-main-view.component';
import { DemoHomeComponent } from './demo-home/demo-home.component';
import { SharedModule } from '../shared/shared.module';
import { MatIconModule, MatMenuModule, MatTooltipModule } from '@angular/material';

@NgModule({
  declarations: [DemoMainViewComponent, DemoHomeComponent],
  imports: [
    CommonModule,
    DemoRoutingModule,
    SharedModule,
    MatIconModule,
    MatTooltipModule,
    MatMenuModule
  ]
})
export class DemoModule { }
