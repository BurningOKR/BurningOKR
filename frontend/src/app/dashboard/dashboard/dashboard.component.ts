import { Component } from '@angular/core';
import { LineChartLines, LineChartTitle, LineChartXAxis } from '../charts/basic-line-chart/basic-line-chart.component';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
})
export class DashboardComponent {
  chartLines: LineChartLines = {
    lines: [
      {
        name: 'Objective 1',
        data: [1, 5, 10, 20, 45],
      },
      {
        name: 'Objective 2',
        data: [5, 34, 36, 36, 50],
      },
    ],
  };

  chartTitle: LineChartTitle = {
    text: 'Fortschritt der Objectives',
    align: 'left',
  };
  chartXAxis: LineChartXAxis = {
    categories: ['Tag 1', 'Tag 2', 'Tag 3', 'Tag 4', 'Tag 5'],
  };
}
