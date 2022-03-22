import { ApexResponsive} from 'ng-apexcharts';
import { BaseChartOptions } from './base-chart-options';

export interface PieChartOptions extends BaseChartOptions{
  responsive: ApexResponsive[];
  labels: any;
}
