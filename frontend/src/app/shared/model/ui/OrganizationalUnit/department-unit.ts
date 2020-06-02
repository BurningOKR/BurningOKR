import { ObjectiveId, UserId } from '../../id-types';
import { SubStructure } from './sub-structure';

export class DepartmentUnit extends SubStructure {
  okrMasterId: UserId;
  okrTopicSponsorId: UserId;
  okrMemberIds: UserId[];
  isParentStructureACorporateObjectiveStructure: boolean;

  constructor(
    id: number,
    name: string,
    objectives: ObjectiveId[],
    parentStructureId: number,
    label: string,
    okrMasterId: string,
    okrTopicSponsorId: string,
    okrMemberIds: UserId[],
    isActive: boolean,
    isParentStructureACorporateObjectiveStructure: boolean) {
    super(id, name, label, objectives, parentStructureId, isActive);
    this.okrMasterId = okrMasterId;
    this.okrTopicSponsorId = okrTopicSponsorId;
    this.okrMemberIds = okrMemberIds;
    this.isParentStructureACorporateObjectiveStructure = isParentStructureACorporateObjectiveStructure;
  }
}
