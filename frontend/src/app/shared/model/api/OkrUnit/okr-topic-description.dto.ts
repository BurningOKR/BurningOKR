import { TopicDescriptionId, UserId } from '../../id-types';

// Attributes changed here need to be changed in okr-topic-draft.dto.ts as well!
export class OkrTopicDescriptionDto {
  descriptionId: TopicDescriptionId;
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
