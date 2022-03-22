import {
  ApexDataLabels,
  ApexGrid,
  ApexStroke,
  ApexXAxis,
} from 'ng-apexcharts';
import { BaseChartOptions } from './base-chart-options';

export class LineChartOptions extends BaseChartOptions{
  xaxis: ApexXAxis;
  dataLabels: ApexDataLabels;
  grid: ApexGrid;
  stroke: ApexStroke;
}

export interface LineChartLineKeyValues {
  name: string;
  data: number[];
}

export interface ChartTitle {
  text: string;
  align: 'left' | 'right' | 'center';
}
