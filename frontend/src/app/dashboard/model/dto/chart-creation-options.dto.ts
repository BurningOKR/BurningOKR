export interface ChartCreationOptionsDto {
  chartCreationOptionsId?: number;
  title: string;
  chartType: ChartInformationTypeEnum;
  teamIds?: number[];
}

export enum ChartInformationTypeEnum {
  LINE_PROGRESS,
  PIE_TOPICDRAFTOVERVIEW,
}

export const ChartTypeEnumDropDownRecord: Record<ChartInformationTypeEnum, string> = {
  [ChartInformationTypeEnum.LINE_PROGRESS]: 'Fortschritt - Liniendiagramm',
  [ChartInformationTypeEnum.PIE_TOPICDRAFTOVERVIEW]: 'Übersicht Themenentwürfe - Kuchendiagramm',
};

