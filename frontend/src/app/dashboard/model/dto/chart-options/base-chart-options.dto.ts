export interface BaseChartOptionsDto {
  title: ChartTitle;
  chart: 'pie' | 'line';
}

export interface ChartTitle {
  text: string;
  align: 'left' | 'right' | 'center';
}
