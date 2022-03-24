import { User } from '../../../shared/model/api/user';
import { BaseChartOptions } from '../../../shared/model/ui/dashboard/base-chart-options';

export interface Dashboard {
  id: number;
  title: string;
  creator: User;
  charts: BaseChartOptions [];
}
