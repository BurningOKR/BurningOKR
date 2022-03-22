import { Component } from '@angular/core';
import { LineChartOptions } from '../../../shared/model/ui/dashboard/line-chart-options';
import { CustomChartComponent } from '../../decorator/chart-component.decorator';
import { BaseChartComponent } from '../base-chart/base-chart.component';

@CustomChartComponent(LineChartOptions)
@Component({
  selector: 'app-basic-line-chart',
  templateUrl: './basic-line-chart.component.html',
  styleUrls: ['./basic-line-chart.component.scss'],
})
export class BasicLineChartComponent extends BaseChartComponent<LineChartOptions>{
}
