import { ChartCreationOptionsDto } from './chart-creation-options.dto';

export interface DashboardCreationDto {
  dashboardCreationId?: number;
  title: string;
  charts: ChartCreationOptionsDto[];
}
