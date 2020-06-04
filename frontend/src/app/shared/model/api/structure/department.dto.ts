import { SubStructureDto } from './sub-structure.dto';

export class DepartmentDto extends SubStructureDto {
  okrMasterId: string;
  okrTopicSponsorId: string;
  okrMemberIds: string[];
  isParentStructureACorporateObjectiveStructure?: boolean;
}
