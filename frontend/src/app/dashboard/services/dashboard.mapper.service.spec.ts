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

  it('should map DashboardDto with ONE PieChartOptionsDto', () => {
    const singlePieTestDto: DashboardDto = {
      companyId: 0,
      id: 1,
      creatorId: getTestUser().id,
      creationDate: new Date(),
      title: 'Some Dashboard',
      chartDtos: [getPieChartOptionsDtoMock()],
    };

    const singlePieTestDb: Dashboard = {
      companyId: 0,
      id: 1,
      creatorId: getTestUser().id,
      creationDate: new Date(),
      title: 'Some Dashboard',
      charts: [getPieChartOptionsDtoMock().buildChartOptions()],
    };
    expect(service.mapDtoToUi(singlePieTestDto)).toEqual(singlePieTestDb);
  });

  it('should map DashboardDto with ONE LineChartOptionsDto', () => {
    const singleLineTestDto: DashboardDto = {
      companyId: 0,
      id: 1,
      creatorId: getTestUser().id,
      creationDate: new Date(),
      title: 'Some Dashboard',
      chartDtos: [getLineChartOptionsDtoMock()],
    };

    const singleLineTestDb: Dashboard = {
      companyId: 0,
      id: 1,
      creatorId: getTestUser().id,
      creationDate: new Date(),
      title: 'Some Dashboard',
      charts: [getLineChartOptionsDtoMock().buildChartOptions()],
    };
    expect(service.mapDtoToUi(singleLineTestDto)).toEqual(singleLineTestDb);
  });

  it('should map DashboardDto with multiple ChartDtos', () => {
    const testDto: DashboardDto = {
      companyId: 0,
      id: 1,
      creatorId: getTestUser().id,
      creationDate: new Date(),
      title: 'Some Dashboard',
      chartDtos: [getLineChartOptionsDtoMock(), getPieChartOptionsDtoMock()],
    };

    const testDb: Dashboard = {
      companyId: 0,
      id: 1,
      creatorId: getTestUser().id,
      creationDate: new Date(),
      title: 'Some Dashboard',
      charts: [getLineChartOptionsDtoMock().buildChartOptions(), getPieChartOptionsDtoMock().buildChartOptions()],
    };
    expect(service.mapDtoToUi(testDto)).toEqual(testDb);
  });

  it('should build pieChartOptions with correct values', () => {
    const pieChartOptionsDto: PieChartOptionsDto = getPieChartOptionsDtoMock();
    const actual: PieChartOptions = pieChartOptionsDto.buildChartOptions();

    expect(actual.title.text).toBe(pieChartOptionsDto.title);
    expect(actual.series).toBe(pieChartOptionsDto.series);
    expect(actual.labels).toBe(pieChartOptionsDto.valueLabels);
    expect(actual.chart.type).toBe('pie');
  });

  it('should build lineChartOptions with correct values', () => {
    const lineChartOptionsDto: LineChartOptionsDto = getLineChartOptionsDtoMock();
    const actual: LineChartOptions = lineChartOptionsDto.buildChartOptions();

    expect(actual.title.text).toBe(lineChartOptionsDto.title);
    expect(actual.series).toBe(lineChartOptionsDto.series);
    expect(actual.xaxis.categories).toBe(lineChartOptionsDto.xaxisCategories);
    expect(actual.chart.type).toBe('line');
  });

  it('should map Dashboard with ONE PieChartOptionsDto', () => {
    const singlePieTestDto: DashboardDto = {
      companyId: 0,
      id: 1,
      creatorId: getTestUser().id,
      creationDate: new Date(),
      title: 'Some Dashboard',
      chartDtos: [getPieChartOptionsDtoMock2()],
    };

    const singlePieTestDb: Dashboard = {
      companyId: 0,
      id: 1,
      creatorId: getTestUser().id,
      creationDate: new Date(),
      title: 'Some Dashboard',
      charts: [getPieChartOptionsDtoMock2().buildChartOptions()],
    };
    expect(service.mapUiToDto(singlePieTestDb)).toEqual(singlePieTestDto);
  });

  it('should map Dashboard with ONE LineChartOptionsDto', () => {
    const singleLineTestDto: DashboardDto = {
      companyId: 0,
      id: 1,
      creatorId: getTestUser().id,
      creationDate: new Date(),
      title: 'Some Dashboard',
      chartDtos: [getLineChartOptionsDtoMock2()],
    };

    const singleLineTestDb: Dashboard = {
      companyId: 0,
      id: 1,
      creatorId: getTestUser().id,
      creationDate: new Date(),
      title: 'Some Dashboard',
      charts: [getLineChartOptionsDtoMock2().buildChartOptions()],
    };
    expect(service.mapUiToDto(singleLineTestDb)).toEqual(singleLineTestDto);
  });

  it('should map Dashboard with multiple Charts', () => {
    const testDto: DashboardDto = {
      companyId: 0,
      id: 1,
      creatorId: getTestUser().id,
      creationDate: new Date(),
      title: 'Some Dashboard',
      chartDtos: [getLineChartOptionsDtoMock2(), getPieChartOptionsDtoMock2()],
    };

    const testDb: Dashboard = {
      companyId: 0,
      id: 1,
      creatorId: getTestUser().id,
      creationDate: new Date(),
      title: 'Some Dashboard',
      charts: [getLineChartOptionsDtoMock2().buildChartOptions(), getPieChartOptionsDtoMock2().buildChartOptions()],
    };
    expect(service.mapUiToDto(testDb)).toEqual(testDto);
  });

});

function getLineChartOptionsDtoMock2(): LineChartOptionsDto {
  const lineChartOptionsDto: LineChartOptionsDto = new LineChartOptionsDto();
  lineChartOptionsDto.title = 'Test Line Chart';
  lineChartOptionsDto.chartType = ChartInformationTypeEnum.LINE_PROGRESS;

  return lineChartOptionsDto;
}

function getPieChartOptionsDtoMock2(): PieChartOptionsDto {
  const pieChartOptionsDto: PieChartOptionsDto = new PieChartOptionsDto();
  pieChartOptionsDto.title = 'Test Pie Chart';
  pieChartOptionsDto.chartType = ChartInformationTypeEnum.PIE_TOPICDRAFTOVERVIEW;

  return pieChartOptionsDto;
}

function getLineChartOptionsDtoMock(): LineChartOptionsDto {
  const lineChartOptionsDto: LineChartOptionsDto = new LineChartOptionsDto();
  lineChartOptionsDto.title = 'Test Line Chart';
  lineChartOptionsDto.series = [{ data: [1, 2], name: 'lineName' }];
  lineChartOptionsDto.xaxisCategories = ['a', 'b'];
  lineChartOptionsDto.chartType = ChartInformationTypeEnum.LINE_PROGRESS;

  return lineChartOptionsDto;
}

function getPieChartOptionsDtoMock(): PieChartOptionsDto {
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
