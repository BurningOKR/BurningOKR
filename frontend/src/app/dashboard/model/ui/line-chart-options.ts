import { ApexDataLabels, ApexGrid, ApexStroke, ApexXAxis } from 'ng-apexcharts';
import { BaseChartOptions } from './base-chart-options';

export class LineChartOptions extends BaseChartOptions{
  xaxis: ApexXAxis;
  dataLabels: ApexDataLabels;
  grid: ApexGrid;
  stroke: ApexStroke;
}

