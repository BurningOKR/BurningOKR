import { User } from '../../../shared/model/api/user';
import { BaseChartOptionsDto } from './chart-options/base-chart-options.dto';

export abstract class DashboardDto {
  id: number;
  title: string;
  creator: User;
  chartDtos: BaseChartOptionsDto[];
  creationDate: Date;
}
