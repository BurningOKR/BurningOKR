import { ObjectiveId, UserId } from '../../id-types';
import { OkrChildUnit } from './okr-child-unit';

export class OkrDepartment extends OkrChildUnit {
  okrMasterId: UserId;
  okrTopicSponsorId: UserId;
  okrMemberIds: UserId[];

  constructor(
    id: number,
    name: string,
    objectives: ObjectiveId[],
    parentUnitId: number,
    label: string,
    okrMasterId: string,
    okrTopicSponsorId: string,
    okrMemberIds: UserId[],
    isActive: boolean,
    isParentUnitABranch: boolean,
  ) {
    super(id, name, label, objectives, parentUnitId, isActive);
    this.okrMasterId = okrMasterId;
    this.okrTopicSponsorId = okrTopicSponsorId;
    this.okrMemberIds = okrMemberIds;
    this.isParentUnitABranch = isParentUnitABranch;
  }
}
