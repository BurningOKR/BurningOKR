import { DepartmentDescriptionId, UserId } from '../../id-types';

export class OkrDepartmentDescription {
  descriptionId: DepartmentDescriptionId;
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

  constructor(descriptionId: DepartmentDescriptionId, name: string, initiatorId: UserId, startTeam: UserId[], stakeholders: UserId[],
              acceptanceCriteria: string, contributesTo: string, delimitation: string, beginning: Date,
              dependencies: string, resources: string, handoverPlan: string) {
    this.descriptionId = descriptionId;
    this.name = name;
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
