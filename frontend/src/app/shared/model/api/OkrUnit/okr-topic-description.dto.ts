import { TopicDescriptionId, UserId } from '../../id-types';

export class OkrTopicDescriptionDto {
  id: TopicDescriptionId;
  name: string;
  initiatorId: UserId;
  startTeam: UserId[];
  stakeholders: UserId[];
  description: string;
  contributesTo: string;
  delimitation: string;
  beginning: string;
  dependencies: string;
  resources: string;
  handoverPlan: string;
}
