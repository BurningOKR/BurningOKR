import { BaseChartOptionsDto } from './chart-options/base-chart-options.dto';
import { CompanyId, DashboardId, UserId } from '../../../shared/model/id-types';

/**
 * Used to transport data of existing Dashboards from Backend to Frontend and vice versa.
 *
 * @see DashboardCreationDto
 * for transporting data of newly created Dashboards
 */
export class DashboardDto {
  id: DashboardId;
  title: string;
  companyId: CompanyId;
  creatorId?: UserId;
  chartDtos: BaseChartOptionsDto[];
  creationDate: Date;
}
