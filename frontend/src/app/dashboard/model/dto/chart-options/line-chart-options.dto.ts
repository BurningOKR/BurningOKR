import { LineChartOptions } from '../../ui/line-chart-options';
import { BaseChartOptionsDto } from './base-chart-options.dto';

export abstract class LineChartOptionsDto extends BaseChartOptionsDto {
  series: LineChartLineKeyValues[];
  xAxisCategories: string[];

  buildChartOptions(): LineChartOptions {
    const lineChartOptions: LineChartOptions = new LineChartOptions();

    lineChartOptions.series = this.series;
    lineChartOptions.chart = {
      height: 350,
      type: 'line',
      zoom: {
        enabled: true,
      },
    };
    lineChartOptions.dataLabels = {
      enabled: false,
    };
    lineChartOptions.stroke = {
      curve: 'straight',
    };
    lineChartOptions.title = this.title;
    lineChartOptions.grid = {
      row: {
        colors: ['#f3f3f3', 'transparent'], // takes an array which will be repeated on columns
        opacity: 0.5,
      },
    };
    lineChartOptions.xaxis = {
      categories: this.xAxisCategories,
    };

    return lineChartOptions;
  }
}

export interface LineChartLineKeyValues {
  name: string;
  data: number[];
}
