import { OkrUnitId } from '../../id-types';
import { OkrTopicDescriptionDto } from './okr-topic-description.dto';
import { User } from '../user';

export class OkrTopicDraftDto extends OkrTopicDescriptionDto {
  okrParentUnitId: OkrUnitId;
  currentStatus: number;
  initiator: User;
}
