import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgApexchartsModule } from 'ng-apexcharts';
import { DashboardComponent } from './dashboard/dashboard.component';
import { BasicLineChartComponent } from './charts/basic-line-chart/basic-line-chart.component';
import { PieChartComponent } from './charts/pie-chart/pie-chart.component';
import { ChartRendererComponent } from './chart-renderer/chart-renderer.component';

@NgModule({
  declarations: [
    DashboardComponent,
    BasicLineChartComponent,
    PieChartComponent,
    ChartRendererComponent
  ],
  imports: [
    CommonModule,
    NgApexchartsModule
  ]
})
export class DashboardModule { }
