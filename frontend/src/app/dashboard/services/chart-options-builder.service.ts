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
    const lineChartOptions: LineChartOptions = new LineChartOptions();

    lineChartOptions.series = lineChartValues;
    lineChartOptions.chart = {
      height: 350,
      type: 'line',
      zoom: {
        enabled: false,
      },
    };
    lineChartOptions.dataLabels = {
      enabled: showDataLabels,
    };
    lineChartOptions.stroke = {
      curve: 'straight',
    };
    lineChartOptions.title = lineChartTitle;
    lineChartOptions.grid = {
      row: {
        colors: ['#f3f3f3', 'transparent'], // takes an array which will be repeated on columns
        opacity: 0.5,
      },
    };
    lineChartOptions.xaxis = {
      categories: xAxisCategories,
    };

    return lineChartOptions;
  }

  buildPieChartOptions(pieChartValues: number[], pieChartValueLabels: string[]):
    PieChartOptions {
    const pieChartOptions: PieChartOptions = new PieChartOptions();

    pieChartOptions.series = pieChartValues;
    pieChartOptions.chart = {
      width: 380,
      type: 'pie',
    };
    pieChartOptions.labels = pieChartValueLabels;
    pieChartOptions.responsive = [
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
    ];

    return pieChartOptions;
  }
}
