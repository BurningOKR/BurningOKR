import { ApexAxisChartSeries, ApexChart, ApexNonAxisChartSeries, ApexTitleSubtitle } from 'ng-apexcharts';

export abstract class BaseChartOptions {
  title: ApexTitleSubtitle;
  series: ApexAxisChartSeries | ApexNonAxisChartSeries;
  chart: ApexChart;
  teamIds?: number[]; //NEW
  colors = ['#b81f40', '#d1b37b', '#e7795c', '#48b69c', '#479fc0', '#6c548b', '#575756', '#b2b2b2', '#a3bbc8'];
}
