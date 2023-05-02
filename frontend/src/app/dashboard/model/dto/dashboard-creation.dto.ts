import { ChartCreationOptionsDto } from './chart-creation-options.dto';
import { CompanyId, DashboardId, UserId } from '../../../shared/model/id-types';

/**
 * Used to deliver data from Frontend to Backend.
 * To transport data from Backend to Frontend, see the DashboardDto.
 * DashboardId and date of creation are not set in the Frontend but in the Backend (for new Dashboards).
 */
export interface DashboardCreationDto {
  id?: DashboardId;
  title: string;
  creatorId?: UserId;
  companyId: CompanyId;
  chartCreationOptions: ChartCreationOptionsDto[];
}
