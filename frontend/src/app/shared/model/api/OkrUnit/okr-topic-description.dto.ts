import { TopicDescriptionId, UserId } from '../../id-types';

export class OkrTopicDescriptionDto {
  id: TopicDescriptionId;
  name: string;
  initiatorId: UserId;
  startTeam: UserId[];
  stakeholders: UserId[];
  acceptanceCriteria: string;
  contributesTo: string;
  delimitation: string;
  beginning: number[];
  dependencies: string;
  resources: string;
  handoverPlan: string;
}
