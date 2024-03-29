import { Component } from '@angular/core';
import { LineChartOptions } from '../../model/ui/line-chart-options';
import { BaseChartComponent } from '../base-chart/base-chart.component';

@Component({
  selector: 'app-basic-line-chart',
  templateUrl: './line-chart.component.html',
  styleUrls: ['./line-chart.component.scss'],
})
export class LineChartComponent extends BaseChartComponent<LineChartOptions> {
}
