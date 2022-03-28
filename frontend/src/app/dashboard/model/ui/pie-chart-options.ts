import { ApexResponsive } from 'ng-apexcharts';
import { BaseChartOptions } from './base-chart-options';
import { ChartTitle } from './line-chart-options';

export class PieChartOptions extends BaseChartOptions {
  responsive: ApexResponsive[];
  labels: any;
  title: ChartTitle = {
    text: 'Pie-Chart Titel oder so',
    align: 'left',
  };
}
