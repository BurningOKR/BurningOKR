import { Injectable } from '@angular/core';
import {
  LineChartLineKeyValues,
  LineChartOptions,
  LineChartTitle,
} from '../../shared/model/ui/dashboard/line-chart-options';

@Injectable({
  providedIn: 'root',
})
export class ChartOptionsBuilderService {

  buildLineChartOptions(lineChartTitle: LineChartTitle, lineChartValues: LineChartLineKeyValues[],
                        xAxisCategories: string[], showDataLabels?: boolean): LineChartOptions {

    return {
      lines: lineChartValues,
      chart: {
        height: 350,
        type: 'line',
        zoom: {
          enabled: false,
        },
      },
      dataLabels: {
        enabled: showDataLabels,
      },
      stroke: {
        curve: 'straight',
      },
      title: lineChartTitle,
      grid: {
        row: {
          colors: ['#f3f3f3', 'transparent'], // takes an array which will be repeated on columns
          opacity: 0.5,
        },
      },
      xaxis: {
        categories: xAxisCategories,
      },
    };
  }
}
