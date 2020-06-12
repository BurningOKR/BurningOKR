import { CompanyId, OkrUnitId, UserId } from '../id-types';

export class UserSettings {
  id: number;
  userId: UserId;
  defaultCompanyId: CompanyId;
  defaultTeamId: OkrUnitId; // can be wrong

  constructor(id: number, userId: string, defaultCompanyId: CompanyId, defaultTeamId: OkrUnitId) {
    this.id = id;
    this.userId = userId;
    this.defaultCompanyId = defaultCompanyId;
    this.defaultTeamId = defaultTeamId;
  }
}
