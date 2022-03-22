import { Component, Input } from '@angular/core';
import { PieChartOptions } from '../../../shared/model/ui/dashboard/pie-chart-options';
import { CustomChartComponent } from '../../decorator/chart-component.decorator';

@CustomChartComponent(PieChartOptions)
@Component({
  selector: 'app-pie-chart',
  templateUrl: './pie-chart.component.html',
  styleUrls: ['./pie-chart.component.scss']
})
export class PieChartComponent {
  @Input() pieChartOptions!: PieChartOptions;
}
