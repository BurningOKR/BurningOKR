import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatTooltipModule } from '@angular/material/tooltip';
import { TranslateModule } from '@ngx-translate/core';
import { NgApexchartsModule } from 'ng-apexcharts';
import { AppRoutingModule } from '../app-routing.module';
import { SharedModule } from '../shared/shared.module';
import { DashboardComponent } from './sites/dashboard/dashboard.component';
import { BasicLineChartComponent } from './charts/basic-line-chart/basic-line-chart.component';
import { PieChartComponent } from './charts/pie-chart/pie-chart.component';
import { ChartRendererComponent } from './chart-renderer/chart-renderer.component';
import { ChartHostDirective } from './chart-renderer/chart-host.directive';
import { BaseChartComponent } from './charts/base-chart/base-chart.component';
import { DashboardOverviewComponent } from './sites/dashboard-overview/dashboard-overview.component';
import { CreateDashboardComponent } from './sites/create-dashboard/create-dashboard.component';
import { DashboardCardComponent } from './sites/dashboard-overview/dashboard-card/dashboard-card.component';

@NgModule({
    declarations: [
        DashboardComponent,
        BasicLineChartComponent,
        PieChartComponent,
        ChartRendererComponent,
        ChartHostDirective,
        BaseChartComponent,
        DashboardOverviewComponent,
        CreateDashboardComponent,
        DashboardCardComponent,
    ],
  imports: [
    CommonModule,
    NgApexchartsModule,
    AppRoutingModule,
    SharedModule,
    MatIconModule,
    MatTooltipModule,
    TranslateModule,
    MatInputModule,
    FormsModule,
    MatSelectModule,
  ],
})
export class DashboardModule {
}
