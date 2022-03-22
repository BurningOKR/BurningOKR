import { ApexAxisChartSeries, ApexChart, ApexNonAxisChartSeries } from 'ng-apexcharts';

export abstract class BaseChartOptions {
  series: ApexAxisChartSeries | ApexNonAxisChartSeries;
  chart: ApexChart;
}
