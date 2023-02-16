import { User } from '../../../shared/model/api/user';
import { BaseChartOptions } from './base-chart-options';

export interface Dashboard {
  id: number;
  title: string;
  creator: User;
  charts: BaseChartOptions[];
  creationDate: Date;
}
