import { BaseChartOptions } from '../../../shared/model/ui/dashboard/base-chart-options';

export interface Dashboard {
  id: number;
  title: string;
  charts: BaseChartOptions [];
}
