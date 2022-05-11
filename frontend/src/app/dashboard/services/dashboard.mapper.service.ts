import { Injectable } from '@angular/core';
import { plainToClass } from 'class-transformer';
import { ChartInformationTypeEnum } from '../model/dto/chart-creation-options.dto';
import { BaseChartOptionsDto } from '../model/dto/chart-options/base-chart-options.dto';
import { LineChartOptionsDto } from '../model/dto/chart-options/line-chart-options.dto';
import { PieChartOptionsDto } from '../model/dto/chart-options/pie-chart-options.dto';
import { DashboardDto } from '../model/dto/dashboard.dto';
import { Dashboard } from '../model/ui/dashboard';

@Injectable({
  providedIn: 'root',
})
export class DashboardMapperService {

  mapDtoToUi(dashboardDto: DashboardDto): Dashboard {
    const new_dashboardDto: DashboardDto = plainToClass(DashboardDto, dashboardDto);
    console.log(dashboardDto.creator);

    return {
      id: new_dashboardDto.id,
      title: new_dashboardDto.title,
      creator: new_dashboardDto.creator,
      charts: new_dashboardDto.chartDtos.map(chartDto => {
        const ChartTypeOptionStringRecord: Record<ChartInformationTypeEnum, BaseChartOptionsDto> = {
          [ChartInformationTypeEnum.LINE_PROGRESS]: plainToClass(LineChartOptionsDto, chartDto),
          [ChartInformationTypeEnum.PIE_TOPICDRAFTOVERVIEW]: plainToClass(PieChartOptionsDto, chartDto),
        };

        return ChartTypeOptionStringRecord[chartDto.chart].buildChartOptions();
      }),
      creationDate: new_dashboardDto.creationDate,
    };
  }
}
