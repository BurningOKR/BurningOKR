import { OkrChildUnitDto } from './okr-child-unit.dto';

export interface OkrDepartmentDto extends OkrChildUnitDto {
  okrMasterId: string;
  okrTopicSponsorId: string;
  okrMemberIds: string[];
}
