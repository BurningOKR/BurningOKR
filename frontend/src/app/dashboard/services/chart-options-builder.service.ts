import { Injectable } from '@angular/core';
import { BaseChartOptions } from '../model/ui/base-chart-options';
import {
  LineChartLineKeyValues,
  LineChartOptions,
  ChartTitle,
} from '../model/ui/line-chart-options';
import { PieChartOptions } from '../model/ui/pie-chart-options';

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
      width: 400,
      type: 'pie',
    };
    pieChartOptions.labels = pieChartValueLabels;
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

  buildTestCharts(): BaseChartOptions[] {
    const chartLines: LineChartLineKeyValues[] = [
      {
        name: 'Objective 1',
        data: [0, 3, 19, 24, 25, 30, 41, 52, 60, 68, 81, 100],
      },
      {
        name: 'Objective 2',
        data: [0, 10, 14, 20, 28, 33, 41, 55, 65, 73, 78, 90],
      },
    ];
    const lineChartTitle: ChartTitle = {
      text: 'Objective-Progress in %',
      align: 'left',
    };

    const pieChartTitle: ChartTitle = {
      text: 'TopicDraft states',
      align: 'left',
    };
    const chartXAxis: string[] = ['January', 'February', 'March', 'April', 'Mai', 'June', 'July',
      'August', 'September', 'October', 'November', 'December'];

    const  chartOptions: BaseChartOptions[] = [];
    const lineChartOptions: LineChartOptions = this.buildLineChartOptions(lineChartTitle,chartLines, chartXAxis, false);
    const pieChartOptions: PieChartOptions = this.buildPieChartOptions(pieChartTitle, [10,5, 3 ,4], ['ToDo', 'Doing', 'Blocked', 'Done']);

    chartOptions.push(lineChartOptions);
    chartOptions.push(pieChartOptions);
    chartOptions.push(lineChartOptions);
    chartOptions.push(lineChartOptions);
    chartOptions.push(pieChartOptions);
    chartOptions.push(pieChartOptions);

    return chartOptions;
  }
}
