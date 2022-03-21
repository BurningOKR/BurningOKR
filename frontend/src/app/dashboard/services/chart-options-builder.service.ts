import { Injectable } from '@angular/core';
import {
  LineChartLineKeyValues,
  LineChartOptions,
  LineChartTitle,
} from '../../shared/model/ui/dashboard/line-chart-options';
import { PieChartOptions } from '../../shared/model/ui/dashboard/pie-chart-options';

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

  buildPieChartOptions(pieChartValues: number[], pieChartvalueLabels: string[]): PieChartOptions {
    return {
      series: pieChartValues,
      chart: {
        width: 380,
        type: 'pie',
      },
      labels: pieChartvalueLabels,
      responsive: [
        {
          breakpoint: 480,
          options: {
            chart: {
              width: 200,
            },
            legend: {
              position: 'left',
            },
          },
        },
      ],
    };
  }
}
