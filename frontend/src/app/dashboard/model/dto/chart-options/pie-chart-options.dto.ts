import { PieChartOptions } from '../../ui/pie-chart-options';
import { BaseChartOptionsDto } from './base-chart-options.dto';

export abstract class PieChartOptionsDto extends BaseChartOptionsDto {
  series: number[];
  valueLabels: string[];

  buildChartOptions() {
    const pieChartOptions: PieChartOptions = new PieChartOptions();

    pieChartOptions.title = this.title;
    pieChartOptions.series = this.series;
    pieChartOptions.chart = {
      width: 400,
      type: 'pie',
    };
    pieChartOptions.labels = this.valueLabels;
    pieChartOptions.responsive = [
      {
        breakpoint: 480,
        options: {
          chart: {
            width: 300,
          },
          legend: {
            position: 'left',
          },
        },
      },
    ];

    return pieChartOptions;
  }
}
