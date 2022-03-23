import { BaseChartOptions } from '../../../shared/model/ui/dashboard/base-chart-options';

export interface DashboardDto {
  id: number;
  title: string;
  chartDtos: BaseChartOptions[]; //TODO - P.B. 23-03-2022 Create Dto for BaseChartOptions
}
