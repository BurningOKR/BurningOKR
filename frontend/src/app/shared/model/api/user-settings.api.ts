import { UserId } from './user';
import { CompanyId } from './company.dto';

export type UserSettingsId = number;

export interface UserSettingsApi {
  id: UserSettingsId;
  userId: UserId;
  defaultCompanyId: CompanyId;
  defaultTeamId: number;
}
