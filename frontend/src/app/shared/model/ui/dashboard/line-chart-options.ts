import {
  ApexAxisChartSeries,
  ApexChart,
  ApexDataLabels,
  ApexGrid,
  ApexStroke,
  ApexTitleSubtitle,
  ApexXAxis,
} from 'ng-apexcharts';

export interface LineChartOptions {
  lines: ApexAxisChartSeries;
  chart: ApexChart;
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
