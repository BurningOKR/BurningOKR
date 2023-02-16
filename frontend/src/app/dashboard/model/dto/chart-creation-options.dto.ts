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
  [ChartInformationTypeEnum.LINE_PROGRESS]: 'create-dashboard.line-progress',
  [ChartInformationTypeEnum.PIE_TOPICDRAFTOVERVIEW]: 'create-dashboard.pie-topicdraft',
};

