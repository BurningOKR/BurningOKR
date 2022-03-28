export interface ChartCreationOptionsDto {
  title: string;
  chartType: ChartTypeEnum;
  teamId: number;
}

export enum ChartTypeEnum {
  pie = 'Übersicht Themenentwürfe',
  line = 'Fortschritt',
}
