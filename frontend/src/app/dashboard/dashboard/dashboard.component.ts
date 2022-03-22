import { Component } from '@angular/core';
import { BaseChartOptions } from '../../shared/model/ui/dashboard/base-chart-options';
import {
  LineChartLineKeyValues,
  LineChartOptions,
  LineChartTitle,
} from '../../shared/model/ui/dashboard/line-chart-options';
import { PieChartOptions } from '../../shared/model/ui/dashboard/pie-chart-options';
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
  pieChartOptions: PieChartOptions;

  chartOptions: BaseChartOptions[] = [];

  constructor(private chartOptionsBuilder: ChartOptionsBuilderService) {
    this.lineChartOptions = chartOptionsBuilder.buildLineChartOptions(this.chartTitle, this.chartLines, this.chartXAxis, true);
    this.pieChartOptions = chartOptionsBuilder.buildPieChartOptions([2,4,4,10], ['Zwei', 'VierEins', 'VierZwei', 'Zehn']);
    this.chartOptions.push(this.lineChartOptions);
    this.chartOptions.push(this.pieChartOptions);
  }
}
