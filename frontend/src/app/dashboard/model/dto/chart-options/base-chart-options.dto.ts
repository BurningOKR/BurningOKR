import { ChartInformationTypeEnum } from '../chart-creation-options.dto';
import { ChartId } from '../../../../shared/model/id-types';

export abstract class BaseChartOptionsDto {
  id: ChartId;
  title: string;
  chartType: ChartInformationTypeEnum;
  selectedTeamIds?: number[];

  public abstract buildChartOptions();
}

export interface ChartTitle {
  text: string;
  align: 'left';
}
