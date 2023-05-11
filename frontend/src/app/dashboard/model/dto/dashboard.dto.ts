import { BaseChartOptionsDto } from './chart-options/base-chart-options.dto';
import { CompanyId, DashboardId, UserId } from '../../../shared/model/id-types';

/**
 * Used to deliver data from Backend to Frontend.
 * To transport data from Frontend to Backend see DashboardCreationDto.
 *
 * @see DashboardCreationDto
 */
export class DashboardDto {
  id: DashboardId;
  title: string;
  companyId: CompanyId;
  creatorId?: UserId;
  chartDtos: BaseChartOptionsDto[];
  creationDate: Date;
}
