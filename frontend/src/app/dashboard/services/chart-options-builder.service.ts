import { Injectable } from '@angular/core';
import { ChartTitle } from '../model/dto/chart-options/base-chart-options.dto';
import { LineChartLineKeyValues } from '../model/dto/chart-options/line-chart-options.dto';
import { BaseChartOptions } from '../model/ui/base-chart-options';
import {
  LineChartOptions,

} from '../model/ui/line-chart-options';
import { PieChartOptions } from '../model/ui/pie-chart-options';

@Injectable({
  providedIn: 'root',
})
export class ChartOptionsBuilderService {
  buildLineChartOptions(lineChartTitle: ChartTitle, lineChartValues: LineChartLineKeyValues[],
                        xAxisCategories: string[]): LineChartOptions {
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
      enabled: false,
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
        name: 'Kunden auflisten',
        data: [0, 30, 30, 30, 65, 65, 65, 65, 100, 100, 100, 100],
      },
      {
        name: 'Kunden anschreiben',
        data: [0, 0, 30, 30, 30, 30, 65, 65, 65, 65, 100, 100],
      },
      {
        name: 'Für Kunden arbeiten',
        data: [0, 0, 0, 0, 0, 30, 30, 30, 30, 65, 65, 65],
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

    const chartOptions: BaseChartOptions[] = [];
    const lineChartOptions: LineChartOptions = this.buildLineChartOptions(lineChartTitle, chartLines, chartXAxis);
    const pieChartOptions: PieChartOptions = this.buildPieChartOptions(pieChartTitle,
      [20, 17, 13, 9], ['ToDo', 'Doing', 'Blocked', 'Done']);

    chartOptions.push(lineChartOptions);
    chartOptions.push(pieChartOptions);
    chartOptions.push(lineChartOptions);

    return chartOptions;
  }
}
