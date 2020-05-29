import { SubStructureDto } from './sub-structure.dto';

export interface DepartmentDto extends SubStructureDto {
  okrMasterId: string;
  okrTopicSponsorId: string;
  okrMemberIds: string[];
  isParentStructureADepartment?: boolean;
}
