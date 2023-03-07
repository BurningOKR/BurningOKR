import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Dashboard } from '../../../model/ui/dashboard';
import { ChartCreationOptionsDto } from '../../../model/dto/chart-creation-options.dto';
import { BaseChartOptions } from '../../../model/ui/base-chart-options';

@Component({
  selector: 'app-dashboard-modification',
  templateUrl: './dashboard-modification.component.html',
  styleUrls: ['./dashboard-modification.component.scss'],
})
export class DashboardModificationComponent {
  @Input() dashboard: Dashboard;
  @Output() updateDashboard: EventEmitter<Dashboard> = new EventEmitter<Dashboard>();
  @Output() clickedDelete: EventEmitter<ChartCreationOptionsDto> = new EventEmitter<ChartCreationOptionsDto>();

  deleteChart(chartToDelete: BaseChartOptions): void {
    this.dashboard.charts.splice(this.dashboard.charts.indexOf(chartToDelete), 1);
    // this.dashboard.charts.forEach((chart, index) => {
    //   if(chart===chartToDelete) {
    //     this.dashboard.charts.splice(index, 1);
    //   }
    // });
    // this.charts = this.charts.filter(chart => chart !== chartToDelete);
  }

}
