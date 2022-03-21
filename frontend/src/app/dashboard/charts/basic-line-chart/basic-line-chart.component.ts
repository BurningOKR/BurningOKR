import { Component, Input } from '@angular/core';
import { LineChartOptions } from '../../../shared/model/ui/dashboard/line-chart-options';

@Component({
  selector: 'app-basic-line-chart',
  templateUrl: './basic-line-chart.component.html',
  styleUrls: ['./basic-line-chart.component.scss'],
})
export class BasicLineChartComponent{
  @Input() lineChartOptions!: LineChartOptions;

}
