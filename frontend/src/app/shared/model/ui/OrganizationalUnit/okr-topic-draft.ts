import { OkrUnitId, UserId } from '../../id-types';
import { OkrTopicDescription } from './okr-topic-description';

export class OkrTopicDraft extends OkrTopicDescription {
  parentUnitId: OkrUnitId;
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

  constructor(
    parentUnitId: OkrUnitId,
    topicDraftId: number,
    name: string,
    initiatorId: UserId,
    startTeam: UserId[],
    stakeholders: UserId[],
    acceptanceCriteria: string,
    contributesTo: string,
    delimitation: string,
    beginning: Date,
    dependencies: string,
    resources: string,
    handoverPlan: string) {
      super(topicDraftId, name, initiatorId, startTeam, stakeholders, acceptanceCriteria,
      contributesTo, delimitation, beginning, dependencies, resources, handoverPlan);
      this.parentUnitId = parentUnitId;
    }
}
