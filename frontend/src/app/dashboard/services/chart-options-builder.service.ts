import { Injectable } from '@angular/core';
import { BaseChartOptions } from '../../shared/model/ui/dashboard/base-chart-options';
import {
  LineChartLineKeyValues,
  LineChartOptions,
  ChartTitle,
} from '../../shared/model/ui/dashboard/line-chart-options';
import { PieChartOptions } from '../../shared/model/ui/dashboard/pie-chart-options';

@Injectable({
  providedIn: 'root',
})
export class ChartOptionsBuilderService {

  buildLineChartOptions(lineChartTitle: ChartTitle, lineChartValues: LineChartLineKeyValues[],
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

  buildPieChartOptions(pieChartTitle: ChartTitle, pieChartValues: number[], pieChartValueLabels: string[]):
    PieChartOptions {
    const pieChartOptions: PieChartOptions = new PieChartOptions();

    pieChartOptions.title = pieChartTitle;
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

  buildTestCharts(): BaseChartOptions[] {
    const chartLines: LineChartLineKeyValues[] = [
      {
        name: 'Objective 1',
        data: [1, 5, 10, 20, 45],
      },
      {
        name: 'Objective 2',
        data: [5, 34, 36, 36, 50],
      },
    ];
    const chartTitle: ChartTitle = {
      text: 'Fortschritt der Objectives',
      align: 'left',
    };
    const chartXAxis: string[] = ['Tag 1', 'Tag 2', 'Tag 3', 'Tag 4', 'Tag 5'];

    const  chartOptions: BaseChartOptions[] = [];
    const lineChartOptions: LineChartOptions = this.buildLineChartOptions(chartTitle,chartLines, chartXAxis, true);
    const pieChartOptions: PieChartOptions = this.buildPieChartOptions(chartTitle, [2, 4,10], ['Zwei', 'Vier', 'Zehn']);

    chartOptions.push(lineChartOptions);
    chartOptions.push(pieChartOptions);

    return chartOptions;
  }
}
