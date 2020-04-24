import { CompanyId, UserId, UserSettingsId } from '../id-types';

export interface UserSettingsApi {
  id: UserSettingsId;
  userId: UserId;
  defaultCompanyId: CompanyId;
  defaultTeamId: number;
}
