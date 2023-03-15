import { Injectable } from '@angular/core';
import { BaseChartOptions } from '../model/ui/base-chart-options';
import { ChartInformationTypeEnum } from '../model/dto/chart-creation-options.dto';
import { BaseChartOptionsDto } from '../model/dto/chart-options/base-chart-options.dto';
import { LineChartOptionsDto } from '../model/dto/chart-options/line-chart-options.dto';
import { PieChartOptionsDto } from '../model/dto/chart-options/pie-chart-options.dto';
import { LineChartOptions } from '../model/ui/line-chart-options';
import { PieChartOptions } from '../model/ui/pie-chart-options';

@Injectable({
  providedIn: 'root',
})
export class ChartMapperService {
  private static mapLineChartEntityToDto(lineChartOptions: LineChartOptions): LineChartOptionsDto {
    const lineChartOptionsDto: LineChartOptionsDto = new LineChartOptionsDto();
    lineChartOptionsDto.id = lineChartOptions.id;
    lineChartOptionsDto.title = lineChartOptions.title.text;
    lineChartOptionsDto.chartType = lineChartOptions.chartType;
    lineChartOptionsDto.selectedTeamIds = lineChartOptions.selectedTeamIds;

    return lineChartOptionsDto;
  }

  private static mapPieChartEntityToDto(pieChartOptions: PieChartOptions): PieChartOptionsDto {
    const pieChartOptionsDto: PieChartOptionsDto = new PieChartOptionsDto();
    pieChartOptionsDto.id = pieChartOptions.id;
    pieChartOptionsDto.title = pieChartOptions.title.text;
    pieChartOptionsDto.chartType = pieChartOptions.chartType;
    pieChartOptionsDto.selectedTeamIds = pieChartOptions.selectedTeamIds;

    return pieChartOptionsDto;
  }

  //TODO: this. throws undefined error.
  mapEntityToDto(baseChartOptions: BaseChartOptions): BaseChartOptionsDto {
    switch (baseChartOptions.chartType) {
      case ChartInformationTypeEnum.LINE_PROGRESS:
        return ChartMapperService.mapLineChartEntityToDto(baseChartOptions as LineChartOptions);
      case ChartInformationTypeEnum.PIE_TOPICDRAFTOVERVIEW:
        return ChartMapperService.mapPieChartEntityToDto(baseChartOptions as PieChartOptions);
      default:
        return null;
    }
  }
}
