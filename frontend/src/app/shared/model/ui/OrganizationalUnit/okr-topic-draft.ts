import { OkrChildUnit } from './okr-child-unit';
import { ObjectiveId, UserId } from '../../id-types';

export class OkrTopicDraft extends OkrChildUnit {
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
    id: number,
    name: string,
    label: string,
    objectives: ObjectiveId[],
    parentUnitId: number,
    isActive: boolean,
    isParentUnitABranch: boolean,
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
    super(id, name, label, objectives, parentUnitId, isActive, isParentUnitABranch);
    this.initiatorId = initiatorId;
    this.startTeam = startTeam;
    this.stakeholders = stakeholders;
    this.acceptanceCriteria = acceptanceCriteria;
    this.contributesTo = contributesTo;
    this.delimitation = delimitation;
    this.beginning = beginning;
    this.dependencies = dependencies;
    this.resources = resources;
    this.handoverPlan = handoverPlan;
  }
}
