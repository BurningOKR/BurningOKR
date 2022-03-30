import { ApexAxisChartSeries, ApexChart, ApexNonAxisChartSeries, ApexTitleSubtitle } from 'ng-apexcharts';

export abstract class BaseChartOptions {
  title: ApexTitleSubtitle;
  series: ApexAxisChartSeries | ApexNonAxisChartSeries;
  chart: ApexChart;
}
