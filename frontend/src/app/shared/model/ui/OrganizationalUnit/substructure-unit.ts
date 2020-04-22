import { CompanyStructure } from './company-structure';
import { UserId } from '../../id-types';

export class DepartmentUnit extends CompanyStructure {
  subDepartmentIds: number[];
  parentStructureId: number;
  okrMasterId: UserId;
  okrTopicSponsorId: UserId;
  okrMemberIds: UserId[];
  isActive: boolean;
  isParentStructureADepartment: boolean;

  constructor(
    id: number,
    name: string,
    subDepartmentIds: number[],
    objectives: number[],
    parentStructureId: number,
    label: string,
    okrMasterId: string,
    okrTopicSponsorId: string,
    okrMemberIds: UserId[],
    isActive: boolean,
    isParentStructureADepartment: boolean
  ) {
    super(id, name, objectives, label);
    this.subDepartmentIds = subDepartmentIds;
    this.parentStructureId = parentStructureId;
    this.okrMasterId = okrMasterId;
    this.okrTopicSponsorId = okrTopicSponsorId;
    this.okrMemberIds = okrMemberIds;
    this.isActive = isActive;
    this.isParentStructureADepartment = isParentStructureADepartment;
  }
}
