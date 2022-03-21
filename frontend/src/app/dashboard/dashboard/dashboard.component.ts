import { Component } from '@angular/core';
import {
  LineChartLineKeyValues,
  LineChartOptions,
  LineChartTitle,
} from '../../shared/model/ui/dashboard/line-chart-options';
import { ChartOptionsBuilderService } from '../services/chart-options-builder.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
})
export class DashboardComponent {
  chartLines: LineChartLineKeyValues[] = [
    {
      name: 'Objective 1',
      data: [1, 5, 10, 20, 45],
    },
    {
      name: 'Objective 2',
      data: [5, 34, 36, 36, 50],
    },
  ];

  chartTitle: LineChartTitle = {
    text: 'Fortschritt der Objectives',
    align: 'left',
  };
  chartXAxis = ['Tag 1', 'Tag 2', 'Tag 3', 'Tag 4', 'Tag 5'];

  lineChartOptions: LineChartOptions;
  constructor(private chartOptionsBuilder: ChartOptionsBuilderService) {
    this.lineChartOptions = chartOptionsBuilder.buildLineChartOptions(this.chartTitle, this.chartLines, this.chartXAxis, true);
  }
}
