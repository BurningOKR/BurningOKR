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

export const ChartTypeEnumRecord: Record<ChartTypeEnum, string> = {
  [ChartTypeEnum.LINE_PROGRESS]: 'Fortschritt - Liniendiagramm',
  [ChartTypeEnum.PIE_TOPICDRAFTOVERVIEW]: 'Übersicht Themenentwürfe - Kuchendiagramm',
};
