import { OkrDepartmentDescriptionDto } from '../../model/api/OkrUnit/okr-department-description.dto';
import { Injectable } from '@angular/core';
import { OkrDepartmentDescription } from '../../model/ui/OrganizationalUnit/okr-department-description';
import { DepartmentDescriptionId } from '../../model/id-types';
import { Observable } from '../../../../typings';
import { DepartmentDescriptionApiService } from '../api/department-description-api.service';
import { map } from 'rxjs/internal/operators';

@Injectable({
  providedIn: 'root'
})

export class DepartmentDescriptionMapper {
  constructor(private departmentDescriptionApiService: DepartmentDescriptionApiService) {
  }

  static mapDepartmentDescriptionDto(description: OkrDepartmentDescriptionDto): OkrDepartmentDescription {
    return new OkrDepartmentDescription(
      description.descriptionId,
      description.name,
      description.initiatorId,
      description.startTeam,
      description.stakeholders,
      description.acceptanceCriteria,
      description.contributesTo,
      description.delimitation,
      description.beginning,
      description.dependencies,
      description.resources,
      description.handoverPlan
    );
  }

  static mapDepartmentDescriptionUnit(description: OkrDepartmentDescription): OkrDepartmentDescriptionDto {
      const descriptionDto: OkrDepartmentDescriptionDto = new OkrDepartmentDescriptionDto();
      descriptionDto.descriptionId = description.descriptionId;
      descriptionDto.name = description.name;
      descriptionDto.initiatorId = description.initiatorId;
      descriptionDto.startTeam = description.startTeam;
      descriptionDto.stakeholders = description.stakeholders;
      descriptionDto.acceptanceCriteria = description.acceptanceCriteria;
      descriptionDto.contributesTo = description.contributesTo;
      descriptionDto.delimitation = description.delimitation;
      descriptionDto.beginning = description.beginning;
      descriptionDto.dependencies = description.dependencies;
      descriptionDto.resources = description.resources;
      descriptionDto.handoverPlan = description.handoverPlan;

      return descriptionDto;
    }

  getDepartmentDescriptionById$(descriptionId: DepartmentDescriptionId): Observable<OkrDepartmentDescription> {
      return this.departmentDescriptionApiService
        .getDepartmentDescriptionById$(descriptionId)
        .pipe(map(descriptionDto => DepartmentDescriptionMapper.mapDepartmentDescriptionDto(descriptionDto)));
    }
  postDepartmentDescription$(descriptionId: DepartmentDescriptionId,
                             description: OkrDepartmentDescription): Observable<OkrDepartmentDescription> {
    return this.departmentDescriptionApiService
      .postDepartmentDescription$(descriptionId, description)
      .pipe(map(descriptionDto => DepartmentDescriptionMapper.mapDepartmentDescriptionDto(descriptionDto)));
    }
  putDepartmentDescription$(descriptionId: DepartmentDescriptionId,
                            description: OkrDepartmentDescription): Observable<OkrDepartmentDescription> {
    return this.departmentDescriptionApiService
      .postDepartmentDescription$(descriptionId, description)
      .pipe(map(descriptionDto => DepartmentDescriptionMapper.mapDepartmentDescriptionDto(descriptionDto)));
    }
  }
