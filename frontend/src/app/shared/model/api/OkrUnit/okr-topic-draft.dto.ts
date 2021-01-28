import { TopicDraftId, UserId } from '../../id-types';
import { OkrChildUnitDto } from './okr-child-unit.dto';

export class OkrTopicDraftDto extends OkrChildUnitDto {
  topicDraftID: TopicDraftId;
  name: string;
  initiatorId: UserId;
  startTeam: UserId[];
  stakeholders: UserId[];
  acceptanceCriteria: string;
  contributesTo: string;
  delimitation: string;
  beginning: Date;
  dependencies: string;
  resources: string;
  handoverPlan: string;
}
