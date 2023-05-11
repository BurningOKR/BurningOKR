import { Component, Injectable } from '@angular/core';
import { PieChartOptions } from '../../model/ui/pie-chart-options';
import { CustomChartComponent } from '../../decorator/chart-component.decorator';
import { BaseChartComponent } from '../base-chart/base-chart.component';

// (window as any).retain = [ CustomChartComponent ];
@Component({
  selector: 'app-pie-chart',
  templateUrl: './pie-chart.component.html',
  styleUrls: ['./pie-chart.component.scss'],
})
@CustomChartComponent(PieChartOptions)
@Injectable({
  providedIn: 'root',
})
export class PieChartComponent extends BaseChartComponent<PieChartOptions> {
}
