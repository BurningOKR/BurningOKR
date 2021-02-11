import { OkrUnitId } from '../../id-types';
import { OkrTopicDescriptionDto } from './okr-topic-description.dto';

export class OkrTopicDraftDto extends OkrTopicDescriptionDto {
  parentUnitId: OkrUnitId;
}
