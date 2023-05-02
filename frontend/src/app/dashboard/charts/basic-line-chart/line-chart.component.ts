import { Component, Injectable } from '@angular/core';
import { LineChartOptions } from '../../model/ui/line-chart-options';
import { CustomChartComponent } from '../../decorator/chart-component.decorator';
import { BaseChartComponent } from '../base-chart/base-chart.component';

@Component({
  selector: 'app-basic-line-chart',
  templateUrl: './line-chart.component.html',
  styleUrls: ['./line-chart.component.scss'],
})
@CustomChartComponent(LineChartOptions)
@Injectable({
  providedIn: 'root',
})
export class LineChartComponent extends BaseChartComponent<LineChartOptions> {
}
