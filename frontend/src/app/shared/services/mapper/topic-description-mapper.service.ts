import { OkrTopicDescriptionDto } from '../../model/api/OkrUnit/okr-topic-description.dto';
import { Injectable } from '@angular/core';
import { OkrTopicDescription } from '../../model/ui/OrganizationalUnit/okr-topic-description';
import { DepartmentId } from '../../model/id-types';
import { Observable } from '../../../../typings';
import { TopicDescriptionApiService } from '../api/topic-description-api.service';
import { map } from 'rxjs/internal/operators';

@Injectable({
  providedIn: 'root'
})

export class TopicDescriptionMapper {
  constructor(private topicDescriptionApiService: TopicDescriptionApiService) {
  }

  static mapTopicDescriptionDto(description: OkrTopicDescriptionDto): OkrTopicDescription {
    return new OkrTopicDescription(
      description.descriptionId,
      description.name,
      description.initiatorId,
      description.startTeam,
      description.stakeholders,
      description.acceptanceCriteria,
      description.contributesTo,
      description.delimitation,
      description.beginning ?
        new Date(description.beginning[0], description.beginning[1] - 1, description.beginning[2]) :
        null,
      description.dependencies,
      description.resources,
      description.handoverPlan
    );
  }

  static mapTopicDescription(description: OkrTopicDescription): OkrTopicDescriptionDto {
    const descriptionDto: OkrTopicDescriptionDto = new OkrTopicDescriptionDto();
    descriptionDto.descriptionId = description.descriptionId;
    descriptionDto.name = description.name;
    descriptionDto.initiatorId = description.initiatorId;
    descriptionDto.startTeam = description.startTeam;
    descriptionDto.stakeholders = description.stakeholders;
    descriptionDto.acceptanceCriteria = description.acceptanceCriteria;
    descriptionDto.contributesTo = description.contributesTo;
    descriptionDto.delimitation = description.delimitation;
    descriptionDto.beginning = description.beginning ? [
      Number(description.beginning.getFullYear()),
      Number(description.beginning.getMonth()) + 1,
      Number(description.beginning.getDate())
    ] : null;
    descriptionDto.dependencies = description.dependencies;
    descriptionDto.resources = description.resources;
    descriptionDto.handoverPlan = description.handoverPlan;

    return descriptionDto;
  }

  getTopicDescriptionById$(departmentId: DepartmentId): Observable<OkrTopicDescription> {
    return this.topicDescriptionApiService
      .getTopicDescriptionById$(departmentId)
      .pipe(map(descriptionDto => TopicDescriptionMapper.mapTopicDescriptionDto(descriptionDto)));
  }

  putTopicDescription$(departmentId: DepartmentId, description: OkrTopicDescription): Observable<OkrTopicDescription> {
    return this.topicDescriptionApiService
      .putTopicDescription$(departmentId, TopicDescriptionMapper.mapTopicDescription(description))
      .pipe(map(descriptionDto => TopicDescriptionMapper.mapTopicDescriptionDto(descriptionDto)));
  }
}
