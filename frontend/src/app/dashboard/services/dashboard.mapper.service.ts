import { Injectable } from '@angular/core';
import { ChartInformationTypeEnum } from '../model/dto/chart-creation-options.dto';
import { DashboardDto } from '../model/dto/dashboard.dto';
import { Dashboard } from '../model/ui/dashboard';
import { LineChartOptionsDto } from '../model/dto/chart-options/line-chart-options.dto';
import { PieChartOptionsDto } from '../model/dto/chart-options/pie-chart-options.dto';
import { BaseChartOptionsDto } from '../model/dto/chart-options/base-chart-options.dto';
import { ChartMapperService } from './chart.mapper.service';

@Injectable({
  providedIn: 'root',
})
export class DashboardMapperService {

  constructor(private chartMapper: ChartMapperService) {
  }

  static mapLineChartOptions(chartOptionsDto: LineChartOptionsDto): LineChartOptionsDto {
    const lineChartOptions: LineChartOptionsDto = new LineChartOptionsDto();
    lineChartOptions.id = chartOptionsDto.id;
    lineChartOptions.chartType = chartOptionsDto.chartType;
    lineChartOptions.title = chartOptionsDto.title;
    lineChartOptions.selectedTeamIds = chartOptionsDto.selectedTeamIds;
    lineChartOptions.xaxisCategories = chartOptionsDto.xaxisCategories;
    lineChartOptions.series = chartOptionsDto.series;

    return lineChartOptions;
  }

  static mapPieChartOptions(chartOptionsDto: PieChartOptionsDto): PieChartOptionsDto {
    const pieChartOptions: PieChartOptionsDto = new PieChartOptionsDto();
    pieChartOptions.id = chartOptionsDto.id;
    pieChartOptions.chartType = chartOptionsDto.chartType;
    pieChartOptions.title = chartOptionsDto.title;
    pieChartOptions.selectedTeamIds = chartOptionsDto.selectedTeamIds;
    pieChartOptions.series = chartOptionsDto.series;
    pieChartOptions.valueLabels = chartOptionsDto.valueLabels;

    return pieChartOptions;
  }

  mapDtoToUi(dashboardDto: DashboardDto): Dashboard {
    return {
      id: dashboardDto.id,
      title: dashboardDto.title,
      companyId: dashboardDto.companyId,
      // creator: dashboardDto.creator,
      creatorId: dashboardDto.creatorId,
      charts: dashboardDto.chartDtos.map(chartDto => {
        let chartOptions: BaseChartOptionsDto;
        switch (chartDto.chartType) {
          case ChartInformationTypeEnum.LINE_PROGRESS:
            chartOptions = DashboardMapperService.mapLineChartOptions(chartDto as LineChartOptionsDto);
            break;
          case ChartInformationTypeEnum.PIE_TOPICDRAFTOVERVIEW:
            chartOptions = DashboardMapperService.mapPieChartOptions(chartDto as PieChartOptionsDto);
            break;
          default:
            chartOptions = undefined;
            break;
        }

        return chartOptions?.buildChartOptions();
      }),
      creationDate: dashboardDto.creationDate,
    };
  }

  // mapUiToDto(dashboard: Dashboard): DashboardCreationDto {
  //   return {
  //     id: dashboard.id,
  //     title: dashboard.title,
  //     companyId: dashboard.companyId,
  //     creatorId: dashboard.creatorId,
  //     // creationDate: dashboard.creationDate,
  //     chartCreationOptions: dashboard.charts.map(this.chartMapper.mapEntityToDto),
  //   };
  // }

  mapUiToDto(dashboard: Dashboard): DashboardDto {
    return {
      id: dashboard.id,
      title: dashboard.title,
      companyId: dashboard.companyId,
      // creator: dashboard.creator,
      creatorId: dashboard.creatorId,
      creationDate: dashboard.creationDate,
      chartDtos: dashboard.charts.map(this.chartMapper.mapEntityToDto),
    };
  }
}
