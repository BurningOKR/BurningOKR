import { LineChartOptions } from '../../ui/line-chart-options';
import { BaseChartOptionsDto } from './base-chart-options.dto';

export class LineChartOptionsDto extends BaseChartOptionsDto {
  xaxisCategories: string[];
  series: LineChartLineKeyValues[];

  buildChartOptions(): LineChartOptions {
    console.log(this.xaxisCategories);
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
    lineChartOptions.title = {
      text: this.title,
      align: 'left',
    };
    lineChartOptions.grid = {
      row: {
        colors: ['#f3f3f3', 'transparent'], // takes an array which will be repeated on columns
        opacity: 0.5,
      },
    };
    lineChartOptions.xaxis = {
      categories: this.xaxisCategories,
    };

    return lineChartOptions;
  }
}

export interface LineChartLineKeyValues {
  name: string;
  data: number[];
}
