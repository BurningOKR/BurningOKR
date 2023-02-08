import { ApexResponsive } from 'ng-apexcharts';
import { ChartTitle } from '../dto/chart-options/base-chart-options.dto';
import { BaseChartOptions } from './base-chart-options';

export class PieChartOptions extends BaseChartOptions {
  responsive: ApexResponsive[];
  labels: string[];
  title: ChartTitle = {
    text: 'Pie-Chart Titel oder so',
    align: 'left',
  };
}
