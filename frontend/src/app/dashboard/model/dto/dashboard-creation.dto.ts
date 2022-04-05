import { ChartCreationOptionsDto } from './chart-creation-options.dto';

export interface DashboardCreationDto {
  dashboardCreationId?: number;
  title: string;
  creatorId?: number;
  companyId: number;
  chartCreationOptions: ChartCreationOptionsDto[];
}
