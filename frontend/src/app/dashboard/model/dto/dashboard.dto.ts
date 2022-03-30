import { User } from '../../../shared/model/api/user';
import { BaseChartOptions } from '../ui/base-chart-options';

export interface DashboardDto {
  id: number;
  title: string;
  creator: User;
  chartDtos: BaseChartOptions[]; //TODO - P.B. 23-03-2022 Create Dto for BaseChartOptions
  creationDate: Date;
}
