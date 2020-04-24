import { DepartmentId } from '../id-types';

export interface DepartmentDto {
  structureId?: DepartmentId;
  structureName: string;
  subDepartmentIds?: number[];
  objectiveIds?: number[];
  parentStructureId?: number;
  label: string;
  okrMasterId: string;
  okrTopicSponsorId: string;
  okrMemberIds: string[];
  isActive: boolean;
  isParentStructureADepartment?: boolean;
}
