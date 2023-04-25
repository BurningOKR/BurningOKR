import { ApexAxisChartSeries, ApexChart, ApexNonAxisChartSeries, ApexTitleSubtitle } from 'ng-apexcharts';
import { ChartInformationTypeEnum } from '../dto/chart-creation-options.dto';
import { ChartId } from '../../../shared/model/id-types';

export abstract class BaseChartOptions {
  id: ChartId;
  title: ApexTitleSubtitle = {
    text: '',
    align: 'left',
  };
  series: ApexAxisChartSeries | ApexNonAxisChartSeries;
  chart: ApexChart;
  chartType: ChartInformationTypeEnum;
  selectedTeamIds?: number[];
  colors = ['#b81f40', '#d1b37b', '#e7795c', '#48b69c', '#479fc0', '#6c548b', '#575756', '#b2b2b2', '#a3bbc8'];
  chartOptionsName: string = '';
}
