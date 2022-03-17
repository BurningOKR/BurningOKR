import { Component, Input, OnInit } from '@angular/core';
import {
  ApexAxisChartSeries,
  ApexChart,
  ApexDataLabels,
  ApexGrid,
  ApexStroke,
  ApexTitleSubtitle,
  ApexXAxis
} from 'ng-apexcharts';

interface LineChartOptions {
  lines: ApexAxisChartSeries; // NEEDED
  chart: ApexChart; // FIXED (Except zoom maybe)
  xaxis: ApexXAxis; // NEEDED
  dataLabels: ApexDataLabels; // FIXED
  grid: ApexGrid; // FIXED
  stroke: ApexStroke; // FIXED
  title: ApexTitleSubtitle; // NEEDED
}

export interface LineChartLineKeyValues {
  name: string;
  data: number[];
}

export interface LineChartLines {
  lines: LineChartLineKeyValues[];
}

export interface LineChartTitle {
  text: string;
  align: 'left' | 'right' | 'center';
}

export interface  LineChartXAxis{
  categories: string[];
}

@Component({
  selector: 'app-basic-line-chart',
  templateUrl: './basic-line-chart.component.html',
  styleUrls: ['./basic-line-chart.component.scss'],
})
export class BasicLineChartComponent implements OnInit{
  @Input() lineChartTitle!: LineChartTitle;
  @Input() chartLines!: LineChartLines;
  @Input() xAxis!: LineChartXAxis;
  @Input() lineType?: 'straight' | 'smooth' | 'stepline' = 'straight';
  @Input() zoomable? = false;
  @Input() showDataLabels? = false;

  chartOptions: LineChartOptions;
  ngOnInit(): void {
    this.chartOptions = this.createChartOptions();
  }

  private mapLinesToApexChartSeries(chartLines: LineChartLines): ApexAxisChartSeries {
    const apexLines: ApexAxisChartSeries = [];
    chartLines.lines.forEach(line => {
      apexLines.Add({
        name: line.name,
        data: line.data
      });
      console.log(line);
    });

    return apexLines;
  }

  private createChartOptions(): LineChartOptions {
    const apexLines: ApexAxisChartSeries = this.mapLinesToApexChartSeries(this.chartLines);

    return  {
      lines: apexLines,
      chart: {
        height: 350,
        type: 'line',
        zoom: {
          enabled: this.zoomable,
        }
      },
      dataLabels: {
        enabled: this.showDataLabels,
      },
      stroke: {
        curve: this.lineType,
      },
      title: this.lineChartTitle,
      grid: {
        row: {
          colors: ['#f3f3f3', 'transparent'], // takes an array which will be repeated on columns
          opacity: 0.5
        }
      },
      xaxis: {
        categories: this.xAxis.categories,
      }
    };
  }
}

/*
 this.chartOptions = {
      lines: this.lineKeyValues,
      chart: {
        height: 350,
        type: 'line',
        zoom: {
          enabled: this.zoomable,
        }
      },
      dataLabels: {
        enabled: this.showDataLabels,
      },
      stroke: {
        curve: this.lineType,
      },
      title: this.lineChartTitle,
      grid: {
        row: {
          colors: ['#f3f3f3', 'transparent'], // takes an array which will be repeated on columns
          opacity: 0.5
        }
      },
      xaxis: {
        categories: this.xAxisCategories,
      }
    };
 */
