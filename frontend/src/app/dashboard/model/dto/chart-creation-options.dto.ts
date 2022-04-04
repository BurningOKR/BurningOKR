export interface ChartCreationOptionsDto {
  chartCreationOptionsId?: number;
  title: string;
  chartType: ChartTypeEnum;
  teams?: number[];
}

export enum ChartTypeEnum {
  PIE = 'Übersicht Themenentwürfe',
  LINE = 'Fortschritt',
}
