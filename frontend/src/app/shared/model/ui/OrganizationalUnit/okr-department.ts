import { UserId } from '../../id-types';
import { OkrChildUnit } from './okr-child-unit';

export interface OkrDepartment extends OkrChildUnit {
  okrMasterId: UserId;
  okrTopicSponsorId: UserId;
  okrMemberIds: UserId[];
}
