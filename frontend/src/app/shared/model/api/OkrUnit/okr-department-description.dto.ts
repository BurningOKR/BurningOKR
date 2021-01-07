import { UserId, DepartmentDescriptionId } from '../../id-types';

export interface OkrDepartmentDescriptionDto {
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
}
