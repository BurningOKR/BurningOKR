import { ApexChart, ApexResponsive, ApexNonAxisChartSeries } from 'ng-apexcharts';

export interface PieChartOptions {
  series: ApexNonAxisChartSeries;
  chart: ApexChart;
  responsive: ApexResponsive[];
  labels: any;
}
