import { Injectable } from '@angular/core';
import { BaseChartOptionsDto } from '../model/dto/chart-options/base-chart-options.dto';
import { BaseChartOptions } from '../model/ui/base-chart-options';
import { ChartInformationTypeEnum } from '../model/dto/chart-creation-options.dto';

@Injectable({
  providedIn: 'root',
})
export class ChartMapperService {
  //TODO: this.getChartType() throws undefined error.
  static getChartType(baseChartOptions: BaseChartOptions): ChartInformationTypeEnum {
    switch (baseChartOptions.chart.type) {
      case 'line':
        return ChartInformationTypeEnum.LINE_PROGRESS;
      case 'pie':
        return ChartInformationTypeEnum.PIE_TOPICDRAFTOVERVIEW;
      default:
        return null;
    }
  }

  // mapDtoToEntity(dto: BaseChartOptionsDto): BaseChartOptions {
  //   return null;
  // }

  mapEntityToDto(baseChartOptions: BaseChartOptions): BaseChartOptionsDto {
    return {
      title: baseChartOptions.title.text,
      chart: ChartMapperService.getChartType(baseChartOptions),
    } as any; //buildChartOptions function cannot be mapped.
  }
}
