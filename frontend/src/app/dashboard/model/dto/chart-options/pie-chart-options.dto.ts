import { BaseChartOptionsDto } from './base-chart-options.dto';

export interface PieChartOptionsDto extends BaseChartOptionsDto {
  series: number[];
}
