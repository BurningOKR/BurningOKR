import { Injectable } from '@angular/core';
import { DashboardDto } from '../model/dto/dashboard.dto';
import { Dashboard } from '../model/ui/dashboard';

@Injectable({
  providedIn: 'root',
})
export class DashboardMapperService {
  mapDtoToUi(dashboardDto: DashboardDto): Dashboard {
    return {
      id: dashboardDto.id,
      title: dashboardDto.title,
      creator: dashboardDto.creator,
      charts: dashboardDto.chartDtos,
      creationDate: dashboardDto.creationDate,
    };
  }

  mapUiToDto(dashboard: Dashboard): DashboardDto {
    return {
      id: dashboard.id,
      title: dashboard.title,
      creator: dashboard.creator,
      chartDtos: dashboard.charts,
      creationDate: dashboard.creationDate,
    };
  }
}
