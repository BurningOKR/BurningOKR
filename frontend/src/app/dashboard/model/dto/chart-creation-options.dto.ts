export interface ChartCreationOptionsDto {
  chartCreationOptionsId?: number;
  title: string;
  chartType: ChartTypeEnum;
  teamIds?: number[];
}

export enum ChartTypeEnum {
  LINE_PROGRESS,
  PIE_TOPICDRAFTOVERVIEW,
}
