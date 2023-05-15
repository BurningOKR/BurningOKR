import { Component } from '@angular/core';
import { PieChartOptions } from '../../model/ui/pie-chart-options';
import { BaseChartComponent } from '../base-chart/base-chart.component';

@Component({
  selector: 'app-pie-chart',
  templateUrl: './pie-chart.component.html',
  styleUrls: ['./pie-chart.component.scss'],
})
export class PieChartComponent extends BaseChartComponent<PieChartOptions> {
}
