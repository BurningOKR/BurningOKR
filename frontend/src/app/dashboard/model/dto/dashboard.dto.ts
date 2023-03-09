import { User } from '../../../shared/model/api/user';
import { BaseChartOptionsDto } from './chart-options/base-chart-options.dto';

export class DashboardDto {
  id: number;
  title: string;
  companyId: number;
  creator: User;
  chartDtos: BaseChartOptionsDto[];
  creationDate: Date;
}
