import { TestBed } from '@angular/core/testing';
import { User } from '../../shared/model/api/user';
import { ChartInformationTypeEnum } from '../model/dto/chart-creation-options.dto';
import { LineChartOptionsDto } from '../model/dto/chart-options/line-chart-options.dto';
import { PieChartOptionsDto } from '../model/dto/chart-options/pie-chart-options.dto';
import { DashboardDto } from '../model/dto/dashboard.dto';
import { Dashboard } from '../model/ui/dashboard';
import { LineChartOptions } from '../model/ui/line-chart-options';
import { PieChartOptions } from '../model/ui/pie-chart-options';

import { DashboardMapperService } from './dashboard.mapper.service';

describe('Dashboard.MapperService', () => {
  let service: DashboardMapperService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DashboardMapperService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should map Dashboard with ONE PieChartOptionsDto', () => {
    const currentDate: Date = new Date();
    const dashboardDto: DashboardDto = {
      id: 1,
      creator: getTestUser(),
      creationDate: currentDate,
      title: 'Some Dashboard',
      chartDtos: [getPieChartOptionsDto()],
    };

    const expected: Dashboard = {
      id: 1,
      creator: getTestUser(),
      creationDate: currentDate,
      title: 'Some Dashboard',
      charts: [getPieChartOptionsDto().buildChartOptions()],
    };

    expect(service.mapDtoToUi(dashboardDto)).toEqual(expected);
  });

  it('should map Dashboard with ONE LineChartOptionsDto', () => {
    const currentDate: Date = new Date();
    const dashboardDto: DashboardDto = {
      id: 1,
      creator: getTestUser(),
      creationDate: currentDate,
      title: 'Some Dashboard',
      chartDtos: [getLineChartOptionsDto()],
    };

    const expected: Dashboard = {
      id: 1,
      creator: getTestUser(),
      creationDate: currentDate,
      title: 'Some Dashboard',
      charts: [getLineChartOptionsDto().buildChartOptions()],
    };

    expect(service.mapDtoToUi(dashboardDto)).toEqual(expected);
  });

  it('should map with multiple ChartDtos', () => {
    const currentDate: Date = new Date();
    const dashboardDto: DashboardDto = {
      id: 1,
      creator: getTestUser(),
      creationDate: currentDate,
      title: 'Some Dashboard',
      chartDtos: [getLineChartOptionsDto(), getPieChartOptionsDto()],
    };

    const expected: Dashboard = {
      id: 1,
      creator: getTestUser(),
      creationDate: currentDate,
      title: 'Some Dashboard',
      charts: [getLineChartOptionsDto().buildChartOptions(), getPieChartOptionsDto().buildChartOptions()],
    };

    expect(service.mapDtoToUi(dashboardDto)).toEqual(expected);
  });

  it('should build pieChartOptions with correct values', () => {
    const pieChartOptionsDto: PieChartOptionsDto = getPieChartOptionsDto();
    const actual: PieChartOptions = pieChartOptionsDto.buildChartOptions();

    expect(actual.title.text).toBe(pieChartOptionsDto.title);
    expect(actual.series).toBe(pieChartOptionsDto.series);
    expect(actual.labels).toBe(pieChartOptionsDto.valueLabels);
    expect(actual.chart.type).toBe('pie');
  });

  it('should build lineChartOptions with correct values', () => {
    const lineChartOptionsDto: LineChartOptionsDto = getLineChartOptionsDto();
    const actual: LineChartOptions = lineChartOptionsDto.buildChartOptions();

    expect(actual.title.text).toBe(lineChartOptionsDto.title);
    expect(actual.series).toBe(lineChartOptionsDto.series);
    expect(actual.xaxis.categories).toBe(lineChartOptionsDto.xaxisCategories);
    expect(actual.chart.type).toBe('line');
  });
});

function getLineChartOptionsDto(): LineChartOptionsDto {
  const lineChartOptionsDto: LineChartOptionsDto = new LineChartOptionsDto();
  lineChartOptionsDto.title = 'Test Line Chart';
  lineChartOptionsDto.series = [{ data: [1, 2], name: 'lineName' }];
  lineChartOptionsDto.xaxisCategories = ['a', 'b'];
  lineChartOptionsDto.chartType = ChartInformationTypeEnum.LINE_PROGRESS;

  return lineChartOptionsDto;
}

function getPieChartOptionsDto(): PieChartOptionsDto {
  const pieChartOptionsDto: PieChartOptionsDto = new PieChartOptionsDto();
  pieChartOptionsDto.title = 'Test Pie Chart';
  pieChartOptionsDto.series = [1, 2, 3, 4, 5];
  pieChartOptionsDto.valueLabels = ['a,b,c,d,e'];
  pieChartOptionsDto.chartType = ChartInformationTypeEnum.PIE_TOPICDRAFTOVERVIEW;

  return pieChartOptionsDto;
}

function getTestUser(): User {
  return {
    id: '123',
    surname: 'Hans',
    givenName: 'Wurst',
    active: true,
    email: 'Hans@Wurst.de',
  } as User;
}
