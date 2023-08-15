import { TestBed } from '@angular/core/testing';

import { ChartMapperService } from './chart.mapper.service';
import { BaseChartOptions } from '../model/ui/base-chart-options';
import { BaseChartOptionsDto } from '../model/dto/chart-options/base-chart-options.dto';
import { ChartInformationTypeEnum } from '../model/dto/chart-creation-options.dto';

describe('ChartMapperService', () => {
  let service: ChartMapperService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ChartMapperService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should map BaseChartOptions (Line) to BaseChartOptionsDto', () => {
    const baseChartOptions: BaseChartOptions = {
      id: 0,
      title: { text: 'expectedTitle', align: 'left' },
      chart: undefined,
      series: [],
      chartType: ChartInformationTypeEnum.LINE_PROGRESS,
      selectedTeamIds: [],
      colors: [],
      chartOptionsName: '',

    };

    const baseChartOptionsDto: BaseChartOptionsDto = {
      id: 0,
      title: 'expectedTitle',
      chartType: ChartInformationTypeEnum.LINE_PROGRESS,
      selectedTeamIds: [],

    } as any;

    expect(service.mapEntityToDto(baseChartOptions)).toEqual(baseChartOptionsDto);
  });

  it('should map BaseChartOptions (Pie) to BaseChartOptionsDto', () => {
    const baseChartOptions: BaseChartOptions = {
      id: 0,
      title: { text: 'expectedTitle', align: 'left' },
      chart: undefined,
      series: [],
      chartType: ChartInformationTypeEnum.PIE_TOPICDRAFTOVERVIEW,
      selectedTeamIds: [],
      colors: [],
      chartOptionsName: '',

    };

    const baseChartOptionsDto: BaseChartOptionsDto = {
      id: 0,
      title: 'expectedTitle',
      chartType: ChartInformationTypeEnum.PIE_TOPICDRAFTOVERVIEW,
      selectedTeamIds: [],

    } as any;

    expect(service.mapEntityToDto(baseChartOptions)).toEqual(baseChartOptionsDto);
  });
});
