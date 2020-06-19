import { OkrChildUnitDto } from './okr-child-unit.dto';

export class OkrDepartmentDto extends OkrChildUnitDto {
  okrMasterId: string;
  okrTopicSponsorId: string;
  okrMemberIds: string[];
}
