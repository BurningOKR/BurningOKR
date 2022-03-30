import { BaseChartOptionsDto } from './base-chart-options.dto';

export interface LineChartOptionsDto extends BaseChartOptionsDto {
  series: LineChartLineKeyValues[];
  xAxisCategories: string[];
}

export interface LineChartLineKeyValues {
  name: string;
  data: number[];
}
