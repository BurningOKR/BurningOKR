import { OkrUnitId } from '../../../shared/model/id-types';

export interface ChartCreationOptionsDto {
  title: string;
  chartType: ChartTypeEnum;
  okrUnitId: OkrUnitId;
}

export enum ChartTypeEnum {
  pie,
  line,
}
