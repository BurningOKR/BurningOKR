import { OkrDepartment } from '../../../shared/model/ui/OrganizationalUnit/okr-department';

export interface ChartCreationOptionsDto {
  chartCreationOptionsId?: number;
  title: string;
  chartType: ChartTypeEnum;
  teams?: OkrDepartment[];
}

export enum ChartTypeEnum {
  pie = 'Übersicht Themenentwürfe',
  line = 'Fortschritt',
}
