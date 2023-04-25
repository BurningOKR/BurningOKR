import { ApexDataLabels, ApexGrid, ApexOptions, ApexStroke, ApexXAxis } from 'ng-apexcharts';
import { BaseChartOptions } from './base-chart-options';
import { ChartInformationTypeEnum } from '../dto/chart-creation-options.dto';

export class LineChartOptions extends BaseChartOptions {
  xaxis: ApexXAxis;
  dataLabels: ApexDataLabels;
  grid: ApexGrid;
  stroke: ApexStroke;
  options: ApexOptions;
  chartType: ChartInformationTypeEnum = ChartInformationTypeEnum.LINE_PROGRESS;
  selectedTeamIds: number[] = [];
  chartOptionsName: string = 'LineChartOptions';
}

