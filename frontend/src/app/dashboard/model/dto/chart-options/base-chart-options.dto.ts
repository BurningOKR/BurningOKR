import { ChartInformationTypeEnum } from '../chart-creation-options.dto';

export abstract class BaseChartOptionsDto {
  title: string;
  chartType: ChartInformationTypeEnum;
  selectedTeamIds?: number[];

  public abstract buildChartOptions();
}

export interface ChartTitle {
  text: string;
  align: 'left';
}
