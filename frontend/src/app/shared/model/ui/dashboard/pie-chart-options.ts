import { ApexResponsive} from 'ng-apexcharts';
import { BaseChartOptions } from './base-chart-options';

export class PieChartOptions extends BaseChartOptions{
  responsive: ApexResponsive[];
  labels: any;
}
