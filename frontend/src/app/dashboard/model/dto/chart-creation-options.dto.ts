export interface ChartCreationOptionsDto {
  chartCreationOptionsId?: number;
  title: string;
  chartType: ChartTypeEnum;
  informationType: InformationTypeEnum;
  teamIds?: number[];
}

export enum ChartTypeEnum {
  LINE,
  PIE,
}

export enum InformationTypeEnum {
  PROGRESS,
  TOPICDRAFTOVERVIEW,
}

export const ChartTypeEnumMapping: Record<ChartTypeEnum, string> = {
  [ChartTypeEnum.LINE]: 'Fortschritt',
  [ChartTypeEnum.PIE]: 'Übersicht Themenentwürfe',
};
