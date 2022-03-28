import { Component, Input } from '@angular/core';
import { BaseChartOptions } from '../../model/ui/base-chart-options';

@Component({
  selector: 'app-base-chart',
  templateUrl: './base-chart.component.html',
  styleUrls: ['./base-chart.component.scss']
})
export class BaseChartComponent<TChartOptions extends BaseChartOptions> {
  @Input() chartOptions: TChartOptions;
}
