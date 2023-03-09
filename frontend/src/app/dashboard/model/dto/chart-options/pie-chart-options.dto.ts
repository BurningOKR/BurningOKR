import { PieChartOptions } from '../../ui/pie-chart-options';
import { BaseChartOptionsDto } from './base-chart-options.dto';

export class PieChartOptionsDto extends BaseChartOptionsDto {
  series: number[];
  valueLabels: string[];

  buildChartOptions() {
    const pieChartOptions: PieChartOptions = new PieChartOptions();

    pieChartOptions.title = {
      text: this.title,
      align: 'left',
    };
    pieChartOptions.series = this.series;
    pieChartOptions.chart = {
      width: 400,
      type: 'pie',
    };
    pieChartOptions.chartType = this.chartType;
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
