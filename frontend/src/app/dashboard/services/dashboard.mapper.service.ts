import { Injectable } from '@angular/core';
import { ChartInformationTypeEnum } from '../model/dto/chart-creation-options.dto';
import { DashboardDto } from '../model/dto/dashboard.dto';
import { Dashboard } from '../model/ui/dashboard';
import { LineChartOptionsDto } from '../model/dto/chart-options/line-chart-options.dto';
import { PieChartOptionsDto } from '../model/dto/chart-options/pie-chart-options.dto';
import { BaseChartOptionsDto } from '../model/dto/chart-options/base-chart-options.dto';
import { ChartMapperService } from './chart.mapper.service';
import { DashboardCreationDto } from '../model/dto/dashboard-creation.dto';

@Injectable({
  providedIn: 'root',
})
export class DashboardMapperService {

  constructor(private chartMapper: ChartMapperService) {
  }

  static mapLineChartOptions(chartOptionsDto: LineChartOptionsDto): LineChartOptionsDto {
    const lineChartOptions: LineChartOptionsDto = new LineChartOptionsDto();
    lineChartOptions.chartType = chartOptionsDto.chartType;
    lineChartOptions.title = chartOptionsDto.title;
    console.log(`Teams ID before Map: ${chartOptionsDto.selectedTeamIds}`);
    lineChartOptions.selectedTeamIds = chartOptionsDto.selectedTeamIds;
    console.log(`Teams ID after Map: ${lineChartOptions.selectedTeamIds}`);
    lineChartOptions.xaxisCategories = chartOptionsDto.xaxisCategories;
    lineChartOptions.series = chartOptionsDto.series;

    return lineChartOptions;
  }

  static mapPieChartOptions(chartOptionsDto: PieChartOptionsDto): PieChartOptionsDto {
    const pieChartOptions: PieChartOptionsDto = new PieChartOptionsDto();
    pieChartOptions.chartType = chartOptionsDto.chartType;
    pieChartOptions.title = chartOptionsDto.title;
    console.log(`Teams ID before Map: ${chartOptionsDto.selectedTeamIds}`);
    pieChartOptions.selectedTeamIds = chartOptionsDto.selectedTeamIds;
    console.log(`Teams ID after Map: ${pieChartOptions.selectedTeamIds}`);
    pieChartOptions.series = chartOptionsDto.series;
    pieChartOptions.valueLabels = chartOptionsDto.valueLabels;

    return pieChartOptions;
  }

  mapDtoToUi(dashboardDto: DashboardDto): Dashboard {
    console.log(dashboardDto.chartDtos.map(chart => {
      console.log(`Team IDs: ${chart.selectedTeamIds}`);
    }));

    return {
      id: dashboardDto.id,
      title: dashboardDto.title,
      companyId: dashboardDto.companyId,
      creator: dashboardDto.creator,
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

  mapUiToDto(dashboard: Dashboard): DashboardCreationDto {
    return {
      id: dashboard.id,
      title: dashboard.title,
      companyId: dashboard.companyId,
      creatorId: +dashboard.creator.id,
      // creationDate: dashboard.creationDate,
      chartCreationOptions: dashboard.charts.map(this.chartMapper.mapEntityToDto),
    };
  }

  // mapUiToDto(dashboard: Dashboard): DashboardDto {
  //   return {
  //     id: dashboard.id,
  //     title: dashboard.title,
  //     companyId: dashboard.companyId,
  //     creator: dashboard.creator,
  //     creationDate: dashboard.creationDate,
  //     chartDtos: dashboard.charts.map(this.chartMapper.mapEntityToDto),
  //   };
  // }
}
