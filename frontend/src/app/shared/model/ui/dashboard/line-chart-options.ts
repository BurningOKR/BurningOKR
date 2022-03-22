import {
  ApexDataLabels,
  ApexGrid,
  ApexStroke,
  ApexTitleSubtitle,
  ApexXAxis,
} from 'ng-apexcharts';
import { BaseChartOptions } from './base-chart-options';

export class LineChartOptions extends BaseChartOptions{
  xaxis: ApexXAxis;
  dataLabels: ApexDataLabels;
  grid: ApexGrid;
  stroke: ApexStroke;
  title: ApexTitleSubtitle;
}

export interface LineChartLineKeyValues {
  name: string;
  data: number[];
}

export interface LineChartTitle {
  text: string;
  align: 'left' | 'right' | 'center';
}
