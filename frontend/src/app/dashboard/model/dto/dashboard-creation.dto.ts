import { ChartCreationOptionsDto } from './chart-creation-options.dto';

export interface DashboardCreationDto {
  id?: number;
  title: string;
  creatorId?: number;
  companyId: number;
  chartCreationOptions: ChartCreationOptionsDto[];
}
