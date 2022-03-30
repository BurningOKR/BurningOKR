import { LineChartLineKeyValues } from './line-chart-options.dto';

export abstract class BaseChartOptionsDto {
  series: LineChartLineKeyValues[] | number[];
  title: ChartTitle;
  chart: 'pie' | 'line';

  abstract buildChartOptions();
}

export interface ChartTitle {
  text: string;
  align: 'left' | 'right' | 'center';
}
