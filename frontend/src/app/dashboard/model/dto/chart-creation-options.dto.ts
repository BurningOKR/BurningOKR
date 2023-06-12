export interface ChartCreationOptionsDto {
  chartCreationOptionsId?: number;
  title: string;
  chartType: ChartInformationTypeEnum;
  selectedTeamIds?: number[];
}

export enum ChartInformationTypeEnum {
  LINE_PROGRESS,
  PIE_TOPICDRAFTOVERVIEW,
}

export const ChartTypeEnumDropDownRecord: Record<ChartInformationTypeEnum, string> = {
  [ChartInformationTypeEnum.LINE_PROGRESS]: 'edit-dashboard.line-progress',
  [ChartInformationTypeEnum.PIE_TOPICDRAFTOVERVIEW]: 'edit-dashboard.pie-topicdraft',
};

