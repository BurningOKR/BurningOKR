import { Component } from '@angular/core';
import { LineChartOptions } from '../../model/ui/line-chart-options';
import { CustomChartComponent } from '../../decorator/chart-component.decorator';
import { BaseChartComponent } from '../base-chart/base-chart.component';

@Component({
  selector: 'app-basic-line-chart',
  templateUrl: './basic-line-chart.component.html',
  styleUrls: ['./basic-line-chart.component.scss'],
})
@CustomChartComponent(LineChartOptions)
export class BasicLineChartComponent extends BaseChartComponent<LineChartOptions> {
}
