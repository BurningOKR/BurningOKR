import { OkrUnitId, UserId } from '../../../id-types';
import { OkrTopicDescription } from '../okr-topic-description';
import { status } from './okr-topic-draft-status-enum';
import { User } from '../../../api/user';

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
  currentStatus: status;
  initiator: User;

  constructor(
    parentUnitId: OkrUnitId,
    currentStatus: status,
    initiator: User,
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
      this.currentStatus = currentStatus;
      this.initiator = initiator;
    }
}
