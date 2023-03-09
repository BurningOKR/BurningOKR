import { ChartInformationTypeEnum } from '../chart-creation-options.dto';

export abstract class BaseChartOptionsDto {
  title: string;
  chart: ChartInformationTypeEnum;
  teamIds?: number[]; //NEW

  public abstract buildChartOptions();
}

export interface ChartTitle {
  text: string;
  align: 'left';
}
