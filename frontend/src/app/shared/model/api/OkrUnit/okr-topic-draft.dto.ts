import { OkrUnitId } from '../../id-types';
import { OkrTopicDescriptionDto } from './okr-topic-description.dto';
import { User } from '../user';

export class OkrTopicDraftDto extends OkrTopicDescriptionDto {
  parentUnitId: OkrUnitId;
  currentStatus: number;
  initiator: User;
}
