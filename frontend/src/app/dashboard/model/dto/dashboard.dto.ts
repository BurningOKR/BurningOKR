import { User } from '../../../shared/model/api/user';
import { BaseChartOptions } from '../../../shared/model/ui/dashboard/base-chart-options';

export interface DashboardDto {
  id: number;
  title: string;
  creator: User;
  chartDtos: BaseChartOptions[]; //TODO - P.B. 23-03-2022 Create Dto for BaseChartOptions
}
