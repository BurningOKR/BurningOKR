import { UserId, TopicDescriptionId } from '../../id-types';

export class OkrTopicDescriptionDto {
  descriptionId: TopicDescriptionId;
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
