import { BaseChartOptions } from './base-chart-options';
import { CompanyId, DashboardId, UserId } from '../../../shared/model/id-types';

export interface Dashboard {
  id: DashboardId;
  title: string;
  companyId: CompanyId;
  creatorId?: UserId;
  charts: BaseChartOptions[];
  creationDate: Date;
}
