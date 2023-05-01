import { OkrTopicDescriptionDto } from '../../model/api/OkrUnit/okr-topic-description.dto';
import { Injectable } from '@angular/core';
import { OkrTopicDescription } from '../../model/ui/OrganizationalUnit/okr-topic-description';
import { DepartmentId } from '../../model/id-types';
import { Observable } from '../../../../typings';
import { TopicDescriptionApiService } from '../api/topic-description-api.service';
import { map } from 'rxjs/internal/operators';
import { DateMapper } from './date.mapper';

@Injectable({
  providedIn: 'root',
})

export class TopicDescriptionMapper {
  constructor(
    private topicDescriptionApiService: TopicDescriptionApiService,
    private dateMapper: DateMapper,
  ) {
  }

  mapTopicDescriptionDto(description: OkrTopicDescriptionDto): OkrTopicDescription {
    return new OkrTopicDescription(
      description.id,
      description.name,
      description.initiatorId,
      description.startTeam,
      description.stakeholders,
      description.description,
      description.contributesTo,
      description.delimitation,
      DateMapper.mapDateStringToDate(description.beginning),
      description.dependencies,
      description.resources,
      description.handoverPlan,
    );
  }

  mapTopicDescription(description: OkrTopicDescription): OkrTopicDescriptionDto {
    const descriptionDto: OkrTopicDescriptionDto = new OkrTopicDescriptionDto();
    descriptionDto.id = description.id;
    descriptionDto.name = description.name;
    descriptionDto.initiatorId = description.initiatorId;
    descriptionDto.startTeam = description.startTeam;
    descriptionDto.stakeholders = description.stakeholders;
    descriptionDto.description = description.description;
    descriptionDto.contributesTo = description.contributesTo;
    descriptionDto.delimitation = description.delimitation;
    descriptionDto.beginning = DateMapper.mapDateToDateString(description.beginning);
    descriptionDto.dependencies = description.dependencies;
    descriptionDto.resources = description.resources;
    descriptionDto.handoverPlan = description.handoverPlan;

    return descriptionDto;
  }

  getTopicDescriptionById$(departmentId: DepartmentId): Observable<OkrTopicDescription> {
    return this.topicDescriptionApiService
      .getTopicDescriptionById$(departmentId)
      .pipe(map(descriptionDto => this.mapTopicDescriptionDto(descriptionDto)));
  }

  putTopicDescription$(departmentId: DepartmentId, description: OkrTopicDescription): Observable<OkrTopicDescription> {
    return this.topicDescriptionApiService
      .putTopicDescription$(departmentId, this.mapTopicDescription(description))
      .pipe(map(descriptionDto => this.mapTopicDescriptionDto(descriptionDto)));
  }
}
