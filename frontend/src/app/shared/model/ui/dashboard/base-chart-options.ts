import { ApexAxisChartSeries, ApexChart, ApexNonAxisChartSeries } from 'ng-apexcharts';

export interface BaseChartOptions {
  series: ApexAxisChartSeries | ApexNonAxisChartSeries;
  chart: ApexChart;
}
