import { ChartCreationOptionsDto } from './chart-creation-options.dto';
import { CompanyId, DashboardId, UserId } from '../../../shared/model/id-types';

export interface DashboardCreationDto {
  id?: DashboardId;
  title: string;
  creatorId?: UserId;
  companyId: CompanyId;
  chartCreationOptions: ChartCreationOptionsDto[];
}
